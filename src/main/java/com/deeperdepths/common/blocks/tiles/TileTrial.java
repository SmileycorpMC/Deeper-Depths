package com.deeperdepths.common.blocks.tiles;

import com.deeperdepths.common.network.NetworkHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class TileTrial extends TileEntity implements ITickable {

    public void configure(EntityPlayerMP player) {
        IMessage message = getConfigMessage();
        if (message != null) NetworkHandler.network.sendTo(message, player);
    }

    protected abstract IMessage getConfigMessage();

}
