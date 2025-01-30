package com.deeperdepths.common.items;

import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.BlockSculkVein;
import com.deeperdepths.common.blocks.DeeperDepthsBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

public class ItemSculkVein extends ItemDeeperDepths {
    
    public ItemSculkVein() {
        super("sculk_vein");
    }
    
    //I have looked into the deeper depths and sculk veins have stared back
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking() |! player.canPlayerEdit(pos, side, stack) || stack.getItem() != this)  return EnumActionResult.PASS;
        pos = pos.offset(side);
        IBlockState state = world.getBlockState(pos);
        EnumFacing facing = side.getOpposite();
        if (state.getBlock() instanceof BlockSculkVein) {
            if (BlockSculkVein.hasFacing(state, facing)) return EnumActionResult.PASS;
            state = BlockSculkVein.getBlockState(ArrayUtils.add(BlockSculkVein.getFacings(state), facing));
        } else if (!world.mayPlace(DeeperDepthsBlocks.SCULK_VEINS[0], pos, false, side, player)) return EnumActionResult.PASS;
        else state = BlockSculkVein.getBlockState(facing);
        if (!world.setBlockState(pos, state, 11)) return EnumActionResult.PASS;
        SoundType sound = DeeperDepthsSoundTypes.SCULK_VEIN;
        world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1) / 2f, sound.getPitch() * 0.8f);
        stack.shrink(1);
        if (player instanceof EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
        return EnumActionResult.SUCCESS;
    }
    
}
