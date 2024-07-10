package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.DeeperDepths;

import javax.annotation.Nullable;

public class BlockDDStone extends Block {
    
    public static final PropertyEnum<EnumStoneType> VARIANT = PropertyEnum.create("variant", EnumStoneType.class);
    
    public BlockDDStone() {
        super(Material.ROCK);
        setRegistryName(Constants.loc("stone"));
        setUnlocalizedName(Constants.name("Stone"));
        setDefaultState(getBlockState().getBaseState().withProperty(VARIANT, EnumStoneType.TUFF));
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }
    
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(VARIANT).getMapColor();
    }
    
    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return state.getValue(VARIANT).getHardness();
    }
    
    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return world.getBlockState(pos).getValue(VARIANT).getResistance() / 5f;
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).ordinal();
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, EnumStoneType.get(meta));
    }
    
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < EnumStoneType.values().length; i++) items.add(new ItemStack(this, 1, i));
    }
    
}
