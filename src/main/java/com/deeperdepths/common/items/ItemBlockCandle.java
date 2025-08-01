package com.deeperdepths.common.items;

import com.deeperdepths.common.blocks.BlockCandle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockCandle extends ItemDDBlock<BlockCandle> {
    
    public ItemBlockCandle(BlockCandle block) {
        super(block);
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        if (!player.isSneaking() && state.getBlock() == block) if (state.getValue(BlockCandle.CANDLES) < 4) return EnumActionResult.PASS;
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }
    
}
