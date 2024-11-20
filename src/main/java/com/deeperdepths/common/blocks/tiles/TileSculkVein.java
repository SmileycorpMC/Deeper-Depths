package com.deeperdepths.common.blocks.tiles;

import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.smileycorp.atlas.api.util.Func;

import javax.annotation.Nullable;
import java.util.EnumMap;

public class TileSculkVein extends TileEntity {
    
    private final EnumMap<EnumFacing, Boolean> FACINGS = Maps.newEnumMap(EnumFacing.class);
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        for (EnumFacing facing : EnumFacing.values()) if (compound.hasKey(facing.getName()))
            FACINGS.put(facing, compound.getBoolean(facing.getName()));
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        for (EnumFacing facing : EnumFacing.values()) compound.setBoolean(facing.getName(), FACINGS.computeIfAbsent(facing, Func::False));
        return super.writeToNBT(compound);
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }
    
    @Override
    public void handleUpdateTag(NBTTagCompound nbt) {
        readFromNBT(nbt);
    }
    
    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
        world.markBlockRangeForRenderUpdate(pos, pos);
    }
    
    public void setFacing(EnumFacing facing, boolean enabled) {
        FACINGS.put(facing, enabled);
        if (world != null) world.markBlockRangeForRenderUpdate(pos, pos);
    }
    
    public boolean hasFacing(EnumFacing facing) {
        return FACINGS.computeIfAbsent(facing, Func::False);
    }
    
}
