package com.deeperdepths.common.capabilities;

import com.deeperdepths.common.blocks.tiles.TileSculkShrieker;
import com.deeperdepths.common.entities.EntityWarden;
import com.deeperdepths.config.BlockConfig;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.smileycorp.atlas.api.util.DirectionUtils;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface WardenSpawnTracker {

    @CapabilityInject(WardenSpawnTracker.class)
    Capability<WardenSpawnTracker> CAPABILITY = null;

    void tick();

    void reset();

    void increaseWarningLevel();

    int getWarningLevel();

    boolean isOnCooldown();

    NBTTagCompound writeToNbt();

    void readFromNBT(NBTTagCompound nbt);

    class Impl implements WardenSpawnTracker {

        private int warningLevel, cooldown, lastWarning;

        @Override
        public void tick() {
            if (warningLevel > 0 && lastWarning++ >= BlockConfig.sculkShriekerDecayRate) {
                warningLevel--;
                lastWarning = 0;
            }
            if (cooldown > 0) cooldown--;
        }

        @Override
        public void reset() {
            warningLevel = 0;
            cooldown = BlockConfig.sculkShriekerCooldown;
            lastWarning = 0;
        }

        @Override
        public void increaseWarningLevel() {
            if (warningLevel < 4) warningLevel++;
            cooldown = BlockConfig.sculkShriekerCooldown;
            lastWarning = 0;
        }

        @Override
        public int getWarningLevel() {
            return warningLevel;
        }

        @Override
        public boolean isOnCooldown() {
            return cooldown != 0;
        }

        @Override
        public NBTTagCompound writeToNbt() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("warningLevel", warningLevel);
            nbt.setInteger("cooldown", cooldown);
            nbt.setInteger("lastWarning", lastWarning);
            return nbt;
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt) {
            warningLevel = nbt.getInteger("warningLevel");
            cooldown = nbt.getInteger("cooldown");
            lastWarning = nbt.getInteger("lastWarning");
        }

    }

    class Storage implements Capability.IStorage<WardenSpawnTracker> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<WardenSpawnTracker> capability, WardenSpawnTracker instance, EnumFacing facing) {
            return instance.writeToNbt();
        }

        @Override
        public void readNBT(Capability<WardenSpawnTracker> capability, WardenSpawnTracker instance, EnumFacing facing, NBTBase nbt) {
            if (!(nbt instanceof NBTTagCompound)) return;
            instance.readFromNBT((NBTTagCompound) nbt);
        }

    }

    class Provider implements ICapabilitySerializable<NBTTagCompound> {

        protected final WardenSpawnTracker instance = new Impl();

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == CAPABILITY;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == CAPABILITY ? CAPABILITY.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) CAPABILITY.getStorage().writeNBT(CAPABILITY, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            CAPABILITY.getStorage().readNBT(CAPABILITY, instance, null, nbt);
        }

    }

    static WardenSpawnTracker get(EntityPlayer player) {
        if (player == null) return null;
        return player.hasCapability(CAPABILITY, null) ? player.getCapability(CAPABILITY, null) : null;
    }

    static int tryWarn(World world, BlockPos pos, EntityPlayer player) {
        Vec3d vec = DirectionUtils.centerOf(pos);
        if (!world.getEntitiesWithinAABB(EntityWarden.class,
                new AxisAlignedBB(vec.addVector(24, 24, 24), vec.subtract(24, 24, 24))).isEmpty())  return 0;
        List<EntityPlayer> players = world.getPlayers(EntityPlayer.class, player1 -> player1 == player ||
                (vec.squareDistanceTo(player1.posX, player1.posY, player1.posZ) <= 256 && player1.isEntityAlive() &! player1.isSpectator()));
        int warningLevel = 0;
        for (WardenSpawnTracker tracker : players.stream().map(WardenSpawnTracker::get).filter(Predicates.not(WardenSpawnTracker::isOnCooldown)).collect(Collectors.toList())) {
            tracker.increaseWarningLevel();
            if (warningLevel < tracker.getWarningLevel()) warningLevel = tracker.getWarningLevel();
        }
        return warningLevel;
    }

    static boolean isOnCooldown(@Nullable Entity entity) {
        return !(entity instanceof EntityPlayer) || !entity.hasCapability(CAPABILITY, null)
                || entity.getCapability(CAPABILITY, null).isOnCooldown();
    }


}
