package net.smileycorp.deeperdepths.common.blocks;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.smileycorp.atlas.api.block.PropertyString;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.EnumWeatherStage;

import java.util.List;

public class BlockCopperSlab extends BlockSlab {
    
    public static final PropertyString VARIANT = new PropertyString("variant", getAllowedValues());
    
    private final boolean isDouble;
    
    public BlockCopperSlab(String name, boolean isDouble) {
        super(Material.IRON);
        this.isDouble = isDouble;
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setHarvestLevel("PICKAXE", 1);
        setHardness(3);
        setResistance(6);
        IBlockState base = getBlockState().getBaseState().withProperty(VARIANT, "normal");
        if (!isDouble) base = base.withProperty(HALF, EnumBlockHalf.TOP);
        setDefaultState(base);
    }
    
    @Override
    public String getUnlocalizedName(int meta) {
        StringBuilder builder = new StringBuilder(getUnlocalizedName());
        if (meta % 8 >= 4) builder.append("_waxed");
        if (meta % 4 > 0) builder.append("_" + EnumWeatherStage.values()[meta % 4].getName());
        return builder.toString();
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
        return getPropertyName(stack.getMetadata());
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
                .withProperty(VARIANT, getPropertyName(meta % 8));
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return (isDouble ? 0 : (state.getValue(HALF) == EnumBlockHalf.TOP ? 8 : 0)) + getMeta(state.getValue(VARIANT));
    }
    
    private static List<String> getAllowedValues() {
        List<String> allowedValues = Lists.newArrayList();
        for (int i = 0; i < 8; i++) allowedValues.add(getPropertyName(i));
        return allowedValues;
    }
    
    private static String getPropertyName(int meta) {
        StringBuilder builder = new StringBuilder();
        if (meta % 8 >= 4) builder.append("waxed_");
        return builder.append("_" + EnumWeatherStage.values()[meta % 4].getName()).toString();
    }
    
    private int getMeta(String value) {
        int meta = 0;
        if (value.contains("waxed")) {
            meta = 4;
            value = value.substring(0, 5);
        }
        return meta + EnumWeatherStage.fromName(value).ordinal();
    }
    
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < 8; i++) items.add(new ItemStack(this, 1, i));
    }
    
}
