package com.deeperdepths.common.entities;

import com.deeperdepths.common.items.DeeperDepthsItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityDDPainting extends EntityHanging implements IEntityAdditionalSpawnData {

    public EntityDDPainting(World worldIn) {
        super(worldIn);
    }

    public EntityDDPainting(World worldIn, BlockPos hangingPositionIn, EnumFacing facing) {
        super(worldIn, hangingPositionIn);
        this.updateFacingWithBoundingBox(facing);
    }

    @Override
    public int getWidthPixels() {
        return 48;
    }

    @Override
    public int getHeightPixels() {
        return 32;
    }

    @Override
    public void onBroken(@Nullable Entity brokenEntity) {
        if(this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.playSound(SoundEvents.ENTITY_PAINTING_BREAK, 1.0F, 1.0F);
            if (brokenEntity instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer)brokenEntity;

                if (entityplayer.capabilities.isCreativeMode) {
                    return;
                }
            }
        }
        this.entityDropItem(new ItemStack(DeeperDepthsItems.TRIAL_CHAMBERS_PAINTING.getDefaultInstance().getItem(), 1), 0.0F);
    }

    @Override
    public void playPlaceSound() {
        this.playSound(SoundEvents.ENTITY_PAINTING_PLACE, 1.0F, 1.0F);
    }

    @Override
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch)  {
        this.setPosition(x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.setPosition((double)this.hangingPosition.getX(), (double)this.hangingPosition.getY(), (double)this.hangingPosition.getZ());
    }

    @Override
    @Nonnull
    public ItemStack getPickedResult(RayTraceResult target)
    { return new ItemStack(DeeperDepthsItems.TRIAL_CHAMBERS_PAINTING); }

    @Override
    public void writeSpawnData(ByteBuf buf) {
        buf.writeLong(this.hangingPosition.toLong());
        buf.writeBoolean(this.facingDirection != null);
        if(this.facingDirection != null) {
            buf.writeInt(this.facingDirection.getIndex());
        }
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        this.hangingPosition = BlockPos.fromLong(buf.readLong());
        if(buf.readBoolean()) {
            this.facingDirection = EnumFacing.getFront(buf.readInt());
            this.updateFacingWithBoundingBox(this.facingDirection);
        }
    }
}
