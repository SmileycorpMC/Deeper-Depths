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
        return MathHelper.positiveModulo((float) wobble(world, pos == null ? MathHelper.positiveModulo((float) Math.random(), 1) :
            0.5 - (MathHelper.positiveModulo(entity.rotationYaw / 360d, 1) - 0.25
                    - Math.atan2(pos.getZ() - entity.posZ, pos.getX() - entity.posX) / (Math.PI * 2d))), 1);
    }

    @SideOnly(Side.CLIENT)
    private BlockPos getPos(World world, Entity entity) {
        if (!(entity instanceof EntityPlayer) | !entity.hasCapability(DeathLocation.CAPABILITY, null)) return null;
        Tuple<Integer, BlockPos> data = entity.getCapability(DeathLocation.CAPABILITY, null).getDeathInformation();
        if (data == null) return null;
        return data.getFirst() == world.provider.getDimension() ? data.getSecond() : null;
    }

    @SideOnly(Side.CLIENT)
    private double wobble(World world, double angle) {
        if (world.getTotalWorldTime() == lastUpdateTick) return prevAngle;
        lastUpdateTick = world.getTotalWorldTime();
        change = 0.8 * (change + MathHelper.positiveModulo(angle - prevAngle + 0.5, 1.0) - 0.5 * 0.1);
        prevAngle = MathHelper.positiveModulo(prevAngle + change, 1.0);
        return prevAngle;
    }

}
