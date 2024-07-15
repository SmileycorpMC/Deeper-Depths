package net.smileycorp.deeperdepths.common.blocks;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.DeeperDepths;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumWeatherStage;

public class BlockCutCopperSlab extends BlockSlab implements IBlockProperties, ICopperBlock {
    
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);
    
    private final boolean isDouble;
    
    public BlockCutCopperSlab(boolean isDouble) {
        super(Material.IRON);
        this.isDouble = isDouble;
        String name = (isDouble ? "Double_" : "") + "Cut_Copper_Slab";
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setHarvestLevel("PICKAXE", 1);
        setHardness(3);
        setResistance(6);
        IBlockState base = blockState.getBaseState().withProperty(VARIANT, Variant.NORMAL);
        if (!isDouble) base = base.withProperty(HALF,EnumBlockHalf.BOTTOM);
        setDefaultState(base);
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
        useNeighborBrightness = true;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return isDouble ? new BlockStateContainer(this, VARIANT) : new BlockStateContainer(this, VARIANT, HALF);
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
        return Variant.values()[stack.getMetadata() % 8];
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
                .withProperty(VARIANT, Variant.values()[meta % 8]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return (isDouble ? 0 : (state.getValue(HALF) == EnumBlockHalf.TOP ? 8 : 0)) + state.getValue(VARIANT).ordinal();
    }
    
    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(DeeperDepthsBlocks.CUT_COPPER_SLAB, 1, getMetaFromState(state) % 8);
    }
    
    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state) % 8;
    }
    
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < 8; i++) items.add(new ItemStack(this, 1, i));
    }
    
    @Override
    public int getMaxMeta() {
        return 8;
    }
    
    @Override
    public String byMeta(int meta) {
        Variant variant = Variant.values()[meta % 8];
        return (variant == Variant.NORMAL ? "" : variant.getName().replace("_normal", "") + "_") + "cut_copper_slab";
    }
    
    @Override
    public boolean isWaxed(IBlockState state) {
        return state.getValue(VARIANT).isWaxed();
    }
    
    @Override
    public EnumWeatherStage getStage(IBlockState state) {
        return state.getValue(VARIANT).getStage();
    }
    
    @Override
    public IBlockState getScraped(IBlockState state) {
        Variant variant = state.getValue(VARIANT);
        return variant.isWaxed() ? state.withProperty(VARIANT, variant.getUnwaxed()) : state.withProperty(VARIANT, variant.previous());
    }
    
    @Override
    public IBlockState getWaxed(IBlockState state) {
        return state.withProperty(VARIANT, state.getValue(VARIANT).getWaxed());
    }
    
    @Override
    public IBlockState getWeathered(IBlockState state) {
        return state.withProperty(VARIANT, state.getValue(VARIANT).next());
    }
    
    public enum Variant implements IStringSerializable {
        
        NORMAL(EnumWeatherStage.NORMAL, false),
        EXPOSED(EnumWeatherStage.EXPOSED, false),
        WEATHERED(EnumWeatherStage.WEATHERED, false),
        OXIDIZED(EnumWeatherStage.OXIDIZED, false),
        WAXED(EnumWeatherStage.NORMAL, true),
        WAXED_EXPOSED(EnumWeatherStage.EXPOSED, true),
        WAXED_WEATHERED(EnumWeatherStage.WEATHERED, true),
        WAXED_OXIDIZED(EnumWeatherStage.OXIDIZED, true);
        
        private static final Table<EnumWeatherStage, Boolean, Variant> VALUES = createTable();
        
        private static Table<EnumWeatherStage, Boolean, Variant> createTable() {
            ImmutableTable.Builder<EnumWeatherStage, Boolean, Variant> builder = ImmutableTable.builder();
            for (Variant variant : values()) builder.put(variant.stage, variant.waxed, variant);
            return builder.build();
        }
        
        private final EnumWeatherStage stage;
        private final boolean waxed;
        
        Variant(EnumWeatherStage stage, boolean waxed) {
            this.stage = stage;
            this.waxed = waxed;
        }
        
        public EnumWeatherStage getStage() {
            return stage;
        }
        
        public boolean isWaxed() {
            return waxed;
        }
        
        @Override
        public String getName() {
            return (waxed ? "waxed_" : "") + stage.getName();
        }
        
        public String getUnlocalizedName() {
            return (waxed ? "Waxed" : "") + stage.getUnlocalizedName();
        }
        
        public Variant previous() {
            return get(stage.previous(), waxed);
        }
        
        public Variant next() {
            return get(stage.next(), waxed);
        }
        
        public Variant getWaxed() {
            return get(stage, true);
        }
        
        public Variant getUnwaxed() {
            return get(stage, false);
        }
        
        public static Variant get(EnumWeatherStage stage, boolean waxed) {
            return VALUES.get(stage, waxed);
        }
        
    }
    
}
