package net.smileycorp.deeperdepths.common.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.deeperdepths.common.potion.DeeperDepthsPotions;
import net.smileycorp.deeperdepths.common.world.DeeperDepthsSoundEvents;

import java.util.List;

public class ItemOminousBottle extends ItemDeeperDepths {
    
    public ItemOminousBottle() {
        super("ominous_bottle");
        setHasSubtypes(true);
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }
    
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
        if (!world.isRemote) {
            world.playSound(null, entity.getPosition(), DeeperDepthsSoundEvents.OMINOUS_BOTTLE_USE, entity.getSoundCategory(), 1, 1);
            //entity.removePotionEffect(DeeperDepthsPotions.BAD_OMEN);
            //entity.addPotionEffect(new PotionEffect(DeeperDepthsPotions.BAD_OMEN, 120000, getAmplifier(stack), false, false));
            if (entity instanceof EntityPlayerMP) CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP) entity, stack);
            if (!(entity instanceof EntityPlayer && ((EntityPlayer) entity).func_184812_l_())) stack.shrink(1);
        }
        return stack;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        StringBuilder builder = new StringBuilder(I18n.translateToLocal("effect.deeperdepths.bad_omen").trim());
        int amplifier = getAmplifier(stack);
        if (amplifier > 0) builder.append(" " + I18n.translateToLocal("potion.potency." + amplifier).trim());
        //builder.append(" (" + Potion.getPotionDurationString(new PotionEffect(DeeperDepthsPotions.BAD_OMEN, 120000), 1f) + ")");
        tooltip.add(TextFormatting.BLUE + builder.toString());
    }
    
    @Override
    public void getSubItems(CreativeTabs tabs, NonNullList<ItemStack> itemList) {
        if(!isInCreativeTab(tabs)) return;
        for (int i = 0; i < 5; i++) itemList.add(createStack(i));
    }
    
    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
    
    public static ItemStack createStack(int amplifier) {
        return new ItemStack(DeeperDepthsItems.OMINOUS_BOTTLE, 1, amplifier);
    }
    
    public static int getAmplifier(ItemStack stack) {
        return stack.getMetadata();
    }
    
}
