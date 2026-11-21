package com.deeperdepths.common.blocks.tiles;

import com.deeperdepths.common.blocks.BlockCalibratedSculkSensor;
import com.deeperdepths.config.VibrationsConfig;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;

public class TileCalibratedSculkSensor extends TileSculkSensor {

    public TileCalibratedSculkSensor() {
        super();
    }

    public TileCalibratedSculkSensor(int cooldown) {
        super(cooldown);
    }

    @Override
    public boolean canHear(Entity entity, SoundEvent sound, double x, double y, double z) {
        if (!super.canHear(entity, sound, x, y, z)) return false;
        IBlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof BlockCalibratedSculkSensor)) return true;
        EnumFacing facing = state.getValue(BlockHorizontal.FACING).getOpposite();
        int power = world.getRedstonePower(pos.offset(facing), facing);
        return power == 0 || power > 0 && VibrationsConfig.getVibrationFrequency(sound) == power;
    }

}
