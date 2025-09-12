package com.deeperdepths.common.items;

import com.deeperdepths.common.blocks.BlockCopperChest;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import com.deeperdepths.common.blocks.ICopperBlock;
import com.deeperdepths.common.blocks.tiles.TileCopperChest;
import com.deeperdepths.config.BlockConfig;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCopperChest extends ItemBlockCopper<BlockCopperChest> {

    public ItemCopperChest(BlockCopperChest block) {
        super(block);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState state) {
        if (!super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, state)) return false;
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof TileCopperChest)) return true;
        TileCopperChest chest = (TileCopperChest) tile;
        if (stack.hasDisplayName()) chest.setCustomName(stack.getDisplayName());
        if (chest.canConnect(pos.offset(side.getOpposite()))) {
            EnumFacing direction = side.getOpposite();
            BlockPos other = pos.offset(direction);
            IBlockState otherState = world.getBlockState(other);
            if (otherState.getValue(BlockHorizontal.FACING) != side.getOpposite()) {
                ((TileCopperChest) tile).setNeighbor(direction);
                ((TileCopperChest) world.getTileEntity(other)).setNeighbor(side);
                placeDoubleChest(world, state.withProperty(BlockHorizontal.FACING, otherState.getValue(BlockHorizontal.FACING)), pos, direction);
                return true;
            }
        }
        if (!player.isSneaking()) {
            chest.checkForAdjacentChests();
            EnumFacing direction = chest.getOtherDirection();
            if (direction != null) {
                placeDoubleChest(world, state, pos, direction);
                return true;
            }
        }
        world.setBlockState(pos, state, 3);
        return true;
    }

    private void placeDoubleChest(World world, IBlockState state, BlockPos pos, EnumFacing direction) {
        BlockPos other = pos.offset(direction);
        IBlockState otherState = world.getBlockState(other);
        if (!BlockConfig.sameTypeChests)
            state = (!((BlockCopperChest) state.getBlock()).isWaxed() || ((BlockCopperChest) otherState.getBlock()).isWaxed() ?
                    DeeperDepthsBlocks.COPPER_CHEST : DeeperDepthsBlocks.WAXED_COPPER_CHEST).getDefaultState()
                    .withProperty(BlockHorizontal.FACING, state.getValue(BlockHorizontal.FACING))
                    .withProperty(ICopperBlock.WEATHER_STAGE, state.getValue(ICopperBlock.WEATHER_STAGE)
                            .getLowest(otherState.getValue(ICopperBlock.WEATHER_STAGE)));
        world.setBlockState(pos, state, 3);
        world.setBlockState(other, state, 3);
    }

}
