package com.deeperdepths.common.blocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockDeeperDepths extends Block implements IBlockProperties {
    
    public BlockDeeperDepths(String name, Material material, float h, float r, int level) {
        super(material);
        setResistance(r);
        setHardness(h);
        setHarvestLevel("pickaxe", level);
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
        setSoundType(SoundType.STONE);
    }
    
}
