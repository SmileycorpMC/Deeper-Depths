package net.smileycorp.deeperdepths.common;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.deeperdepths.common.blocks.ICopperBlock;
import net.smileycorp.deeperdepths.common.potion.DeeperDepthsPotions;

import java.util.Random;

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
        ItemStack stack = event.getItemStack();
        IBlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof ICopperBlock)) return;
        ICopperBlock copper = (ICopperBlock) state.getBlock();
        EntityLivingBase entity = event.getEntityLiving();
        if (copper.interactRequiresSneak() &! entity.isSneaking()) return;
        if (stack.getItem() instanceof ItemAxe) copper.scrape(entity, world, stack, state, pos, event.getHand());
        if (stack.getItem() == Items.SLIME_BALL) copper.wax(entity, world, stack, state, pos, event.getHand());
    }
    
    @SubscribeEvent
    public void entityJoinWorld(EntityJoinWorldEvent event) {
        World world = event.getWorld();
        if (!(event.getEntity() instanceof EntityLightningBolt) || world.isRemote) return;
        Random rand = world.rand;
        BlockPos base = event.getEntity().getPosition().add(-1, -1, -1);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(base);
        //copper stripping
        for (int i = 0; i < 10; i++) {
            pos.setPos(base.add(rand.nextInt(3), rand.nextInt(3), rand.nextInt(3)));
            IBlockState state = world.getBlockState(pos);
            if (!(state.getBlock() instanceof ICopperBlock)) return;
            ICopperBlock copper = (ICopperBlock) state.getBlock();
            if (copper.isWaxed(state)) return;
            copper.scrape(world, state, pos);
        }
    }
    
}
