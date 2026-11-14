package com.deeperdepths.common.blocks.tiles;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.blocks.BlockSculkSensor;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.common.blocks.IFluidloggable;
import com.deeperdepths.common.capabilities.VibrationHandler;
import com.deeperdepths.config.SensorSoundHandler;
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
        return 8;
    }

    @Override
    public boolean canHear(Entity entity, SoundEvent sound, double x, double y, double z) {
        return cooldown == 0;
    }

    @Override
    public void reactToSound(Entity entity, SoundEvent sound, double x, double y, double z) {
        cooldown = 40;
        frequency = SensorSoundHandler.getSoundFrequency(sound);
        power = Math.max(1, 15 - (int) (15f / 8f * DirectionUtils.centerOf(pos).distanceTo(new Vec3d(x, y, z))));
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos pos1 = pos.offset(facing);
            if (world.getBlockState(pos1).getBlock() == DeeperDepthsBlocks.AMETHYST_BLOCK)
                VibrationHandler.getInstance((WorldServer) world).playResonance(pos1, frequency);
        }
        IBlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof BlockSculkSensor)) return;
        if (!state.getValue(IFluidloggable.WATERLOGGED)) world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                DeeperDepthsSoundEvents.SCULK_SENSOR_CLICKING, SoundCategory.BLOCKS, 1, 1);
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
        if (cooldown == 10) {
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
