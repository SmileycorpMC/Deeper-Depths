package net.smileycorp.deeperdepths.common.blocks;

import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.util.TextUtils;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.DeeperDepths;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundTypes;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumWeatherStage;

public class BlockCopperTrapdoor extends BlockTrapDoor implements IBlockProperties, ICopperBlock {
    
    private final EnumWeatherStage stage;
    private final boolean waxed;
    
    public BlockCopperTrapdoor(EnumWeatherStage stage, boolean waxed) {
        super(Material.IRON);
        this.stage = stage;
        this.waxed = waxed;
        String name = "Copper_Trapdoor";
        if (stage != EnumWeatherStage.NORMAL) name = TextUtils.toProperCase(stage.getName()) + "_" + name;
        if (waxed) name = "Waxed_" + name;
        setUnlocalizedName(Constants.name(name));
        setRegistryName(Constants.loc(name));
        setCreativeTab(DeeperDepths.CREATIVE_TAB);
        setSoundType(DeeperDepthsSoundTypes.COPPER);
        useNeighborBrightness = true;
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        state = state.cycleProperty(OPEN);
        world.setBlockState(pos, state, 2);
        world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                state.getValue(OPEN) ? DeeperDepthsSoundEvents.COPPER_TRAPDOOR_OPEN : DeeperDepthsSoundEvents.COPPER_TRAPDOOR_CLOSE,
                SoundCategory.BLOCKS, 1, 1);
        return true;
    }
    
    @Override
    public boolean isWaxed(IBlockState state) {
        return waxed;
    }
    
    @Override
    public EnumWeatherStage getStage(IBlockState state) {
        return stage;
    }
    
    @Override
    public IBlockState getScraped(IBlockState state) {
        return stage == EnumWeatherStage.NORMAL ? state : copyProperties(state, DeeperDepthsBlocks.COPPER_TRAPDOORS.get(waxed ? stage : stage.previous()).getDefaultState());
    }
    
    private IBlockState copyProperties(IBlockState oldState, IBlockState newState) {
        return newState.withProperty(FACING, oldState.getValue(FACING)).withProperty(HALF, oldState.getValue(HALF))
                .withProperty(OPEN, oldState.getValue(OPEN));
    }
    
    @Override
    public IBlockState getWaxed(IBlockState state) {
        return waxed ? state : copyProperties(state, DeeperDepthsBlocks.WAXED_COPPER_TRAPDOORS.get(stage).getDefaultState());
    }
    
    @Override
    public IBlockState getWeathered(IBlockState state) {
        return waxed || stage == EnumWeatherStage.OXIDIZED ? state : copyProperties(state, DeeperDepthsBlocks.COPPER_TRAPDOORS.get(stage.next()).getDefaultState());
    }
    
    @Override
    public boolean interactRequiresSneak() {
        return true;
    }
    
}
