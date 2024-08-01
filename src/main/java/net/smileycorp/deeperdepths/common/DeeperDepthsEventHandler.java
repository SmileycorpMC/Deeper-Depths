package net.smileycorp.deeperdepths.common;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.deeperdepths.common.blocks.ICopperBlock;
import net.smileycorp.deeperdepths.common.potion.DeeperDepthsPotions;

public class DeeperDepthsEventHandler {
    
    @SubscribeEvent
    public void potionAdded(PotionEvent.PotionAddedEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity.world.isRemote) return;
        if (event.getOldPotionEffect() != null) return;
        Potion effect = event.getPotionEffect().getPotion();
        SoundEvent sound = effect == DeeperDepthsPotions.BAD_OMEN ? DeeperDepthsSoundEvents.MOB_EFFECT_BAD_OMEN:
                effect == DeeperDepthsPotions.TRIAL_OMEN ? DeeperDepthsSoundEvents.MOB_EFFECT_TRIAL_OMEN : null;
        if (sound != null) entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, sound, entity.getSoundCategory(), 1.0F, 1.0F);
    }
    
    @SubscribeEvent
    public void blockInteract(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        if (world == null) return;
        if (world.isRemote) return;
        ItemStack stack = event.getItemStack();
        IBlockState state = world.getBlockState(pos);
        if (!(stack.getItem() instanceof ItemAxe)) return;
        if (!(state.getBlock() instanceof ICopperBlock)) return;
        ICopperBlock copper = (ICopperBlock) state.getBlock();
        IBlockState scraped = copper.getScraped(state);
        if (state.equals(scraped)) return;
        world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                copper.isWaxed(state) ? DeeperDepthsSoundEvents.COPPER_WAX_OFF : DeeperDepthsSoundEvents.COPPER_SCRAPE,
                SoundCategory.BLOCKS, 1, 1);
        world.setBlockState(pos, scraped, 3);
        EntityLivingBase entity = event.getEntityLiving();
        if (!(entity instanceof EntityPlayer && ((EntityPlayer)entity).isCreative()))
            stack.damageItem(1, entity);
    }
    
}
