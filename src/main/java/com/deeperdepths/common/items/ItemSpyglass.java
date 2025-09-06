package com.deeperdepths.common.items;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.advancements.DeeperDepthsAdvancements;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.util.DirectionUtils;

import javax.annotation.Nullable;

public class ItemSpyglass extends ItemDeeperDepths {
    
    public ItemSpyglass()
    {
        super("spyglass");
        setMaxStackSize(1);

        this.addPropertyOverride(new ResourceLocation("model"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return entityIn != null && worldIn != null ? 1.0F : 0.0F;
            }
        });
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

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase entity, int tickCount) {
        if (!(entity instanceof EntityPlayerMP)) return;
        RayTraceResult result = DirectionUtils.rayTrace(entity.world, entity, 100);
        if (result.typeOfHit != RayTraceResult.Type.ENTITY) return;
        DeeperDepthsAdvancements.SPYGLASS.trigger((EntityPlayerMP) entity, result.entityHit);
    }

}
