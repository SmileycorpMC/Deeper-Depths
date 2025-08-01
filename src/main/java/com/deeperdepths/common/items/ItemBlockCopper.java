package com.deeperdepths.common.items;

import com.deeperdepths.common.blocks.IBlockProperties;
import com.deeperdepths.common.blocks.ICopperBlock;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemBlockCopper<T extends Block & IBlockProperties & ICopperBlock> extends ItemDDBlock<T> implements ICopperItem {
    
    public ItemBlockCopper(T block) {
        super(block);
    }
    
    public EnumAction getItemUseAction(ItemStack stack) {
        return getBlock().isEdible(stack) ? EnumAction.EAT : super.getItemUseAction(stack);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (getBlock().isEdible(stack)) {
            player.setActiveHand(hand);
            return new ActionResult(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(world, player, hand);
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return getBlock().isEdible(stack) ? 32 : 0;
    }
    
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
        if (!getBlock().isEdible(stack)) return stack;
        if (entity instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer)entity;
            world.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ,
                    SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5f, world.rand.nextFloat() * 0.1f + 0.9f);
            entityplayer.addStat(StatList.getObjectUseStats(this));
            if (entityplayer instanceof EntityPlayerMP) CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
        }
        return getBlock().getScraped(stack);
    }
    
    @Override
    public T getBlock() {
        return (T) block;
    }
    
}
