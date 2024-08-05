package net.smileycorp.deeperdepths.common;

import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.*;

import javax.annotation.Nullable;

public class CapabilityWindChargeFall
{
    @CapabilityInject(ICapabilityWindChargeFall.class)
    public static final Capability<ICapabilityWindChargeFall> WINDBURSTHEIGHT_CAP = null;
    public static final ResourceLocation ID = new ResourceLocation(Constants.MODID, "windChargeFallReduction");

    /** Detects if a Wind Charge was used, and calculation is necessary. Used because `burstYPos` cannot simply be set to null, and needs to support any Y-Pos. */
    private static final String WASSET_TAG = "wasSet";
    /** The max tick that the entity is expected to fall by. */
    private static final String SCHEDULED_TAG = "burstTimer";
    /** The Y-Position the Wind Burst went off at. */
    private static final String YPOS_TAG = "burstYPos";

    public interface ICapabilityWindChargeFall
    {
        boolean getUsedWindCharge();
        int getWindBurstTime();
        double getWindBurstHeight();

        void setUsedWindCharge(boolean value);
        void setWindBurstTime(int value);
        void setWindBurstHeight(double value);
    }

    public static class WindChargeHorn implements ICapabilityWindChargeFall
    {
        private boolean usedWindCharge = false;
        private int getWindTime = 0;
        private double getWindFall = 0.0;

        @Override
        public boolean getUsedWindCharge()
        { return usedWindCharge; }

        @Override
        public void setUsedWindCharge(boolean value)
        { usedWindCharge = value; }

        @Override
        public int getWindBurstTime()
        { return getWindTime; }

        @Override
        public void setWindBurstTime(int value)
        { getWindTime = value; }

        @Override
        public double getWindBurstHeight()
        { return getWindFall; }

        @Override
        public void setWindBurstHeight(double value)
        { getWindFall = value; }
    }

    static class Storage implements Capability.IStorage<ICapabilityWindChargeFall>
    {
        @Override
        public NBTBase writeNBT(Capability<ICapabilityWindChargeFall> capability, ICapabilityWindChargeFall instance, EnumFacing side)
        {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setBoolean(WASSET_TAG, instance.getUsedWindCharge());
            compound.setInteger(SCHEDULED_TAG, instance.getWindBurstTime());
            compound.setDouble(YPOS_TAG, instance.getWindBurstHeight());
            return compound;
        }

        @Override
        public void readNBT(Capability<ICapabilityWindChargeFall> capability, ICapabilityWindChargeFall instance, EnumFacing side, NBTBase nbt)
        {
            NBTTagCompound compound = (NBTTagCompound) nbt;
            instance.setUsedWindCharge(compound.getBoolean(WASSET_TAG));
            instance.setWindBurstTime(compound.getInteger(SCHEDULED_TAG));
            instance.setWindBurstHeight(compound.getDouble(YPOS_TAG));
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTBase>
    {
        final Capability<ICapabilityWindChargeFall> capability;
        final EnumFacing facing;
        final ICapabilityWindChargeFall instance;

        Provider(final ICapabilityWindChargeFall instance, final Capability<ICapabilityWindChargeFall> capability, @Nullable final EnumFacing facing)
        {
            this.instance = instance;
            this.capability = capability;
            this.facing = facing;
        }

        @Override
        public boolean hasCapability(@Nullable final Capability<?> capability, final EnumFacing facing)
        { return capability == getCapability(); }

        @Override
        public <T> T getCapability(@Nullable Capability<T> capability, EnumFacing facing)
        { return capability == getCapability() ? getCapability().cast(this.instance) : null; }

        final Capability<ICapabilityWindChargeFall> getCapability()
        { return capability; }

        EnumFacing getFacing()
        { return facing; }

        final ICapabilityWindChargeFall getInstance()
        { return instance; }

        @Override
        public NBTBase serializeNBT()
        { return getCapability().writeNBT(getInstance(), getFacing()); }

        @Override
        public void deserializeNBT(NBTBase nbt)
        { getCapability().readNBT(getInstance(), getFacing(), nbt); }
    }
}