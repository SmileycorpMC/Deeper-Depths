package com.deeperdepths.common.blocks.tiles;

import com.deeperdepths.common.blocks.BlockCopperChest;
import com.deeperdepths.common.blocks.ICopperBlock;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import com.deeperdepths.config.BlockConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileCopperChest extends TileEntityChest {

    private EnumWeatherStage stage = null;
    private boolean waxed = false;

    /*
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : getTranslationKey();
    }*/

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return false;
    }

    @Override
    protected TileCopperChest getAdjacentChest(EnumFacing side) {
        BlockPos blockpos = pos.offset(side);
        if (!isChestAt(blockpos)) return null;
        TileEntity tileentity = world.getTileEntity(blockpos);
        TileCopperChest tile = (TileCopperChest) tileentity;
        tile.setNeighbor(this, side.getOpposite());
        return tile;
    }

    private void setNeighbor(TileCopperChest te, EnumFacing side) {
        if (te.isInvalid()) adjacentChestChecked = false;
        if (!adjacentChestChecked) return;
        switch (side) {
            case NORTH:
                if (adjacentChestZNeg != te) adjacentChestChecked = false;
                break;
            case SOUTH:
                if (adjacentChestZPos != te) adjacentChestChecked = false;
                break;
            case EAST:
                if (adjacentChestXPos != te) adjacentChestChecked = false;
                break;
            case WEST:
                if (adjacentChestXNeg != te) adjacentChestChecked = false;
        }
    }

    private boolean isChestAt(BlockPos pos) {
        if (world == null) return false;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (!(block instanceof BlockCopperChest)) return false;
        if (!(world.getTileEntity(pos) instanceof TileCopperChest)) return false;
        if (BlockConfig.sameTypeChests) {
            if (state.getValue(ICopperBlock.WEATHER_STAGE) != getWeatherStage()) return false;
            if (((BlockCopperChest) block).isWaxed() != isWaxed()) return false;
        }
        return true;
    }

    public EnumWeatherStage getWeatherStage() {
        if (world == null || pos == null) return stage;
        IBlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof BlockCopperChest)) return null;
        return world.getBlockState(pos).getValue(ICopperBlock.WEATHER_STAGE);
    }

    public boolean isWaxed() {
        if (world == null || pos == null) return waxed;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (!(block instanceof BlockCopperChest)) return false;
        return ((BlockCopperChest) block).isWaxed();
    }

    public boolean isLarge() {
        return adjacentChestXPos != null || adjacentChestXNeg != null ||
                adjacentChestZPos != null || adjacentChestZNeg != null;
    }

    public void setupRenderProperties(EnumWeatherStage stage, boolean waxed) {
        this.stage = stage;
        this.waxed = waxed;
    }

    //maybe just copper chest vs large copper chest is better
    public String getTranslationKey() {
        StringBuilder builder = new StringBuilder("container.deeperdepths.");
        if (isLarge()) builder.append("large_");
        if (isWaxed()) builder.append("waxed_");
        EnumWeatherStage stage = getWeatherStage();
        if (stage != null && stage != EnumWeatherStage.NORMAL) builder.append(stage.getName() + "_");
        return builder.append("copper_chest").toString();
    }

}
