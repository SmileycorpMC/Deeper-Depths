package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumTrialSpawnerState;
import net.smileycorp.deeperdepths.common.blocks.tiles.TileTrialSpawner;

import javax.annotation.Nullable;

public class BlockTrialSpawner extends BlockTrial {
    
    public static final PropertyEnum<EnumTrialSpawnerState> STATE = PropertyEnum.create("state", EnumTrialSpawnerState.class);
    
    public BlockTrialSpawner() {
        super("Trial_Spawner");
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileTrialSpawner)) return state;
        TileTrialSpawner spawner = (TileTrialSpawner) te;
        return state.withProperty(STATE, spawner.getState()).withProperty(OMINOUS, spawner.isOminous());
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
