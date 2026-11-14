package com.deeperdepths.common.blocks.tiles;

import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.blocks.BlockSculkShrieker;
import com.deeperdepths.common.capabilities.VibrationHandler;
import com.deeperdepths.common.capabilities.WardenSpawnTracker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.smileycorp.atlas.api.util.DirectionUtils;

import javax.annotation.Nullable;

public class TileSculkShrieker extends TileEntity implements ITickable, VibrationHandler.Listener {

    private int cooldown = 0;

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        if (world instanceof WorldServer) VibrationHandler.getInstance((WorldServer) world).add(this);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (world instanceof WorldServer) VibrationHandler.getInstance((WorldServer) world).remove(this);
    }

    @Override
    public Vec3d getPosition() {
        return DirectionUtils.centerOf(pos);
    }

    @Override
    public int getRange() {
        return 8;
    }

    @Override
    public boolean canHear(@Nullable Entity entity, SoundEvent sound, double x, double y, double z) {
        return sound == DeeperDepthsSoundEvents.SCULK_SENSOR_CLICKING;
    }

    @Override
    public void reactToSound(@Nullable Entity entity, SoundEvent sound, double x, double y, double z) {
        tryShriek(entity);
    }

    @Override
    public void update() {
        if (cooldown > 0) cooldown--;
    }

    public void tryShriek(@Nullable Entity entity) {
        if (entity == null) return;
        EntityPlayer player = null;
        if (entity instanceof EntityPlayer) player = (EntityPlayer) entity;
        for (EntityPlayer player1 : entity.getRecursivePassengersByType(EntityPlayer.class)) {
            player = player1;
            break;
        }
        if (player == null) return;
        if (world.getDifficulty() != EnumDifficulty.PEACEFUL) {
            int warningLevel = WardenSpawnTracker.tryWarn(world, pos, player);
            if (warningLevel == 0) return;
            //darkness warn and spawning
        }
        cooldown = 90;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockSculkShrieker &! state.getValue(BlockSculkShrieker.WATERLOGGED))
            world.playSound(null, pos, DeeperDepthsSoundEvents.SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS, 1.7F, (world.rand.nextFloat() * 0.4F) + 0.6F);

        for (int i = 0; i < 10; i++) DeeperDepths.proxy.spawnParticle(8, world, pos.getX() + 0.5f, pos.getY() + 0.4F, pos.getZ() + 0.5f, 0, 0.1, 0, 5 * i);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("cooldown", cooldown);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        cooldown = compound.getInteger("cooldown");
    }

}
