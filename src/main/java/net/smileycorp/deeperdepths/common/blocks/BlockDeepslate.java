package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.DeeperDepths;

public class BlockDeepslate extends BlockRotatedPillar implements IBlockProperties {
    
    public BlockDeepslate() {
        super(Material.ROCK, MapColor.GRAY);
        setRegistryName(Constants.loc("deepslate"));
        setUnlocalizedName(Constants.name("Deepslate"));
        setHarvestLevel("PICKAXE", 0);
        setHardness(3);
        setResistance(6);
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
    }
    
}
