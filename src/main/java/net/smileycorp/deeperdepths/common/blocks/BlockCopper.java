package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.EnumWeatherStage;

public class BlockCopper extends Block {
    
    public static final PropertyEnum<EnumWeatherStage> WEATHER_STAGE = PropertyEnum.create("weather_stage", EnumWeatherStage.class);
    public static final PropertyBool WAXED = PropertyBool.create("waxed");
    
    public BlockCopper() {
        super(Material.IRON);
        setRegistryName(Constants.loc("copper_block"));
        setUnlocalizedName(Constants.name("copper_block"));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, WEATHER_STAGE, WAXED);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        
    }
    
}
