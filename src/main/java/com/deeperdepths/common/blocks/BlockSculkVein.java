package com.deeperdepths.common.blocks;

import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.items.DeeperDepthsItems;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.*;

//ok so goddamn I know this shit sucks
//but 1.12 doesn't do this
//like nobody will notice or care but these singlehandedly break the engine
//1.12 wasn't built for sculk veins
public class BlockSculkVein extends BlockDeeperDepths implements IHoeEfficient {
    
    //so idk if this is better but I'd rather not calculate this every time the state needs to be converted
    private static final EnumMap<EnumFacing, Integer> DIR_FLAGS = getFlags();
    
    //oh boy flag time
    //uh so because I couldn't figure out a good way to do it we're doing binary math with all the directions being bits
    //then just calculating which bits are flipped and converting the whole thing as a number
    private static EnumMap<EnumFacing, Integer> getFlags() {
        EnumMap<EnumFacing, Integer> flags = Maps.newEnumMap(EnumFacing.class);
        for (EnumFacing facing : EnumFacing.values()) flags.put(facing, (int) Math.pow(2, facing.ordinal()));
        return flags;
    }
    
    protected static final AxisAlignedBB EMPTY_AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    protected static final AxisAlignedBB[] AABBS = {
            new AxisAlignedBB(0, 0, 0, 1, 0.1875D, 1),
            new AxisAlignedBB(0, 0.8125, 0, 1, 1, 1),
            new AxisAlignedBB(0, 0, 0, 1, 1, 0.1875D),
            new AxisAlignedBB(0, 0, 0.8125, 1, 1, 1),
            new AxisAlignedBB(0, 0, 0, 0.1875D, 1, 1),
            new AxisAlignedBB(0.8125, 0, 0, 1, 1, 1)
    };
    
    //the actual property we use, debating rewriting to remove this and make the code worse to make the f3 screen look good or renaming it
    public static final PropertyInteger META = PropertyInteger.create("meta", 0, 15);
    
    //bullshit random ass properties that honestly don't need to exist, but it makes statemapping easier and the F3 screen make sense maybe
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool EAST = PropertyBool.create("east");
    private static final PropertyBool[] DIRECTIONS = {DOWN, UP, NORTH, SOUTH, WEST, EAST};
    
    //need to store this somewhere so I can convert the meta back easily
    private final int ordinal;
    
    public BlockSculkVein(int ordinal) {
        super("sculk_vein_" + ordinal, Material.PLANTS, 0.2f, 0.2f);
        this.ordinal = ordinal;
        setSoundType(DeeperDepthsSoundTypes.SCULK_VEIN);
        setDefaultState(blockState.getBaseState().withProperty(UP, false).withProperty(DOWN, false).withProperty(NORTH, false)
                .withProperty(SOUTH, false).withProperty(EAST, false).withProperty(WEST, false));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, META, DOWN, UP, NORTH, SOUTH, WEST, EAST);
    }
    
    //the simple conversions
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(META, meta);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(META);
    }
    
    //again probably not needed but makes statemapping easier and f3 look better
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        IBlockState state1 = getDefaultState();
        for (EnumFacing facing : getFacings(state)) state1 = state1.withProperty(getProperty(facing), true);
        return state1;
    }
    
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos from) {
        boolean changed = false;
        List<EnumFacing> facings = Lists.newArrayList(getFacings(state));
        Iterator<EnumFacing> iterator = facings.iterator();
        while (iterator.hasNext()) {
            EnumFacing facing = iterator.next();
            BlockPos pos1 = pos.offset(facing);
            IBlockState state1 = world.getBlockState(pos1);
            if (isSideSolid(state1, world, pos1, facing.getOpposite())) continue;
            iterator.remove();
            changed = true;
        }
        if (changed) world.setBlockState(pos, getBlockState(facings.toArray(new EnumFacing[facings.size()])));
    }
    
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return MapColor.BLACK;
    }
    
    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }
    
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(DeeperDepthsItems.SCULK_VEIN);
    }
    
    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(DeeperDepthsItems.SCULK_VEIN, getFacings(state).length);
    }
    
    @Override
    public int quantityDropped(Random rand) {
        return 0;
    }
    
    @Override
    public boolean isToolEffective(String type, IBlockState state) {
        return false;
    }
    
    //no dynamic bounding boxes so we just go fullblock if we have more than one face
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        AxisAlignedBB aabb = EMPTY_AABB;
        for (EnumFacing facing : getFacings(state)) {
            if (aabb != EMPTY_AABB) return FULL_BLOCK_AABB;
            else aabb = AABBS[facing.ordinal()];
        }
        return aabb;
    }
    
    //jank ass shit to let you place stuff through the open faces
    //(you can't put more than 2 sculk veins in the block without doing this)
    //(adding right click functionality instead would mean we need to do vector math, which I really don't want to do)
    @Override
    public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end) {
        for (EnumFacing facing : getFacings(state)) {
            RayTraceResult result = rayTrace(pos, start, end, AABBS[facing.ordinal()]);
            if (result != null) return result;
        }
        return null;
    }
    
    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing facing) {
        return world.isSideSolid(pos.offset(facing.getOpposite()), facing);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) {
        return NULL_AABB;
    }
   
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    //spin
    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing facing) {
        return world.setBlockState(pos, getBlockState(Arrays.stream(getFacings(world.getBlockState(pos)))
                .map(f -> f.rotateAround(facing.getAxis())).toArray(EnumFacing[]::new)));
    }
    
    //YOU, YOU'RE WHY WE CAN'T HAVE NICE THINGS LIKE TILE ENTITIES
    //(ngl this stuff being tiles probably would break something else, but still)
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirror) {
        return getBlockState(Arrays.stream(getFacings(state)).map(f -> mirror.mirror(f)).toArray(EnumFacing[]::new));
    }
    
    //used for item registry jank
    public int getOrdinal() {
        return ordinal;
    }
    
    //normal function
    public static PropertyBool getProperty(EnumFacing facing) {
        return DIRECTIONS[facing.ordinal()];
    }
    
    //ignore everything below here it's all jank and bad
    public static boolean hasFacing(IBlockState state, EnumFacing facing) {
        return (getMeta(state) & DIR_FLAGS.get(facing)) > 0;
    }
    
    public static EnumFacing[] getFacings(IBlockState state) {
        return getFacings(getMeta(state));
    }
    
    public static EnumFacing[] getFacings(int meta) {
        return Arrays.stream(EnumFacing.values()).filter(f -> (DIR_FLAGS.get(f) & meta) > 0).toArray(EnumFacing[]::new);
    }
    
    private static int getMeta(IBlockState state) {
        if (!(state.getBlock() instanceof BlockSculkVein)) return 0;
        return ((BlockSculkVein) state.getBlock()).getOrdinal() * 16 + state.getValue(META);
    }
    
    public static IBlockState getBlockState(int meta) {
        return meta == 0 ? Blocks.AIR.getDefaultState() : DeeperDepthsBlocks.SCULK_VEINS[meta / 16].getDefaultState().withProperty(META, meta % 16);
    }
    
    public static IBlockState getBlockState(EnumFacing... facings) {
        return getBlockState(getMeta(facings));
    }
    
    public static int getMeta(EnumFacing... facings) {
        int meta = 0;
        for (EnumFacing facing : facings) meta += DIR_FLAGS.get(facing);
        return meta;
    }
    
}
