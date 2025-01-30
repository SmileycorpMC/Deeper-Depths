package com.deeperdepths.common.items;

import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.BlockSculkVein;
import com.deeperdepths.common.blocks.tiles.TileSculkVein;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockSculkVein extends ItemDDBlock<BlockSculkVein> {
    
    public ItemBlockSculkVein(BlockSculkVein block) {
        super(block);
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        BlockPos pos1 = pos.offset(side);
        IBlockState state = world.getBlockState(pos1);
        if (!player.isSneaking() && state.getBlock() == block) {
            TileEntity te = world.getTileEntity(pos1);
            EnumFacing facing = side.getOpposite();
            if (te instanceof TileSculkVein) if (!((TileSculkVein) te).hasFacing(facing)) {
                ((TileSculkVein) te).setFacing(facing, true);
                ItemStack stack = player.getHeldItem(hand);
                SoundType soundtype = DeeperDepthsSoundTypes.SCULK_VEIN;
                world.playSound(player, pos1, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1) / 2f,
                        soundtype.getPitch() * 0.8f);
                stack.shrink(1);
                if (player instanceof EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos1, stack);
                return EnumActionResult.SUCCESS;
            }
        }
        return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
    }
    
    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (!super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)) return false;
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileSculkVein) ((TileSculkVein) te).setFacing(side.getOpposite(), true);
        return true;
    }
    
    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
        if (world.getBlockState(pos.offset(side)).getBlock() == block) return true;
        return super.canPlaceBlockOnSide(world, pos, side, player, stack);
    }
    
}
