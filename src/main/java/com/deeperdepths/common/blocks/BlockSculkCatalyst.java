package com.deeperdepths.common.blocks;

import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.tiles.SculkCharge;
import com.deeperdepths.common.blocks.tiles.TileSculkCatalyst;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSculkCatalyst extends BlockSculk implements ITileEntityProvider
{
    public BlockSculkCatalyst()
    {
        super("sculk_catalyst", 3, 3);
        setSoundType(DeeperDepthsSoundTypes.SCULK);
    }
    
    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        return 5;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileSculkCatalyst tile = (TileSculkCatalyst) world.getTileEntity(pos);

        if (!world.isRemote && tile != null)
        {
            tile.activeSpreaders.add(new SculkCharge(world, pos.down(), 5));
            DeeperDepths.proxy.spawnParticle(8, world, pos.getX() + 0.5f, pos.getY() + 1.1F, pos.getZ() + 0.5f, 0, 0.1, 0);
            world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,  DeeperDepthsSoundEvents.TRIAL_POT_INSERT, SoundCategory.BLOCKS, 1, 1);
        }
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileSculkCatalyst();
    }
}
