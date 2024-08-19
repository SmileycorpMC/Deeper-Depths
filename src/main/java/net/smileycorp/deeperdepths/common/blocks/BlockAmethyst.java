package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundTypes;

public class BlockAmethyst extends BlockDeeperDepths {
    
    public BlockAmethyst(String name) {
        super(name, Material.ROCK, 1.5f, 1.5f, 0);
        setSoundType(DeeperDepthsSoundTypes.AMETHYST);
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (world.isRemote | !(entity instanceof IProjectile)) return;
        world.playSound(null, pos, DeeperDepthsSoundEvents.AMETHYST_BLOCK_HIT, SoundCategory.BLOCKS,
                1, 0.5f + world.rand.nextFloat() * 1.2f);
        world.playSound(null, pos, DeeperDepthsSoundEvents.AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS,
                1, 0.5f + world.rand.nextFloat() * 1.2f);
    }
    
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return MapColor.PURPLE;
    }
    
}