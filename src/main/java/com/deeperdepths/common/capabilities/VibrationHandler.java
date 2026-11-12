package com.deeperdepths.common.capabilities;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.ISoundFrequency;
import com.deeperdepths.config.SensorSoundHandler;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.smileycorp.atlas.api.util.DirectionUtils;

import java.util.HashSet;
import java.util.Map;

public class VibrationHandler {
    
    private static final Map<Integer, VibrationHandler> INSTANCES = Maps.newHashMap();
    private static final float[] RESONANCE_PITCHES = {0.5f, 0.5f, 0.561231024154687f, 0.629960524947437f, 0.707106781186548f,
            0.749153538438341f, 0.840896415253715f, 0.890898718140339f, 1, 1.12246204830937f, 1.18920711500272f, 1.4142135623731f,
            1.68179283050743f, 1.78179743628068f, 2};

    private final WorldServer world;
    private final HashSet<Listener> listeners = Sets.newHashSet();
    
    public VibrationHandler(WorldServer world) {
        this.world = world;
    }

    public void add(Listener listener) {
        listeners.add(listener);
    }
    
    public void remove(Listener listener) {
        listeners.remove(listener);
    }
    
    public void playSound(SoundEvent sound, double x, double y, double z) {
        if (SensorSoundHandler.getSoundFrequency(sound) == 0) return;
        for (Listener listener : listeners) {
            Vec3d pos = listener.getPosition();
            if (pos.squareDistanceTo(x, y, z) <= Math.pow(listener.getRange(), 2) &! isOccluded(x, y, z, pos)
                    && listener.canHear(sound, x, y, z)) listener.reactToSound(sound, x, y, z);
        }
        if (sound == DeeperDepthsSoundEvents.AMETHYST_BLOCK_RESONATE) ((ISoundFrequency)sound).setSoundFrequency(0);
    }

    public boolean isOccluded(double soundX, double soundY, double soundZ, Vec3d pos) {
        return DirectionUtils.hasCollision(world, new Vec3d(soundX, soundY, soundZ), pos, (world, pos1, state, block) -> block == Blocks.WOOL);
    }

    public void playResonance(BlockPos pos, int frequency) {
            SoundEvent sound = DeeperDepthsSoundEvents.AMETHYST_BLOCK_RESONATE;
            ((ISoundFrequency)sound).setSoundFrequency(frequency);
            world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                    sound, SoundCategory.BLOCKS, 1, RESONANCE_PITCHES[frequency]);
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
    
    public static class Vibration {
    
    }
    
    public interface Listener {

        Vec3d getPosition();

        int getRange();
        
        boolean canHear(SoundEvent sound, double x, double y, double z);
        
        void reactToSound(SoundEvent sound, double x, double y, double z);
        
    }
}
