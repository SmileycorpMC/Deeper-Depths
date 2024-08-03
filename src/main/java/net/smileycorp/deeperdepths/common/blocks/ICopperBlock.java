package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumWeatherStage;

public interface ICopperBlock {
    
    PropertyEnum<EnumWeatherStage> WEATHER_STAGE = PropertyEnum.create("weather_stage", EnumWeatherStage.class);
    
    boolean isWaxed(IBlockState state);
    
    EnumWeatherStage getStage(IBlockState state);
    
    IBlockState getScraped(IBlockState state);
    
    IBlockState getWaxed(IBlockState state);
    
    IBlockState getWeathered(IBlockState state);
    
    default boolean interactRequiresSneak() {
        return false;
    }
    
    default void scrape(EntityLivingBase entity, World world, ItemStack stack, IBlockState state, BlockPos pos, EnumHand hand) {
        if (!scrape(world, state, pos)) return;
        entity.swingArm(hand);
        if (world.isRemote) return;
        world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                isWaxed(state) ? DeeperDepthsSoundEvents.COPPER_WAX_OFF : DeeperDepthsSoundEvents.COPPER_SCRAPE,
                SoundCategory.BLOCKS, 1, 1);
        //needs particle spawning once we have a particle system
        entity.swingArm(hand);
        if (!(entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative()))
            stack.damageItem(1, entity);
    }
    
    default boolean scrape(World world, IBlockState state, BlockPos pos) {
        if (!isWaxed(state) && getStage(state) == EnumWeatherStage.NORMAL) return false;
        if (!world.isRemote) world.setBlockState(pos, getScraped(state), 3);
        return true;
    }
    
    default void wax(EntityLivingBase entity, World world, ItemStack stack, IBlockState state, BlockPos pos, EnumHand hand) {
        if (!wax(world, state, pos)) return;
        entity.swingArm(hand);
        if (world.isRemote) return;
        world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                DeeperDepthsSoundEvents.COPPER_WAX_ON, SoundCategory.BLOCKS, 1, 1);
        //needs particle spawning once we have a particle system
        if (!(entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative()))
            stack.shrink(1);
    }
    
    default boolean wax(World world, IBlockState state, BlockPos pos) {
        if (isWaxed(state)) return false;
        if (!world.isRemote) world.setBlockState(pos, getWaxed(state), 3);
        return true;
    }
    
    default boolean weather(World world, IBlockState state, BlockPos pos) {
        if (isWaxed(state)) return false;
        if (!world.isRemote) world.setBlockState(pos, getWeathered(state), 3);
        return true;
    }
    
}
