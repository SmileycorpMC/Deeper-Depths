package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.DeeperDepths;

public class BlockDDStoneSlab extends BlockSlab implements IBlockProperties{
    
    public static final PropertyEnum<EnumStoneType> VARIANT = PropertyEnum.create("variant", EnumStoneType.class, EnumStoneType.SHAPED_TYPES);
    
    private final boolean isDouble;
    
    public BlockDDStoneSlab(boolean isDouble) {
        super(Material.IRON);
        this.isDouble = isDouble;
        String name = "Stone_Slab";
        if (isDouble) name = "Double_" + name;
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setHarvestLevel("PICKAXE", 0);
        IBlockState base = getBlockState().getBaseState().withProperty(VARIANT, EnumStoneType.TUFF);
        if (!isDouble) base = base.withProperty(HALF, EnumBlockHalf.TOP);
        setDefaultState(base);
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return this.isDouble() ? new BlockStateContainer(this, VARIANT) : new BlockStateContainer(this, HALF, VARIANT);
    }
    
    @Override
    public String getUnlocalizedName(int meta) {
        return "tile." + Constants.MODID + "." + EnumStoneType.getShaped(meta).getUnlocalizedName() + "Slab";
    }
    
    @Override
    public boolean isDouble() {
        return isDouble;
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
    public boolean isFullBlock(IBlockState state) {
        return isDouble();
    }
    
    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return isDouble() || (state.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP && side == EnumFacing.UP) || (state.getValue(BlockSlab.HALF) == EnumBlockHalf.BOTTOM && side == EnumFacing.DOWN);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return (isDouble ? getDefaultState() : getDefaultState().withProperty(HALF, meta == 8 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM))
                .withProperty(VARIANT, EnumStoneType.getShaped(meta));
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return (isDouble ? 0 : (state.getValue(HALF) == EnumBlockHalf.TOP ? 8 : 0)) + state.getValue(VARIANT).getShapedMeta();
    }
    
    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(this, 1, getMetaFromState(state) % 8);
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
        return EnumStoneType.getShaped(meta).getName();
    }
    
    
}
