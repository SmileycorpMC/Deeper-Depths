package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.BlockWall;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
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
import net.smileycorp.deeperdepths.common.blocks.enums.EnumStoneType;

import javax.annotation.Nullable;

public class BlockDDStoneWall extends BlockWall implements IBlockProperties {
    
    public static final PropertyEnum<EnumStoneType> TYPE = PropertyEnum.create("type", EnumStoneType.class, EnumStoneType.SHAPED_TYPES);
    
    public BlockDDStoneWall() {
        super(DeeperDepthsBlocks.STONE);
        setDefaultState(blockState.getBaseState().withProperty(TYPE, EnumStoneType.TUFF));
        String name = "Stone_Wall";
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setHarvestLevel("PICKAXE", 0);
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
        useNeighborBrightness = true;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UP, NORTH, EAST, WEST, SOUTH, TYPE, VARIANT);
    }
    
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(TYPE).getMapColor();
    }
    
    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        return state.getValue(TYPE).getSoundType();
    }
    
    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return state.getValue(TYPE).getHardness();
    }
    
    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return world.getBlockState(pos).getValue(TYPE).getResistance() / 5f;
    }
    
    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(TYPE).getShapedMeta();
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).getShapedMeta();
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(TYPE, EnumStoneType.getShaped(meta));
    }
    
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < TYPE.getAllowedValues().size(); i++) items.add(new ItemStack(this, 1, i));
    }
    
    @Override
    public int getMaxMeta() {
        return TYPE.getAllowedValues().size();
    }
    
    @Override
    public String byMeta(int meta) {
        return EnumStoneType.getShaped(meta).getName() + "_wall";
    }
    
    @Override
    public boolean usesCustomItemHandler(){
        return true;
    }
    
    
}
