package com.deeperdepths.common.blocks.tiles;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.blocks.BlockSculkSensor;
import com.deeperdepths.common.blocks.IFluidloggable;
import com.deeperdepths.common.capabilities.VibrationHandler;
import com.deeperdepths.config.BlockConfig;
import com.deeperdepths.config.VibrationsConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.smileycorp.atlas.api.util.DirectionUtils;

public class TileSculkSensor extends TileEntity implements ITickable, VibrationHandler.Listener {
    
    private int cooldown, frequency, power;

    public TileSculkSensor() {
        super();
    }

    public TileSculkSensor(int cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        if (world instanceof WorldServer) VibrationHandler.getInstance((WorldServer) world).add(this);
    }
    
    @Override
    public void invalidate() {
        super.invalidate();
        if (world instanceof WorldServer) VibrationHandler.getInstance((WorldServer) world).remove(this);
    }

    @Override
    public Vec3d getPosition() {
        return DirectionUtils.centerOf(pos);
    }

    @Override
    public int getRange() {
        return VibrationsConfig.sculkSensorDetectionRange;
    }

    @Override
    public boolean canHear(Entity entity, SoundEvent sound, double x, double y, double z) {
        return VibrationsConfig.getVibrationFrequency(sound) != 0 && cooldown == 0;
    }

    @Override
    public void reactToSound(Entity entity, SoundEvent sound, double x, double y, double z) {
        if (cooldown > 0) return;
        cooldown = BlockConfig.sculkSensorCooldown + BlockConfig.sculkSensorActiveTicks;
        frequency = VibrationsConfig.getVibrationFrequency(sound);
        power = Math.max(1, 15 - (int) (15f / (float) getRange() * DirectionUtils.centerOf(pos).distanceTo(new Vec3d(x, y, z))));
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos pos1 = pos.offset(facing);
            if (VibrationsConfig.resonatesVibrations(world.getBlockState(pos1))) VibrationHandler.getInstance((WorldServer) world).playResonance(pos1, frequency);
        }
        IBlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof BlockSculkSensor)) return;
        if (!state.getValue(IFluidloggable.WATERLOGGED)) {
            VibrationHandler.getInstance((WorldServer) world).setEntityContext(entity);
            world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                    DeeperDepthsSoundEvents.SCULK_SENSOR_CLICKING, SoundCategory.BLOCKS, 1, 1);
        }
        world.setBlockState(pos, state.withProperty(BlockSculkSensor.ACTIVE, true));
        world.markBlockRangeForRenderUpdate(pos, pos);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void update() {
        if (cooldown == 0) return;
        cooldown--;
        if (cooldown == BlockConfig.sculkSensorActiveTicks) {
            frequency = 0;
            power = 0;
            IBlockState state = world.getBlockState(pos);
            if (!(state.getBlock() instanceof BlockSculkSensor)) return;
            if (!state.getValue(IFluidloggable.WATERLOGGED)) world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                        DeeperDepthsSoundEvents.SCULK_SENSOR_CLICKING_STOP, SoundCategory.BLOCKS, 1, 1);
            world.setBlockState(pos, state.withProperty(BlockSculkSensor.ACTIVE, false));
            world.markBlockRangeForRenderUpdate(pos, pos);
        }
    }

    public int getPower() {
        return power;
    }

    public int getFrequency() {
        return frequency;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("cooldown", cooldown);
        compound.setInteger("frequency", frequency);
        compound.setInteger("power", power);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        cooldown = compound.getInteger("cooldown");
        frequency = compound.getInteger("frequency");
        power = compound.getInteger("power");
    }

}
