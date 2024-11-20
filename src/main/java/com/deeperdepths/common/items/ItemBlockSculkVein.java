package com.deeperdepths.common.items;

import com.deeperdepths.common.blocks.BlockSculkVein;
import com.deeperdepths.common.blocks.tiles.TileSculkVein;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockSculkVein extends ItemDDBlock<BlockSculkVein> {
    
    public ItemBlockSculkVein(BlockSculkVein block) {
        super(block);
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        if (!player.isSneaking() && state.getBlock() == block) if (!state.getValue(BlockSculkVein.getProperty(facing)))
            return EnumActionResult.PASS;
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }
    
    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (!super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)) return false;
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileSculkVein) ((TileSculkVein) te).setFacing(side, true);
        return true;
    }
    
}
