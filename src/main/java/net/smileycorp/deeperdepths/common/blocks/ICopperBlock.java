package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.common.DeeperDepths;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumWeatherStage;
import net.smileycorp.deeperdepths.config.BlockConfig;

import java.util.Random;

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

        if (isWaxed(state)) spawnSurfaceParticles(world, pos, 4, 244, 214, 212);
        else spawnSurfaceParticles(world, pos, 4, 107, 186, 130);
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
        spawnSurfaceParticles(world, pos, 4, 212, 166, 119);
        if (!(entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative()))
            stack.shrink(1);
    }
    
    default boolean wax(World world, IBlockState state, BlockPos pos) {
        if (isWaxed(state)) return false;
        if (!world.isRemote) world.setBlockState(pos, getWaxed(state), 3);
        return true;
    }
    
    default void tryWeather(World world, BlockPos pos, IBlockState state, Random rand) {
        EnumWeatherStage stage = getStage(state);
        int same = 0;
        int moreWeathered = 0;
        if (isWaxed(state) || getStage(state) == EnumWeatherStage.OXIDIZED) return;
        if (rand.nextFloat() > BlockConfig.copperAgeChance) return;
        BlockPos.MutableBlockPos pos1 = new BlockPos.MutableBlockPos(pos);
        for (int i = -4; i <= 4; i++) {
            for (int j = -4; j <= 4; j++) {
                for (int k = -4; k <= 4; k++) {
                    if (i + j + k > 4 || (i == 0 && j == 0 && k == 0)) continue;
                    pos1.setPos(pos.add(i, j, k));
                    IBlockState state1 = world.getBlockState(pos1);
                    if (!(state1.getBlock() instanceof ICopperBlock)) continue;
                    EnumWeatherStage other = ((ICopperBlock) state1.getBlock()).getStage(state1);
                    if (other.ordinal() < stage.ordinal()) return;
                    if (other.ordinal() > stage.ordinal()) moreWeathered++;
                    else same++;
                }
            }
        }
        float chance = (moreWeathered + 1f) / (moreWeathered + same + 1f);
        if (rand.nextFloat() >= chance * chance * stage.getAgeModifier()) return;
        weather(world, state, pos);
    }
    
    default boolean weather(World world, IBlockState state, BlockPos pos) {
        if (isWaxed(state)) return false;
        if (!world.isRemote) world.setBlockState(pos, getWeathered(state), 3);
        return true;
    }
    
    default boolean isEdible(ItemStack stack) {
        return stack.getMetadata() % 4 > 0;
    }
    
    default ItemStack getPrevious(ItemStack stack) {
        ItemStack stack1 = new ItemStack((Block) this, stack.getCount(), stack.getMetadata() - 1);
        if (stack.hasTagCompound()) stack1.setTagCompound(stack.getTagCompound());
        return stack1;
    }

    /** Spawns particles to slide on the surfaces of the Copper block. */
    default void spawnSurfaceParticles(World worldIn, BlockPos pos, int particleCount, int red, int green, int blue)
    {
        for (int i = 0; i < particleCount; i++)
        {
            for (EnumFacing facing : EnumFacing.values())
            {
                double x = pos.getX() + 0.5 + (facing.getAxis() == EnumFacing.Axis.X ? 0.55 * facing.getFrontOffsetX() : (worldIn.rand.nextDouble() - 0.5));
                double y = pos.getY() + 0.5 + (facing.getAxis() == EnumFacing.Axis.Y ? 0.55 * facing.getFrontOffsetY() : (worldIn.rand.nextDouble() - 0.5));
                double z = pos.getZ() + 0.5 + (facing.getAxis() == EnumFacing.Axis.Z ? 0.55 * facing.getFrontOffsetZ() : (worldIn.rand.nextDouble() - 0.5));

                double velocityX = (facing.getAxis() != EnumFacing.Axis.X) ? (worldIn.rand.nextDouble() - 0.5) * 0.1 : 0;
                double velocityY = (facing.getAxis() != EnumFacing.Axis.Y) ? (worldIn.rand.nextDouble() - 0.5) * 0.1 : 0;
                double velocityZ = (facing.getAxis() != EnumFacing.Axis.Z) ? (worldIn.rand.nextDouble() - 0.5) * 0.1 : 0;

                DeeperDepths.proxy.spawnParticle(4, worldIn, x, y, z, velocityX, velocityY, velocityZ, 30, red, green, blue);
            }
        }
    }
    
}
