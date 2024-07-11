package net.smileycorp.deeperdepths.common.blocks;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.PropertyString;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.DeeperDepths;

import java.util.List;

public class BlockCopperSlab extends BlockSlab implements IBlockProperties {
    
    private static final List<String> values = getAllowedValues();
    public static final PropertyString VARIANT = new PropertyString("variant", values);
    
    private final boolean isDouble;
    
    public BlockCopperSlab(String name, boolean isDouble) {
        super(Material.IRON);
        this.isDouble = isDouble;
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setHarvestLevel("PICKAXE", 1);
        setHardness(3);
        setResistance(6);
        IBlockState base = blockState.getBaseState().withProperty(VARIANT, values.get(0));
        if (!isDouble) base = base.withProperty(HALF,EnumBlockHalf.BOTTOM);
        setDefaultState(base);
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return isDouble() ? new BlockStateContainer(this, VARIANT) : new BlockStateContainer(this, VARIANT, HALF);
    }
    
    @Override
    public String getUnlocalizedName(int meta) {
        StringBuilder builder = new StringBuilder();
        if (meta % 8 >= 4) builder.append("Waxed");
        if (meta % 4 > 0) builder.append(EnumWeatherStage.values()[meta % 4].getUnlocalizedName());
        return "tile." + Constants.name(builder + getUnlocalizedName().replace("tile." + Constants.MODID + ".", ""));
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
        return values.get(stack.getMetadata() % 8);
    }
    
    @Override
    public boolean isFullBlock(IBlockState state) {
        return isDouble();
    }
    
    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return isDouble() || (state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP && side == EnumFacing.UP) || (state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.BOTTOM && side == EnumFacing.DOWN);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return (isDouble ? getDefaultState() : getDefaultState().withProperty(HALF, meta == 8 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM))
                .withProperty(VARIANT, values.get(meta % 8));
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return (isDouble ? 0 : (state.getValue(HALF) == EnumBlockHalf.TOP ? 8 : 0)) + getMeta(state.getValue(VARIANT));
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
        for (int i = 0; i < 8; i++) items.add(new ItemStack(this, 1, i));
    }
    
    private static List<String> getAllowedValues() {
        List<String> allowedValues = Lists.newArrayList();
        for (int i = 0; i < 8; i++) {
            StringBuilder builder = new StringBuilder();
            if (i % 8 >= 4) builder.append("waxed_");
            allowedValues.add(builder.append(EnumWeatherStage.values()[i % 4].getName()).toString());
        }
        return allowedValues;
    }
    
    private int getMeta(String value) {
        if (value == null) return 0;
        int meta = 0;
        if (value.contains("waxed_")) {
            meta = 4;
            value = value.replace("waxed_", "");
        }
        return meta + EnumWeatherStage.fromName(value).ordinal();
    }
    
    @Override
    public int getMaxMeta() {
        return 8;
    }
    
    @Override
    public String byMeta(int meta) {
        return values.get(meta % 8) + "_" + getRegistryName().getResourcePath();
    }
    
}
