package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
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
import net.smileycorp.deeperdepths.common.blocks.enums.EnumStoneType;

import javax.annotation.Nullable;

public class BlockDDStoneSlab extends BlockDDSlab {
    
    public static final PropertyEnum<EnumStoneType> VARIANT = PropertyEnum.create("variant", EnumStoneType.class, EnumStoneType.SHAPED_TYPES);
    
    public BlockDDStoneSlab(boolean isDouble) {
        super(Material.ROCK, "Stone_Slab", isDouble);
        setHarvestLevel("PICKAXE", 0);
        IBlockState base = getBlockState().getBaseState().withProperty(VARIANT, EnumStoneType.TUFF);
        if (!isDouble) base = base.withProperty(HALF, EnumBlockHalf.TOP);
        setDefaultState(base);
    }
    
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(VARIANT).getMapColor();
    }
    
    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        return state.getValue(VARIANT).getSoundType();
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
    public String getUnlocalizedName(int meta) {
        return "tile." + Constants.MODID + "." + EnumStoneType.getShaped(meta).getName() + "_slab";
    }
    
    @Override
    public IProperty<?> getVariantProperty() {
        return VARIANT;
    }
    
    @Override
    public Comparable<?> getTypeForItem(ItemStack stack) {
        return EnumStoneType.getShaped(stack.getMetadata());
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return (isDouble() ? getDefaultState() : getDefaultState().withProperty(HALF, meta == 8 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM))
                .withProperty(VARIANT, EnumStoneType.getShaped(meta));
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return (isDouble() ? 0 : (state.getValue(HALF) == EnumBlockHalf.TOP ? 8 : 0)) + state.getValue(VARIANT).getShapedMeta();
    }
    
    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(DeeperDepthsBlocks.STONE_SLAB, 1, getMetaFromState(state) % 8);
    }
    
    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state) % 8;
    }
    
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < VARIANT.getAllowedValues().size(); i++) items.add(new ItemStack(this, 1, i));
    }
    
    @Override
    public int getMaxMeta() {
        return 8;
    }
    
    @Override
    public String byMeta(int meta) {
        return EnumStoneType.getShaped(meta % 8).getName() + "_slab";
    }
    
    @Override
    public BlockDDSlab getDouble() {
        return DeeperDepthsBlocks.DOUBLE_STONE_SLAB;
    }
    
}
