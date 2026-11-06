package com.deeperdepths.common.blocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.blocks.enums.EnumPillarType;
import com.deeperdepths.config.BlockConfig;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockPillar extends BlockRotatedPillar implements IBlockProperties {

    public static PropertyEnum<EnumPillarType> VARIANT = PropertyEnum.create("variant", EnumPillarType.class);

    public BlockPillar() {
        super(Material.ROCK, MapColor.GRAY);
        setRegistryName(Constants.loc("pillar"));
        setUnlocalizedName(Constants.name("pillar"));
        setHarvestLevel("pickaxe", BlockConfig.deepslate.getHarvestLevel());
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
        setDefaultState(blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Y).withProperty(VARIANT, EnumPillarType.CALCITE));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS, VARIANT);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(VARIANT).getMapColor();
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        return state.getValue(VARIANT).getSoundType();
    }

    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return state.getValue(VARIANT).getHardness();
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return world.getBlockState(pos).getValue(VARIANT).getResistance() / 5f;
    }

    @Override
    public int getHarvestLevel(IBlockState state) {
        return state.getValue(VARIANT).getHarvestLevel();
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < EnumPillarType.values().length; i++) items.add(new ItemStack(this, 1, i));
    }

    @Override
    public int getMaxMeta() {
        return EnumPillarType.values().length;
    }

    @Override
    public String byMeta(int meta) {
        return EnumPillarType.get(meta).getName() + "_pillar";
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return super.getMetaFromState(state) + 3 * (state.getValue(VARIANT).ordinal());
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta).withProperty(VARIANT, EnumPillarType.get(meta / 3));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        ItemStack stack = placer.getHeldItem(hand);
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(VARIANT, EnumPillarType.get(stack.getMetadata()));
    }

}
