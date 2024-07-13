package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCopper extends BlockDeeperDepths implements ICopperBlock {
    
    public static final PropertyBool WAXED = PropertyBool.create("waxed");
    
    public BlockCopper(String name) {
        super(name, Material.IRON, 3, 6, 1);
        setDefaultState(getBlockState().getBaseState().withProperty(WEATHER_STAGE, EnumWeatherStage.NORMAL).withProperty(WAXED, false));
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        ItemStack stack = placer.getHeldItem(hand);
        return getStateFromMeta(stack.getMetadata());
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, WEATHER_STAGE, WAXED);
    }
    
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(WEATHER_STAGE).getMapColor();
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(WEATHER_STAGE).ordinal() + (state.getValue(WAXED) ? 4 : 0);
    }
    
    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(WEATHER_STAGE, EnumWeatherStage.values()[meta % 4]).withProperty(WAXED, meta >= 4);
    }
    
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < 8; i++) items.add(new ItemStack(this, 1, i));
    }
    
    @Override
    public int getMaxMeta() {
        return 8;
    }
    
    @Override
    public String byMeta(int meta) {
        StringBuilder builder = new StringBuilder();
        if (meta % 8 >= 4) builder.append("waxed_");
        if (meta % 4 > 0) builder.append(EnumWeatherStage.values()[meta % 4].getName() + "_");
        return builder + (meta % 4 > 0 ? getRegistryName().getResourcePath().replace("_block", "") : getRegistryName().getResourcePath());
    }
    
    @Override
    public boolean isWaxed(IBlockState state) {
        return state.getValue(WAXED);
    }
    
    @Override
    public EnumWeatherStage getStage(IBlockState state) {
        return state.getValue(WEATHER_STAGE);
    }
    
    @Override
    public IBlockState getScraped(IBlockState state) {
        return state.getValue(WAXED) ? state.withProperty(WAXED, false) : state.withProperty(WEATHER_STAGE, state.getValue(WEATHER_STAGE).previous());
    }
    
    @Override
    public IBlockState getWaxed(IBlockState state) {
        return state.withProperty(WAXED, true);
    }
    
    @Override
    public IBlockState getWeathered(IBlockState state) {
        return state.withProperty(WEATHER_STAGE, state.getValue(WEATHER_STAGE).next());
    }
    
}
