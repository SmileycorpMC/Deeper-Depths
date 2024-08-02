package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.util.TextUtils;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.DeeperDepths;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundTypes;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumWeatherStage;
import net.smileycorp.deeperdepths.common.items.ItemCopperDoor;

public class BlockCopperDoor extends BlockDoor implements IBlockProperties, ICopperBlock  {
    
    private final EnumWeatherStage stage;
    private final boolean waxed;
    private final ItemCopperDoor item;
    
    public BlockCopperDoor(EnumWeatherStage stage, boolean waxed) {
        super(Material.IRON);
        this.stage = stage;
        this.waxed = waxed;
        String name = "Copper_Door";
        if (stage != EnumWeatherStage.NORMAL) name = TextUtils.toProperCase(stage.getName()) + "_" + name;
        if (waxed) name = "Waxed_" + name;
        setUnlocalizedName(Constants.name(name));
        setRegistryName(Constants.loc(name));
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
        setSoundType(DeeperDepthsSoundTypes.COPPER);
        useNeighborBrightness = true;
        item = new ItemCopperDoor(this);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        BlockPos blockpos = state.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER ? pos : pos.down();
        IBlockState iblockstate = pos.equals(blockpos) ? state : world.getBlockState(blockpos);
        if (iblockstate.getBlock() != this) return false;
        state = iblockstate.cycleProperty(OPEN);
        world.setBlockState(blockpos, state, 10);
        world.markBlockRangeForRenderUpdate(blockpos, pos);
        world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                state.getValue(OPEN) ? DeeperDepthsSoundEvents.COPPER_DOOR_OPEN : DeeperDepthsSoundEvents.COPPER_DOOR_CLOSE,
                SoundCategory.BLOCKS, 1, 1);
        return true;
    }
    
    @Override
    public void toggleDoor(World world, BlockPos pos, boolean open) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != this) return;
        BlockPos blockpos = state.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER ? pos : pos.down();
        IBlockState other = pos == blockpos ? state : world.getBlockState(blockpos);
        if (other.getBlock() != this || other.getValue(OPEN) == open) return;
        world.setBlockState(blockpos, other.withProperty(OPEN, open), 10);
        world.markBlockRangeForRenderUpdate(blockpos, pos);
        world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                state.getValue(OPEN) ? DeeperDepthsSoundEvents.COPPER_DOOR_OPEN : DeeperDepthsSoundEvents.COPPER_DOOR_CLOSE,
                SoundCategory.BLOCKS, 1, 1);
    }
    
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
            BlockPos blockpos = pos.down();
            IBlockState iblockstate = world.getBlockState(blockpos);
            if (!(iblockstate.getBlock() instanceof BlockCopperDoor)) world.setBlockToAir(pos);
            else if (!(block instanceof BlockCopperDoor)) iblockstate.neighborChanged(world, blockpos, block, fromPos);
            return;
        }
        BlockPos otherpos = pos.up();
        IBlockState other = world.getBlockState(otherpos);
        if (!(other.getBlock() instanceof BlockCopperDoor)) {
            world.setBlockToAir(pos);
            if (!world.isRemote) dropBlockAsItem(world, pos, state, 0);
            return;
        }
        if (!world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP)) {
            world.setBlockToAir(pos);
            if (!(other.getBlock() instanceof BlockCopperDoor)) world.setBlockToAir(otherpos);
            if (!world.isRemote) dropBlockAsItem(world, pos, state, 0);
            return;
        }
        boolean power = world.isBlockPowered(pos) || world.isBlockPowered(otherpos);
        if (block instanceof BlockCopperDoor |! (power && block.getDefaultState().canProvidePower())
            || power == other.getValue(POWERED)) return;
        world.setBlockState(otherpos, other.withProperty(POWERED, power), 2);
        if (power == state.getValue(OPEN)) return;
        world.setBlockState(pos, state.withProperty(OPEN, power), 2);
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                state.getValue(OPEN) ? DeeperDepthsSoundEvents.COPPER_DOOR_OPEN : DeeperDepthsSoundEvents.COPPER_DOOR_CLOSE,
                SoundCategory.BLOCKS, 1, 1);
    }
    
    @Override
    public Item getItem() {
        return item;
    }
    
    @Override
    public boolean isWaxed(IBlockState state) {
        return waxed;
    }
    
    @Override
    public EnumWeatherStage getStage(IBlockState state) {
        return stage;
    }
    
    @Override
    public IBlockState getScraped(IBlockState state) {
        return stage == EnumWeatherStage.NORMAL ? state : copyProperties(state, DeeperDepthsBlocks.COPPER_DOORS.get(waxed ? stage : stage.previous()).getDefaultState());
    }
    
    private IBlockState copyProperties(IBlockState oldState, IBlockState newState) {
        return newState.withProperty(FACING, oldState.getValue(FACING)).withProperty(HALF, oldState.getValue(HALF))
                .withProperty(OPEN, oldState.getValue(OPEN)).withProperty(HINGE, oldState.getValue(HINGE));
    }
    
    @Override
    public IBlockState getWaxed(IBlockState state) {
        return waxed ? state : copyProperties(state, DeeperDepthsBlocks.WAXED_COPPER_DOORS.get(stage).getDefaultState());
    }
    
    @Override
    public IBlockState getWeathered(IBlockState state) {
        return waxed || stage == EnumWeatherStage.OXIDIZED ? state : copyProperties(state, DeeperDepthsBlocks.COPPER_DOORS.get(stage.next()).getDefaultState());
    }
    
    @Override
    public boolean interactRequiresSneak() {
        return true;
    }
    
    @Override
    public boolean scrape(World world, IBlockState state, BlockPos pos) {
        if (!isWaxed(state) && getStage(state) == EnumWeatherStage.NORMAL) return false;
        BlockPos otherpos = state.getValue(HALF) == EnumDoorHalf.LOWER ? pos.up() : pos.down();
        IBlockState other = world.getBlockState(otherpos);
        if (other.getBlock() != this) return false;
        world.setBlockState(pos, getScraped(state), 3);
        world.setBlockState(otherpos, getScraped(other), 3);
        return true;
    }
    
    @Override
    public boolean wax(World world, IBlockState state, BlockPos pos) {
        if (isWaxed(state)) return false;
        BlockPos otherpos = state.getValue(HALF) == EnumDoorHalf.LOWER ? pos.up() : pos.down();
        IBlockState other = world.getBlockState(otherpos);
        if (other.getBlock() != this) return false;
        world.setBlockState(pos, getWaxed(state), 3);
        world.setBlockState(otherpos, getWaxed(other), 3);
        return true;
    }
    
    @Override
    public boolean weather(World world, IBlockState state, BlockPos pos) {
        if (isWaxed(state)) return false;
        BlockPos otherpos = state.getValue(HALF) == EnumDoorHalf.LOWER ? pos.up() : pos.down();
        IBlockState other = world.getBlockState(otherpos);
        if (other.getBlock() != this) return false;
        world.setBlockState(pos, getWeathered(state), 3);
        world.setBlockState(otherpos, getWeathered(other), 3);
        return true;
    }
    
}
