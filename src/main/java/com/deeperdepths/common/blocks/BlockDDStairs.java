package com.deeperdepths.common.blocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.BlockStairsBase;

import javax.annotation.Nullable;

public class BlockDDStairs extends BlockStairsBase implements IBlockProperties {

    private final IBlockState base;

    public BlockDDStairs(String name, IBlockState state) {
        super(name, state);
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
        setSoundType(state.getBlock().getSoundType(state, null, null, null));
        setUnlocalizedName(Constants.name(name + "_stairs"));
        base = state;
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return base.getValue(BlockDDStone.VARIANT).getResistance() / 5f;
    }
    
}
