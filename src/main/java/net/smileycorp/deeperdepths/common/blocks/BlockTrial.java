package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.deeperdepths.config.BlockConfig;

import java.util.Random;

public abstract class BlockTrial extends BlockDeeperDepths implements ITileEntityProvider {
    
    public static final PropertyBool OMINOUS = PropertyBool.create("ominous");
    
    public BlockTrial(String name) {
        super(name, Material.ROCK, BlockConfig.trialSpawner.getHardness(), BlockConfig.trialSpawner.getResistance(), BlockConfig.trialSpawner.getHarvestLevel());
        useNeighborBrightness = true;
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.AIR;
    }
    
    @Override
    public ItemStack getSilkTouchDrop(IBlockState state) {
        return ItemStack.EMPTY;
    }
    
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }
    
}
