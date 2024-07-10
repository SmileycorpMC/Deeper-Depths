package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.EnumWeatherStage;

public class BlockCopper extends Block {
    
    public static final PropertyEnum<EnumWeatherStage> WEATHER_STAGE = PropertyEnum.create("weather_stage", EnumWeatherStage.class);
    public static final PropertyBool WAXED = PropertyBool.create("waxed");
    
    public BlockCopper(String name) {
        super(Material.IRON);
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setHarvestLevel("PICKAXE", 1);
        setHardness(3);
        setResistance(6);
        setDefaultState(getBlockState().getBaseState().withProperty(WEATHER_STAGE, EnumWeatherStage.NORMAL).withProperty(WAXED, false));
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
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(WEATHER_STAGE, EnumWeatherStage.values()[meta % 4]).withProperty(WAXED, meta >= 4);
    }
    
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < 8; i++) items.add(new ItemStack(this, 1, i));
    }
    
}
