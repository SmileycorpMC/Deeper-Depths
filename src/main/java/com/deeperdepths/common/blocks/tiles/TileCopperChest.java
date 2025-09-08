package com.deeperdepths.common.blocks.tiles;

import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.blocks.BlockCopperChest;
import com.deeperdepths.common.blocks.ICopperBlock;
import com.deeperdepths.common.blocks.enums.EnumWeatherStage;
import com.deeperdepths.config.BlockConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.VanillaDoubleChestItemHandler;

import javax.annotation.Nullable;

public class TileCopperChest extends TileEntityChest {

    private IItemHandler itemHandler;
    private EnumWeatherStage stage = null;
    private boolean waxed = false;
    private EnumFacing other = null;
    private int ticksSinceSync;

    /*
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : getTranslationKey();
    }*/

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("OtherChest")) other = EnumFacing.getHorizontal(compound.getByte("OtherChest"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if (other != null) compound.setByte("OtherChest", (byte) other.getHorizontalIndex());
        return super.writeToNBT(compound);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return false;
    }

    @Override
    public void update() {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        ticksSinceSync++;
        if (!world.isRemote && numPlayersUsing != 0 && (ticksSinceSync + x + y + z) % 200 == 0) {
            numPlayersUsing = 0;
            for (EntityPlayer entityplayer : world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(x - 5, y - 5, z - 5, x + 6, y + 6, z + 6))) {
                if (!(entityplayer.openContainer instanceof ContainerChest)) continue;
                IInventory inv = ((ContainerChest)entityplayer.openContainer).getLowerChestInventory();
                if (inv == this || inv instanceof InventoryLargeChest && ((InventoryLargeChest)inv).isPartOfLargeChest(this)) numPlayersUsing++;
            }
        }
        prevLidAngle = lidAngle;
        if (numPlayersUsing > 0 && lidAngle == 0) playSound(getOpenSound());
        if (numPlayersUsing == 0 && lidAngle > 0 || numPlayersUsing > 0 && lidAngle < 1) {
            float f2 = lidAngle;
            if (numPlayersUsing > 0) lidAngle += 0.1f;
            else lidAngle -= 0.1f;
            if (lidAngle > 1) lidAngle = 1;
            if (lidAngle < 0.5 && f2 >= 0.5) playSound(getCloseSound());
            if (lidAngle < 0) lidAngle = 0.0F;
        }
    }

    private SoundEvent getOpenSound() {
        EnumWeatherStage stage = getWeatherStage();
        if (stage == EnumWeatherStage.WEATHERED) return DeeperDepthsSoundEvents.COPPER_CHEST_WEATHERED_OPEN;
        if (stage == EnumWeatherStage.OXIDIZED) return DeeperDepthsSoundEvents.COPPER_CHEST_OXIDIZED_OPEN;
        return DeeperDepthsSoundEvents.COPPER_CHEST_OPEN;
    }

    private SoundEvent getCloseSound() {
        EnumWeatherStage stage = getWeatherStage();
        if (stage == EnumWeatherStage.WEATHERED) return DeeperDepthsSoundEvents.COPPER_CHEST_WEATHERED_CLOSE;
        if (stage == EnumWeatherStage.OXIDIZED) return DeeperDepthsSoundEvents.COPPER_CHEST_OXIDIZED_CLOSE;
        return DeeperDepthsSoundEvents.COPPER_CHEST_CLOSE;
    }

    private void playSound(SoundEvent sound) {
        EnumFacing direction = getOtherDirection();
        if (direction == EnumFacing.NORTH || direction == EnumFacing.WEST) return;
        world.playSound(null, pos.getX() + (direction == EnumFacing.EAST? 1.5 : 0.5), pos.getY() + 0.5, pos.getZ() + (direction == EnumFacing.SOUTH ? 1.5 : 0.5),
                sound, SoundCategory.BLOCKS, 0.5f, world.rand.nextFloat() * 0.1f + 0.9f);
    }

    public void setNeighbor(EnumFacing side) {
        other = side;
    }

    public boolean canConnect(BlockPos pos) {
        if (world == null) return false;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (!(block instanceof BlockCopperChest)) return false;
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileCopperChest)) return false;
        if (te.isInvalid()) return false;
        EnumFacing direction = ((TileCopperChest) te).other;
        if (direction != null) if (!pos.offset(direction).equals(this.pos)) return false;
        if (BlockConfig.sameTypeChests) {
            if (state.getValue(ICopperBlock.WEATHER_STAGE) != getWeatherStage()) return false;
            if (((BlockCopperChest) block).isWaxed() != isWaxed()) return false;
        }
        return true;
    }

    public EnumFacing getOtherDirection() {
        if (world == null || other == null) return null;
        if (!canConnect(pos.offset(other))) other = null;
        return other;
    }

    @Override
    public void checkForAdjacentChests() {
        if (other != null) {
            if (canConnect(pos.offset(other))) return;
            else other = null;
        }
        EnumFacing direction = world.getBlockState(pos).getValue(BlockHorizontal.FACING);
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (facing.getAxis() == direction.getAxis()) continue;
            BlockPos blockpos = pos.offset(facing);
            if (!canConnect(blockpos)) continue;
            setNeighbor(facing);
            ((TileCopperChest)world.getTileEntity(blockpos)).setNeighbor(facing.getOpposite());
            return;
        }
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if(doubleChestHandler == null || doubleChestHandler.needsRefresh()) {
                EnumFacing direction = getOtherDirection();
                doubleChestHandler = new VanillaDoubleChestItemHandler(this, (TileEntityChest) world.getTileEntity(pos.offset(direction)),
                                direction == EnumFacing.SOUTH || direction == EnumFacing.EAST);
            }
            return (T) ((doubleChestHandler != null && doubleChestHandler != VanillaDoubleChestItemHandler.NO_ADJACENT_CHESTS_INSTANCE)
                    ? (itemHandler == null ? (itemHandler = createUnSidedHandler()) : itemHandler) :  doubleChestHandler);
        }
        return super.getCapability(capability, facing);
    }

    public EnumWeatherStage getWeatherStage() {
        if (world == null || pos == null) return stage;
        IBlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof BlockCopperChest)) return null;
        return world.getBlockState(pos).getValue(ICopperBlock.WEATHER_STAGE);
    }

    public boolean isWaxed() {
        if (world == null || pos == null) return waxed;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (!(block instanceof BlockCopperChest)) return false;
        return ((BlockCopperChest) block).isWaxed();
    }

    public boolean isLarge() {
        return other != null;
    }

    public void setupRenderProperties(EnumWeatherStage stage, boolean waxed) {
        this.stage = stage;
        this.waxed = waxed;
    }

    //maybe just copper chest vs large copper chest is better
    public String getTranslationKey() {
        StringBuilder builder = new StringBuilder("container.deeperdepths.");
        if (isLarge()) builder.append("large_");
        if (isWaxed()) builder.append("waxed_");
        EnumWeatherStage stage = getWeatherStage();
        if (stage != null && stage != EnumWeatherStage.NORMAL) builder.append(stage.getName() + "_");
        return builder.append("copper_chest").toString();
    }

}
