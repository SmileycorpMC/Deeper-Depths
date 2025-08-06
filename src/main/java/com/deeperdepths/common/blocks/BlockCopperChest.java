package com.deeperdepths.common.blocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import com.deeperdepths.common.blocks.tiles.TileCopperChest;
import com.deeperdepths.config.BlockConfig;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCopperChest extends BlockChest implements ICopperBlock, IBlockProperties {

    private final boolean waxed;

    protected BlockCopperChest(boolean waxed) {
        super(null);
        this.waxed = waxed;
        String name = "Copper_Chest";
        if (waxed) name = "Waxed_" + name;
        setUnlocalizedName(Constants.name(name));
        setRegistryName(Constants.loc(name));
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
        setSoundType(DeeperDepthsSoundTypes.COPPER);
        setHardness(BlockConfig.copper.getHardness());
        setResistance(BlockConfig.copper.getResistance());
        setHarvestLevel("pickaxe", BlockConfig.copper.getHarvestLevel());
        useNeighborBrightness = true;
        needsRandomTick = BlockConfig.copperAges &! waxed;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, WEATHER_STAGE, FACING);
    }

    @Override
    public TileCopperChest createTileEntity(World world, IBlockState state) {
        return new TileCopperChest();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing enumfacing = EnumFacing.getHorizontal(MathHelper.floor((double)(placer.rotationYaw * 4.f / 360f) + 0.5) & 3).getOpposite();
        state = state.withProperty(FACING, enumfacing);
        BlockPos posN = pos.north();
        BlockPos posS = pos.south();
        BlockPos posW = pos.west();
        BlockPos posE = pos.east();
        boolean north = canConnect(world, pos, posN);
        boolean south = canConnect(world, pos, posS);
        boolean west = canConnect(world, pos, posW);
        boolean east = canConnect(world, pos, posE);
        if (!north && !south && !west && !east) world.setBlockState(pos, state, 3);
        else if (enumfacing.getAxis() != EnumFacing.Axis.X || !north && !south) {
            if (enumfacing.getAxis() == EnumFacing.Axis.Z && (west || east)) {
                if (west) world.setBlockState(posW, state, 3);
                else world.setBlockState(posE, state, 3);
                world.setBlockState(pos, state, 3);
            }
        }
        else {
            if (north) world.setBlockState(posN, state, 3);
            else world.setBlockState(posS, state, 3);
            world.setBlockState(pos, state, 3);
        }
        if (stack.hasDisplayName()) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileCopperChest) ((TileCopperChest)te).setCustomName(stack.getDisplayName());
        }
    }

    private boolean canConnect(IBlockAccess world, BlockPos pos, BlockPos other) {
        IBlockState state = world.getBlockState(pos);
        IBlockState state1 = world.getBlockState(other);
        if (!(state1.getBlock() instanceof BlockCopperChest)) return false;
        if (!(world.getTileEntity(pos) instanceof TileCopperChest && world.getTileEntity(other) instanceof TileCopperChest)) return false;
        if (!BlockConfig.sameTypeChests) return true;
        return state.getValue(WEATHER_STAGE) == state1.getValue(WEATHER_STAGE) && state.getBlock() == state1.getBlock();
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        checkForSurroundingChests(world, pos, state);
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            BlockPos blockpos = pos.offset(enumfacing);
            IBlockState iblockstate = world.getBlockState(blockpos);
            if (canConnect(world, pos, blockpos)) checkForSurroundingChests(world, blockpos, iblockstate);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (canConnect(source, pos, pos.north())) return NORTH_CHEST_AABB;
        if (canConnect(source, pos, pos.south())) return SOUTH_CHEST_AABB;
        if (canConnect(source, pos, pos.east())) return EAST_CHEST_AABB;
        if (canConnect(source, pos, pos.west())) return WEST_CHEST_AABB;
        return NOT_CONNECTED_AABB;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(WEATHER_STAGE).ordinal() + state.getValue(FACING).getHorizontalIndex() * 4;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state) % 4;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(WEATHER_STAGE, EnumWeatherStage.values()[meta % 4])
                .withProperty(FACING, EnumFacing.getHorizontal(meta / 4));
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < 4; i++) items.add(new ItemStack(this, 1, i));
    }

    @Override
    public int getMaxMeta() {
        return 4;
    }

    @Override
    public String byMeta(int meta) {
        StringBuilder builder = new StringBuilder();
        if (waxed) builder.append("waxed_");
        if (meta % 4 > 0) builder.append(EnumWeatherStage.values()[meta % 4].getName() + "_");
        return builder + "copper_chest";
    }

    @Override
    public boolean interactRequiresSneak() {
        return true;
    }

    public boolean isWaxed() {
        return waxed;
    }

    @Override
    public boolean isWaxed(IBlockState state) {
        return waxed;
    }

    @Override
    public EnumWeatherStage getStage(IBlockState state) {
        return state.getValue(WEATHER_STAGE);
    }

    @Override
    public IBlockState getScraped(IBlockState state) {
        return DeeperDepthsBlocks.COPPER_CHEST.getDefaultState().withProperty(WEATHER_STAGE, waxed ? state.getValue(WEATHER_STAGE)
                        : state.getValue(WEATHER_STAGE).previous())
                .withProperty(FACING, state.getValue(FACING));
    }

    @Override
    public IBlockState getWaxed(IBlockState state) {
        return DeeperDepthsBlocks.WAXED_COPPER_CHEST.getDefaultState().withProperty(WEATHER_STAGE, state.getValue(WEATHER_STAGE))
                .withProperty(FACING, state.getValue(FACING));
    }

    @Override
    public IBlockState getWeathered(IBlockState state) {
        return state.withProperty(WEATHER_STAGE, state.getValue(WEATHER_STAGE).next());
    }

    @Override
    public boolean canWax(ItemStack stack) {
        return !waxed;
    }

    @Override
    public boolean canScrape(ItemStack stack) {
        return waxed || stack.getMetadata() > 0;
    }

    @Override
    public ItemStack getWaxed(ItemStack stack) {
        if (waxed) return stack;
        ItemStack stack1 = new ItemStack(DeeperDepthsBlocks.WAXED_COPPER_CHEST, 1, stack.getMetadata());
        if (stack.hasTagCompound()) stack1.setTagCompound(stack.getTagCompound());
        return stack1;
    }

    @Override
    public ItemStack getScraped(ItemStack stack) {
        if (!canScrape(stack)) return stack;
        ItemStack stack1 = new ItemStack(DeeperDepthsBlocks.COPPER_CHEST, 1, (waxed ? stack.getMetadata()
                : stack.getMetadata() - 1));
        if (stack.hasTagCompound()) stack1.setTagCompound(stack.getTagCompound());
        return stack1;
    }

    @Override
    public boolean isWaxed(ItemStack stack) {
        return waxed;
    }

}
