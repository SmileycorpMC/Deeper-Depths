package com.deeperdepths.common.blocks;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.tiles.TileSculkSensor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSculkSensor extends BlockSculk implements ITileEntityProvider {
    
    public BlockSculkSensor(String name) {
        super(name, 1.5f, 1.5f);
        setSoundType(DeeperDepthsSoundTypes.SCULK_SENSOR);
    }
    
    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        super.onEntityWalk(world, pos, entity);
        if (world.isRemote) return;
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileSculkSensor) {
            TileSculkSensor sensor = (TileSculkSensor) tile;
            if (sensor.canHear(DeeperDepthsSoundEvents.SCULK_SENSOR_STEP, pos.getX(), pos.getY(), pos.getZ()))
                sensor.reactToSound(DeeperDepthsSoundEvents.SCULK_SENSOR_STEP, pos.getX(), pos.getY(), pos.getZ());
        }
    }
    
    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        return 5;
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileSculkSensor();
    }
    
}
