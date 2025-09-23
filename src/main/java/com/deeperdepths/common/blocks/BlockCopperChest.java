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
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class BlockCopperChest extends BlockChest implements ICopperBlock, IBlockProperties {

    private static boolean dropInventory = true;
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
    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        tryWeather(world, pos, state, random);
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
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        ItemStack stack = placer.getHeldItem(hand);
        return getDefaultState().withProperty(WEATHER_STAGE, EnumWeatherStage.values()[stack.getMetadata() % 4])
                .withProperty(FACING, EnumFacing.getHorizontal(MathHelper.floor((double)(placer.rotationYaw * 4f / 360f) + 0.5) & 3).getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {}

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {}

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof TileCopperChest)) return NOT_CONNECTED_AABB;
        EnumFacing direction = ((TileCopperChest) tile).getOtherDirection();
        if (direction == null) return NOT_CONNECTED_AABB;
        switch (direction) {
            case NORTH:
                return NORTH_CHEST_AABB;
            case SOUTH:
                return SOUTH_CHEST_AABB;
            case WEST:
                return WEST_CHEST_AABB;
            case EAST:
                return EAST_CHEST_AABB;
            default:
                return NOT_CONNECTED_AABB;
        }
    }

    @Nullable
    public ILockableContainer getContainer(World world, BlockPos pos, boolean allowBlocking) {
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof TileCopperChest)) return null;
        TileCopperChest chest = (TileCopperChest)tile;
        if (!allowBlocking && isBlocked(world, pos)) return null;
        EnumFacing direction = chest.getOtherDirection();
        if (direction == null) return chest;
        TileCopperChest other = (TileCopperChest) world.getTileEntity(pos.offset(direction));
        return direction == EnumFacing.WEST || direction == EnumFacing.NORTH ?
                new InventoryLargeChest("container.deeperdepths.copper_double_chest", chest, other)
                : new InventoryLargeChest("container.deeperdepths.copper_double_chest", other, chest);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (!dropInventory) return;
        super.breakBlock(world, pos, state);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state) % 4;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(WEATHER_STAGE).ordinal() + state.getValue(FACING).getHorizontalIndex() * 4;
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

    @Override
    public boolean scrape(World world, IBlockState state, BlockPos pos) {
        if (!isWaxed(state) && getStage(state) == EnumWeatherStage.NORMAL) return false;
        return setBlockStates(world, getScraped(state), pos);
    }

    @Override
    public boolean wax(World world, IBlockState state, BlockPos pos) {
        if (isWaxed(state)) return false;
        return setBlockStates(world, getWaxed(state), pos);
    }

    @Override
    public boolean weather(World world, IBlockState state, BlockPos pos) {
        if (isWaxed(state)) return false;
        return setBlockStates(world, getWeathered(state), pos);
    }

    private boolean setBlockStates(World world, IBlockState state, BlockPos pos) {
        Optional<BlockPos> optional = getOtherChest(world, pos);
        TileEntity tile1 = world.getTileEntity(pos);
        TileEntity tile2 = null;
        if (tile1 instanceof TileCopperChest) ((TileCopperChest) tile1).disableRefresh();
        if (optional.isPresent()) {
            BlockPos other = optional.get();
            tile2 = world.getTileEntity(other);
            if (tile2 instanceof TileCopperChest) ((TileCopperChest) tile2).disableRefresh();
            setBlockState(world, state, other);
        }
        boolean flag = setBlockState(world, state, pos);
        if (tile1 instanceof TileCopperChest) ((TileCopperChest) tile1).enableRefresh();
        if (tile2 instanceof TileCopperChest) ((TileCopperChest) tile2).enableRefresh();
        return flag;
    }

    private static boolean setBlockState(World world, IBlockState state, BlockPos pos) {
        if (world.isRemote) return true;
        dropInventory = false;
        TileEntity tile = world.getTileEntity(pos);
        boolean flag = world.setBlockState(pos, state);
        dropInventory = true;
        if (!flag) return false;
        if (tile != null) {
            tile.validate();
            world.setTileEntity(pos, tile);
        }
        return true;
    }

    private Optional<BlockPos> getOtherChest(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof TileCopperChest)) return Optional.empty();
        EnumFacing direction = ((TileCopperChest) tile).getOtherDirection();
        return direction == null ? Optional.empty() : Optional.of(pos.offset(direction));
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
