package com.deeperdepths.common.blocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockCopperTorch extends BlockTorch implements IBlockProperties {

    public BlockCopperTorch() {
        String name = "Copper_Torch";
        setUnlocalizedName(Constants.name(name));
        setRegistryName(Constants.loc(name));
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
    }

    @Override
    @SideOnly(Side.CLIENT)
    //wowie the code is the exact same but the particles MUST be green
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        EnumFacing enumfacing = state.getValue(FACING);
        double x = (double)pos.getX() + 0.5D;
        double y = (double)pos.getY() + 0.7D;
        double z = (double)pos.getZ() + 0.5D;
        if (enumfacing.getAxis().isVertical())
        {
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0, 0, 0);
            DeeperDepths.proxy.spawnParticle(3, world, x, y, z, 0.0D, 0.0D, 0.0D, 2);
            return;
        }
        EnumFacing opposite = enumfacing.getOpposite();
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.27 * (double)opposite.getFrontOffsetX(), y + 0.22, z + 0.27 * (double)opposite.getFrontOffsetZ(), 0, 0, 0);
        DeeperDepths.proxy.spawnParticle(3, world, x + 0.27 * (double)opposite.getFrontOffsetX(), y + 0.22, z + 0.27 * (double)opposite.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, 2);
    }

}
