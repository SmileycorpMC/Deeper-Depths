package com.deeperdepths.common.capabilities;

import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.ISoundFrequency;
import com.deeperdepths.common.advancements.DeeperDepthsAdvancements;
import com.deeperdepths.config.VibrationsConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.smileycorp.atlas.api.util.DirectionUtils;

import javax.annotation.Nullable;
import java.util.*;

public class VibrationHandler {
    
    private static final Map<Integer, VibrationHandler> INSTANCES = Maps.newHashMap();
    private static final float[] RESONANCE_PITCHES = {0.5f, 0.5f, 0.561231024154687f, 0.629960524947437f, 0.707106781186548f,
            0.749153538438341f, 0.840896415253715f, 0.890898718140339f, 1, 1.12246204830937f, 1.18920711500272f, 1.4142135623731f,
            1.68179283050743f, 1.78179743628068f, 2};

    private final WorldServer world;
    private final HashSet<Listener> listeners = Sets.newHashSet();
    private Entity ctxEntity = null;
    
    public VibrationHandler(WorldServer world) {
        this.world = world;
    }

    public void add(Listener listener) {
        listeners.add(listener);
        listener.getVibrationData().spawnParticles(world);
    }
    
    public void remove(Listener listener) {
        listeners.remove(listener);
    }
    
    public void playSound(@Nullable Entity entity, SoundEvent sound, double x, double y, double z) {
        if (ctxEntity != null) entity = ctxEntity;
        if (entity != null && entity.isSneaking()) {
            Entity entity1 = entity;
            if (entity instanceof EntityPlayerMP && listeners.stream().anyMatch(listener ->
                    canHear(listener, listener.getListenerPos(), entity1, sound, x, y, z)))
                DeeperDepthsAdvancements.SNEAK_100.trigger((EntityPlayerMP) entity);
            ctxEntity = null;
            return;
        }
        for (Listener listener : listeners) {
            Vec3d pos = listener.getListenerPos();
            if (!canHear(listener, pos, entity, sound, x, y, z)) continue;
            listener.addVibration(world, entity, sound, x, y, z);
        }
        if (sound == DeeperDepthsSoundEvents.AMETHYST_BLOCK_RESONATE) ((ISoundFrequency)sound).setSoundFrequency(0);
        ctxEntity = null;
    }

    public boolean canHear(Listener listener, Vec3d pos, @Nullable Entity entity, SoundEvent sound, double x, double y, double z) {
        return pos.squareDistanceTo(x, y, z) <= Math.pow(listener.getRange(), 2) &! isOccluded(x, y, z, pos) && listener.canHear(entity, sound, x, y, z);
    }

    public boolean isOccluded(double soundX, double soundY, double soundZ, Vec3d pos) {
        return DirectionUtils.hasCollision(world, new Vec3d(soundX, soundY, soundZ), pos, (world, pos1, state, block) -> VibrationsConfig.occludesVibrations(state));
    }

    public void playResonance(BlockPos pos, int frequency) {
        SoundEvent sound = DeeperDepthsSoundEvents.AMETHYST_BLOCK_RESONATE;
        ((ISoundFrequency)sound).setSoundFrequency(frequency);
        world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, sound, SoundCategory.BLOCKS, 1, RESONANCE_PITCHES[frequency]);
    }

    public void setEntityContext(Entity entity) {
        ctxEntity = entity;
    }
    
    public static VibrationHandler getInstance(WorldServer world) {
        int dimension = world.provider.getDimension();
        if (INSTANCES.containsKey(dimension)) {
            VibrationHandler instance = INSTANCES.get(dimension);
            if (instance.world == world) return instance;
            INSTANCES.clear();
        }
        VibrationHandler instance = new VibrationHandler(world);
        INSTANCES.put(dimension, instance);
        return instance;
    }
    
    public interface Listener {

        Vec3d getListenerPos();

        int getRange();

        boolean canHear(@Nullable Entity entity, SoundEvent sound, double x, double y, double z);

        void reactToSound(@Nullable Entity entity, SoundEvent sound, double x, double y, double z);

        Data getVibrationData();

        default void addVibration(WorldServer world, @Nullable Entity entity, SoundEvent sound, double x, double y, double z) {
            getVibrationData().addVibration(world, entity, sound, x, y, z);
        }

    }

    public static class Data {

        private final Listener listener;
        private final List<Vibration> vibrations = Lists.newArrayList();

        public Data(Listener listener) {
            this.listener = listener;
        }

        public void addVibration(WorldServer world, @Nullable Entity entity, SoundEvent sound, double x, double y, double z) {
            Vibration vibration = new Vibration(x, y, z, listener, entity == null ? null : entity.getUniqueID(), sound);
            vibrations.add(vibration);
            vibration.spawnParticle(world);
        }

        public void tick(WorldServer world) {
            Iterator<Vibration> iterator = vibrations.iterator();
            while (iterator.hasNext()) {
                Vibration vibration = iterator.next();
                vibration.tick(world);
                if (!vibration.isActive) iterator.remove();
            }
        }

        public NBTTagList writeToNBT() {
            NBTTagList vibrations = new NBTTagList();
            this.vibrations.forEach(vibration -> vibrations.appendTag(vibration.toNbt()));
            return vibrations;
        }

        public void readFromNBT(NBTTagList vibrations) {
            this.vibrations.clear();
            vibrations.forEach(nbt -> this.vibrations.add(Vibration.fromNBT(listener, (NBTTagCompound) nbt)));
        }

        public void spawnParticles(WorldServer worldServer) {
            vibrations.forEach(vibration -> vibration.spawnParticle(worldServer));
        }

    }

    public static class Vibration {

        private double x, y, z;
        private final Listener destination;
        private final UUID entity;
        private final SoundEvent sound;
        private boolean isActive = true;

        public Vibration(double x, double y, double z, Listener destination, @Nullable UUID entity, SoundEvent sound) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.destination = destination;
            this.entity = entity;
            this.sound = sound;
        }

        public void tick(WorldServer world) {
            Vec3d destinationPos = destination.getListenerPos();
            if (destinationPos.squareDistanceTo(x, y, z) <= 1) {
                destination.reactToSound(entity == null ? null : world.getEntityFromUuid(entity), sound, x, y, z);
                isActive = false;
                return;
            }
            Vec3d dir = DirectionUtils.getDirectionVec(new Vec3d(x, y, z), destinationPos);
            x += dir.x;
            y += dir.y;
            z += dir.z;
        }

        public void spawnParticle(World world) {
            Vec3d pos = destination.getListenerPos();
            DeeperDepths.proxy.spawnParticle(9, world, x, y, z, pos.x, pos.y, pos.z, (int) pos.distanceTo(new Vec3d(x, y, z)));
        }

        public NBTTagCompound toNbt() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setDouble("x", x);
            nbt.setDouble("y", y);
            nbt.setDouble("z", z);
            if (entity != null) nbt.setTag("entity", NBTUtil.createUUIDTag(entity));
            nbt.setString("sound", sound.toString());
            return nbt;
        }

        public static Vibration fromNBT(Listener listener, NBTTagCompound nbt) {
            return new Vibration(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"), listener, nbt.hasKey("entity", 10) ?
                    NBTUtil.getUUIDFromTag(nbt.getCompoundTag("entity")) : null, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(nbt.getString("sound"))));
        }

    }

}
