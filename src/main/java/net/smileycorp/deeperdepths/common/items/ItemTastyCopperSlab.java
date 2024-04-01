package net.smileycorp.deeperdepths.common.items;

import net.minecraft.advancements.CriteriaTriggers;
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
import net.smileycorp.deeperdepths.common.blocks.BlockCutCopperSlab;

public class ItemTastyCopperSlab<T extends BlockCutCopperSlab> extends ItemDDSlab<T> {
    
    public ItemTastyCopperSlab(T block) {
        super(block);
    }
    
    public EnumAction getItemUseAction(ItemStack stack) {
        return isFunny(stack) ? EnumAction.EAT : super.getItemUseAction(stack);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (isFunny(stack)) {
            player.setActiveHand(hand);
            return new ActionResult(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(world, player, hand);
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return isFunny(stack) ? 32 : 0;
    }
    
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        if (!isFunny(stack)) return stack;
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ,
                    SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            entityplayer.addStat(StatList.getObjectUseStats(this));
            if (entityplayer instanceof EntityPlayerMP)
            {
                CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
            }
        }
        stack.setItemDamage(stack.getItemDamage() - 1);
        return stack;
    }
    
    private boolean isFunny(ItemStack stack) {
        return stack.getMetadata() % 4 > 0;
    }
    
}
