package com.deeperdepths.common.items;

import com.deeperdepths.common.capabilities.DeathLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRecoveryCompass extends ItemDeeperDepths {

    public ItemRecoveryCompass() {
        super("recovery_compass");
        addPropertyOverride(new ResourceLocation("angle"), this::getProperties);
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    @SideOnly(Side.CLIENT)
    double prevAngle;
    @SideOnly(Side.CLIENT)
    double change;
    @SideOnly(Side.CLIENT)
    long lastUpdateTick;

    public float getProperties(ItemStack stack, World world, EntityLivingBase living) {
        if (living == null && !stack.isOnItemFrame()) return 0;
        Entity entity = living == null ? stack.getItemFrame() : living;
        if (world == null) world = entity.world;
        if (world == null || entity == null) return 0;
        BlockPos pos = getPos(world, entity);
        return (float) wobble(world, pos == null ? Math.random() : MathHelper.wrapDegrees(90 + entity.rotationYaw -
                Math.toDegrees(Math.atan2(pos.getZ() + 0.5 - entity.posZ, pos.getX() + 0.5 - entity.posX))) / 360d);
    }

    @SideOnly(Side.CLIENT)
    private BlockPos getPos(World world, Entity entity) {
        if (!(entity instanceof EntityPlayer)) return null;
        Tuple<Integer, BlockPos> data = DeathLocation.getDeathInformation((EntityPlayer) entity);
        if (data == null) return null;
        return data.getFirst() == world.provider.getDimension() ? data.getSecond() : null;
    }

    @SideOnly(Side.CLIENT)
    private double wobble(World world, double angle) {
        if (world.getTotalWorldTime() == lastUpdateTick) return prevAngle;
        lastUpdateTick = world.getTotalWorldTime();
        change = 0.8 * (change + (MathHelper.positiveModulo(angle - prevAngle + 0.5, 1) - 0.5) * 0.1);
        prevAngle = MathHelper.positiveModulo(prevAngle + change, 1);
        return prevAngle;
    }

}
