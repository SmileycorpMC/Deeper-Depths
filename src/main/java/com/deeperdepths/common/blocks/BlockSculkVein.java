package com.deeperdepths.common.blocks;

import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.tiles.TileSculkVein;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSculkVein extends BlockDeeperDepths implements IHoeEfficient {
    
    protected static final AxisAlignedBB EMPTY_AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    protected static final AxisAlignedBB[] AABBS = {
            new AxisAlignedBB(0, 0, 0, 1, 0.1875D, 1),
            new AxisAlignedBB(0, 0.8125, 0, 1, 1, 1),
            new AxisAlignedBB(0, 0, 0, 1, 1, 0.1875D),
            new AxisAlignedBB(0, 0, 0.8125, 1, 1, 1),
            new AxisAlignedBB(0, 0, 0, 0.1875D, 1, 1),
            new AxisAlignedBB(0.8125, 0, 0, 1, 1, 1)
    };
    
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool EAST = PropertyBool.create("east");
    private static final PropertyBool[] PROPERTIES = {DOWN, UP, NORTH, SOUTH, WEST, EAST};
    
    public BlockSculkVein() {
        super("sculk_vein", Material.PLANTS, 0.2f, 0.2f);
        setSoundType(DeeperDepthsSoundTypes.SCULK_VEIN);
        setDefaultState(blockState.getBaseState().withProperty(UP, false).withProperty(DOWN, false).withProperty(NORTH, false)
                .withProperty(SOUTH, false).withProperty(EAST, false).withProperty(WEST, false));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PROPERTIES);
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileSculkVein();
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileSculkVein) {
            TileSculkVein vein = (TileSculkVein) te;
            for (EnumFacing facing : EnumFacing.values()) if (vein.hasFacing(facing))
                state = state.withProperty(getProperty(facing), true);
        }
        return state;
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
        return new ItemStack(this);
    }
    
    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        int count = 0;
        for (int i = 0; i < PROPERTIES.length; i++) if (state.getValue(PROPERTIES[i])) count++;
        return new ItemStack(this, count);
    }
    
    @Override
    public int quantityDropped(Random rand) {
        return 0;
    }
    
    @Override
    public boolean isToolEffective(String type, IBlockState state) {
        return false;
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        AxisAlignedBB aabb = EMPTY_AABB;
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileSculkVein) {
            TileSculkVein vein = (TileSculkVein) te;
            for (EnumFacing facing : EnumFacing.values()) if (vein.hasFacing(facing)) {
                if (aabb != EMPTY_AABB) return FULL_BLOCK_AABB;
                else aabb = AABBS[facing.ordinal()];
            }
        }
        return aabb;
    }
    
    @Override
    public RayTraceResult collisionRayTrace(IBlockState blockState, World world, BlockPos pos, Vec3d start, Vec3d end) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileSculkVein)) return null;
        TileSculkVein vein = (TileSculkVein) te;
        for (EnumFacing facing : EnumFacing.values()) if (vein.hasFacing(facing)) {
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
    
    public static PropertyBool getProperty(EnumFacing facing) {
        return PROPERTIES[facing.ordinal()];
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
    
}
