package com.deeperdepths.common.blocks.tiles;

import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.blocks.BlockSculkShrieker;
import com.deeperdepths.common.capabilities.VibrationHandler;
import com.deeperdepths.common.capabilities.WardenSpawnTracker;
import com.deeperdepths.common.entities.EntityWarden;
import com.deeperdepths.common.potion.DeeperDepthsPotions;
import com.deeperdepths.config.BlockConfig;
import com.deeperdepths.config.VibrationsConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.smileycorp.atlas.api.util.DirectionUtils;

import javax.annotation.Nullable;
import java.util.Random;

public class TileSculkShrieker extends TileEntity implements ITickable, VibrationHandler.Listener {

    private static final SoundEvent[] WARNING_SOUNDS = {
            DeeperDepthsSoundEvents.WARDEN_NEARBY_CLOSE, DeeperDepthsSoundEvents.WARDEN_NEARBY_CLOSER,
            DeeperDepthsSoundEvents.WARDEN_NEARBY_CLOSEST, DeeperDepthsSoundEvents.WARDEN_LISTENING_ANGRY};

    private final VibrationHandler.Data vibrationData = new VibrationHandler.Data(this);
    private int spawnDelay = 0;
    private int warningLevel;

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
    public Vec3d getListenerPos() {
        return DirectionUtils.centerOf(pos);
    }

    @Override
    public int getRange() {
        return VibrationsConfig.sculkShriekerDetectionRange;
    }

    @Override
    public boolean canHear(@Nullable Entity entity, SoundEvent sound, double x, double y, double z) {
        return sound == DeeperDepthsSoundEvents.SCULK_SENSOR_CLICKING  && spawnDelay == 0 && ((WardenSpawnTracker.isOnCooldown(entity)));
    }

    @Override
    public void reactToSound(@Nullable Entity entity, SoundEvent sound, double x, double y, double z) {
        tryShriek(entity);
    }

    @Override
    public VibrationHandler.Data getVibrationData() {
        return vibrationData;
    }

    @Override
    public void update() {
        if (!(world instanceof WorldServer)) return;
        getVibrationData().tick((WorldServer) world);
        if (spawnDelay == 0) return;
        if (--spawnDelay == 0) {
            if (warningLevel == 0) return;
            //darkness warn and spawning
            if (warningLevel < 4 || (!trySpawnWarden())) {
                Random rand = world.rand;
                world.playSound(null, pos.add(rand.nextInt(20) - 10, rand.nextInt(20) - 10, rand.nextInt(20) - 10),
                        WARNING_SOUNDS[warningLevel - 1], SoundCategory.HOSTILE, 5, 1);
            }
            PotionEffect effect = new PotionEffect(DeeperDepthsPotions.DARKNESS, BlockConfig.sculkShriekerDarknessLength, 0, false, false);
            for (EntityPlayer player : world.getPlayers(EntityPlayer.class, player -> player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)
                    < BlockConfig.sculkShriekerDarknessRange * BlockConfig.sculkShriekerDarknessRange))
                player.addPotionEffect(effect);
            warningLevel = 0;
        }
    }

    public void tryShriek(@Nullable Entity entity) {
        if (spawnDelay > 0) return;
        if (entity == null) return;
        EntityPlayer player = null;
        if (entity instanceof EntityPlayer) player = (EntityPlayer) entity;
        for (EntityPlayer player1 : entity.getRecursivePassengersByType(EntityPlayer.class)) {
            player = player1;
            break;
        }
        if (player == null) return;
        if (world.getDifficulty() != EnumDifficulty.PEACEFUL) warningLevel = WardenSpawnTracker.tryWarn(world, pos, player);
        spawnDelay = BlockConfig.sculkShriekerSpawnDelay;
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockSculkShrieker &! state.getValue(BlockSculkShrieker.WATERLOGGED))
            world.playSound(null, pos, DeeperDepthsSoundEvents.SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS, 1.7F, (world.rand.nextFloat() * 0.4F) + 0.6F);
        for (int i = 0; i < 10; i++) DeeperDepths.proxy.spawnParticle(8, world, pos.getX() + 0.5f, pos.getY() + 0.4F, pos.getZ() + 0.5f, 0, 0.1, 0, 5 * i);
    }

    private boolean trySpawnWarden() {
        Random rand = world.rand;
        for (int i = 0; i < 20; ++i) {
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos.getX() + rand.nextInt(10) - 5, pos.getY() + 6, pos.getZ() + rand.nextInt(10) - 5);
            for (int j = 6; j > -6; j--) {
                mutable.setY(pos.getY() + j);
               if (!canSpawnAt(mutable)) continue;
               EntityWarden warden = new EntityWarden(world);
               warden.setPosition(mutable.getX() + 0.5, mutable.getY(), mutable.getZ() + 0.5);
               warden.setState(EntityWarden.State.EMERGING);
               world.spawnEntity(warden);
               return true;
            }
        }
        return false;
    }

    private boolean canSpawnAt(BlockPos pos) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos);
        mutable.setY(pos.getY() - 1);
        if (!world.isBlockFullCube(mutable)) return false;
        for (int i = 0; i < 3; i++) {
            mutable.setY(pos.getY() + i);
            if (!world.isAirBlock(mutable)) return false;
        }
        return true;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("spawn_delay", spawnDelay);
        compound.setInteger("warning_level", warningLevel);
        compound.setTag("vibration_data", vibrationData.writeToNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        spawnDelay = compound.getInteger("cooldown");
        warningLevel = compound.getInteger("warning_level");
        vibrationData.readFromNBT(compound.getTagList("vibration_data", 10));
    }

}
