package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.smileycorp.atlas.api.block.IBlockProperties;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.DeeperDepths;

public class BlockDeeperDepths extends Block implements IBlockProperties {
    
    public BlockDeeperDepths(String name, Material material, float h, float r, int level) {
        super(material);
        setResistance(r);
        setHardness(h);
        setHarvestLevel("PICKAXE", level);
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
        setSoundType(SoundType.STONE);
    }
    
}
