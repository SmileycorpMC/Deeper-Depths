package com.deeperdepths.common.items;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemSpyglass extends ItemDeeperDepths {
    
    public ItemSpyglass() {
        super("spyglass");
        setMaxStackSize(1);
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 1200;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        ItemStack stack = player.getHeldItem(hand);
        player.playSound(DeeperDepthsSoundEvents.SPYGLASS_USE, 1, 1);
        if (player instanceof EntityPlayerMP) player.addStat(StatList.getObjectUseStats(stack.getItem()));
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }
    
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int tickCount) {
        entity.playSound(DeeperDepthsSoundEvents.SPYGLASS_STOP_USING, 1, 1);
    }
    
}
