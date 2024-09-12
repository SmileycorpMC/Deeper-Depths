package com.deeperdepths.common.blocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.config.BlockConfig;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockDeepslate extends BlockRotatedPillar implements IBlockProperties {
    
    public BlockDeepslate() {
        super(Material.ROCK, MapColor.GRAY);
        setRegistryName(Constants.loc("deepslate"));
        setUnlocalizedName(Constants.name("Deepslate"));
        setHardness(BlockConfig.deepslate.getHardness());
        setResistance(BlockConfig.deepslate.getResistance());
        setHarvestLevel("PICKAXE", BlockConfig.deepslate.getHarvestLevel());
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
        setSoundType(DeeperDepthsSoundTypes.DEEPSLATE);
    }
    
}
