package com.deeperdepths.common.blocks;

import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.DeeperDepthsSoundTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSculkShrieker extends BlockSculk
{
    public static final AxisAlignedBB SLAB_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);

    public BlockSculkShrieker()
    {
        super("sculk_shrieker", 3, 3);
        setSoundType(DeeperDepthsSoundTypes.SCULK);
    }


    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
        if (entityIn instanceof EntityPlayer)
        { preformShriek(worldIn, pos); }

        super.onEntityWalk(worldIn, pos, entityIn);
    }

    public void preformShriek(World world, BlockPos pos)
    {
        world.playSound(null, pos, DeeperDepthsSoundEvents.SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS, 1.7F, (world.rand.nextFloat() * 0.4F) + 0.6F);

        for (int i = 0; i < 10; i++)
        { DeeperDepths.proxy.spawnParticle(8, world, pos.getX() + 0.5f, pos.getY() + 0.4F, pos.getZ() + 0.5f, 0, 0.1, 0, 5 * i); }
    }


    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return SLAB_AABB;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) { return false; }
}