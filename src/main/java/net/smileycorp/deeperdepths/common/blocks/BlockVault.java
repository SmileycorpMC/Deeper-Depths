package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumVaultState;
import net.smileycorp.deeperdepths.common.blocks.tiles.TileVault;

public class BlockVault extends BlockTrial {
    
    public static final PropertyEnum<EnumVaultState> STATE = PropertyEnum.create("state", EnumVaultState.class);
    
    public BlockVault() {
        super("Vault");
        setDefaultState(blockState.getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockHorizontal.FACING, OMINOUS, STATE);
    }
    
    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(DeeperDepthsBlocks.VAULT, 1, getMetaFromState(state) % 2);
    }
    
    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state) % 2;
    }
    
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < 2; i++) items.add(new ItemStack(this, 1, i));
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileVault(meta % 2 == 1);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking() |! (world.getTileEntity(pos) instanceof TileVault || stack.isEmpty()) || state.getValue(STATE) != EnumVaultState.ACTIVE)
            return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
        if (!world.isRemote) ((TileVault) world.getTileEntity(pos)).interact(player, stack);
        return true;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        return (te instanceof TileVault) ? state.withProperty(STATE, ((TileVault)te).getState()) : state;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(OMINOUS, meta % 2 == 1).withProperty(BlockHorizontal.FACING, EnumFacing.getHorizontal((meta / 2) % 4));
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(OMINOUS) ? 1 : 0) + state.getValue(BlockHorizontal.FACING).getHorizontalIndex() * 2;
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(OMINOUS, placer.getHeldItem(hand).getMetadata() % 2 == 1)
                .withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing().getOpposite());
    }
    
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(BlockHorizontal.FACING, rot.rotate(state.getValue(BlockHorizontal.FACING)));
    }
    
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirror) {
        return state.withRotation(mirror.toRotation(state.getValue(BlockHorizontal.FACING)));
    }
    
    @Override
    public int getMaxMeta() {
        return 2;
    }
    
    @Override
    public String byMeta(int meta) {
        return meta % 2 == 1 ? "ominous_vault" : "vault";
    }
    
}
