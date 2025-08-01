package com.deeperdepths.common.blocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class BlockDDSlab extends BlockSlab implements IBlockProperties {
    
    private final boolean isDouble;
    
    public BlockDDSlab(Material material, String name, boolean isDouble) {
        super(material);
        this.isDouble = isDouble;
        name = (isDouble ? "Double_" : "") + name;
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
        useNeighborBrightness = true;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return isDouble() ? new BlockStateContainer(this, getVariantProperty()) : new BlockStateContainer(this, getVariantProperty(), HALF);
    }
    
    @Override
    public boolean isDouble() {
        return isDouble;
    }
    
    @Override
    public boolean isFullBlock(IBlockState state) {
        return isDouble();
    }
    
    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return isDouble() || (state.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP && side == EnumFacing.UP) || (state.getValue(BlockSlab.HALF) == EnumBlockHalf.BOTTOM && side == EnumFacing.DOWN);
    }
    
    public abstract BlockDDSlab getDouble();
    
}
