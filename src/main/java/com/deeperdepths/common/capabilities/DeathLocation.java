package com.deeperdepths.common.capabilities;

import com.deeperdepths.common.network.SyncDeathLocationMessage;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public interface DeathLocation {

    @CapabilityInject(DeathLocation.class)
    Capability<DeathLocation> CAPABILITY = null;

    @Nullable
    Tuple<Integer, BlockPos> getDeathInformation();

    void setDeathInformation(int dimension, BlockPos pos);

    class Impl implements DeathLocation {

        private int dimension;
        private BlockPos pos;

        @Override
        public Tuple<Integer, BlockPos> getDeathInformation() {
            return pos == null ? null : new Tuple<>(dimension, pos);
        }

        @Override
        public void setDeathInformation(int dimension, BlockPos pos) {
            this.dimension = dimension;
            this.pos = pos;
            System.out.println(dimension + ", " + pos);
        }

    }

    class Storage implements Capability.IStorage<DeathLocation> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<DeathLocation> capability, DeathLocation instance, EnumFacing facing) {
            Tuple<Integer, BlockPos> info = instance.getDeathInformation();
            if (info == null) return new NBTTagCompound();
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("dimension", info.getFirst());
            nbt.setTag("pos", NBTUtil.createPosTag(info.getSecond()));
            return nbt;
        }

        @Override
        public void readNBT(Capability<DeathLocation> capability, DeathLocation instance, EnumFacing facing, NBTBase nbt) {
            if (!(nbt instanceof NBTTagCompound)) return;
            NBTTagCompound compound = (NBTTagCompound) nbt;
            if (!compound.hasKey("pos")) return;
            instance.setDeathInformation(compound.getInteger("dimension"), NBTUtil.getPosFromTag(compound.getCompoundTag("pos")));
        }

    }

    class Provider implements ICapabilitySerializable<NBTTagCompound> {

        protected final DeathLocation instance = new Impl();

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

}
