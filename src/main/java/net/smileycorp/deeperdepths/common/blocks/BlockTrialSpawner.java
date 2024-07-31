package net.smileycorp.deeperdepths.common.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundTypes;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumTrialSpawnerState;
import net.smileycorp.deeperdepths.common.blocks.tiles.TileTrialSpawner;

import javax.annotation.Nullable;

public class BlockTrialSpawner extends BlockTrial {
    
    public static final PropertyEnum<EnumTrialSpawnerState> STATE = PropertyEnum.create("state", EnumTrialSpawnerState.class);
    
    public BlockTrialSpawner() {
        super("Trial_Spawner");
        setSoundType(DeeperDepthsSoundTypes.TRIAL_SPAWNER);
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileTrialSpawner)) return state;
        TileTrialSpawner spawner = (TileTrialSpawner) te;
        return state.withProperty(STATE, spawner.getState()).withProperty(OMINOUS, spawner.isOminous());
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        TileEntity tile = world.getTileEntity(pos);
        if (player.isSneaking() |! (tile instanceof TileTrialSpawner) |! (stack.getItem() instanceof ItemMonsterPlacer))
            return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
        if (!world.isRemote) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey("EntityTag")) ((TileTrialSpawner)tile).modifyConfigs(cfg ->
                    cfg.setEntities(ImmutableMap.of(nbt.getCompoundTag("EntityTag"), 1)));
        }
        return true;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, OMINOUS, STATE);
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileTrialSpawner();
    }
    
    @Override
    public String byState(IBlockState state) {
        return (state.getValue(OMINOUS) ? "ominous_" : "") + "trial_spawner";
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }
    
}
