package com.deeperdepths.common.blocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.enums.EnumStoneType;
import com.deeperdepths.config.BlockConfig;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.config.BlockStatEntry;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockDeepslate extends BlockRotatedPillar implements IBlockProperties {

    public static PropertyBool INFESTED = PropertyBool.create("infested");

    public BlockDeepslate() {
        super(Material.ROCK, MapColor.GRAY);
        setRegistryName(Constants.loc("deepslate"));
        setUnlocalizedName(Constants.name("Deepslate"));
        setHardness(BlockConfig.deepslate.getHardness());
        setResistance(BlockConfig.deepslate.getResistance());
        setHarvestLevel("pickaxe", BlockConfig.deepslate.getHarvestLevel());
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
        setSoundType(DeeperDepthsSoundTypes.DEEPSLATE);
        setDefaultState(blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Y).withProperty(INFESTED, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS, INFESTED);
    }

    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return getConfig(state).getHardness();
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return getConfig(world.getBlockState(pos)).getResistance() / 5f;
    }

    @Override
    public int getHarvestLevel(IBlockState state) {
        return getConfig(state).getHarvestLevel();
    }

    private BlockStatEntry getConfig(IBlockState state) {
        return state.getValue(INFESTED) ? BlockConfig.infestedDeepslate : BlockConfig.deepslate;
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(DeeperDepthsBlocks.STONE);
    }
    
    @Override
    public int damageDropped(IBlockState state) {
        return EnumStoneType.COBBLED_DEEPSLATE.ordinal();
    }
    
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult ray, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this);
    }
    
    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(this);
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!state.getValue(INFESTED)) {
            super.dropBlockAsItemWithChance(world, pos, state, chance, fortune);
            return;
        }
        if (world.isRemote |! world.getGameRules().getBoolean("doTileDrops")) return;
        EntitySilverfish silverfish = new EntitySilverfish(world);
        silverfish.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
        world.spawnEntity(silverfish);
        silverfish.spawnExplosionParticle();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return super.getMetaFromState(state) + (state.getValue(INFESTED) ? 1 : 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta).withProperty(INFESTED, meta % 4 > 0);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < 2; i++) items.add(new ItemStack(this, 1, i));
    }

    @Override
    public int getMaxMeta() {
        return 2;
    }

    @Override
    public String byState(IBlockState state) {
        return state.getValue(INFESTED) ? "infested_deepslate" : "deepslate";
    }

    @Override
    public String byMeta(int meta) {
        return meta > 0 ? "infested_deepslate" : "deepslate";
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        ItemStack stack = placer.getHeldItem(hand);
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(INFESTED, stack.getMetadata() > 0);
    }
    
}
