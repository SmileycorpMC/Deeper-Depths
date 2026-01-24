package com.deeperdepths.common.network;

import com.deeperdepths.client.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SyncDeathLocationMessage implements IMessage {

    private int player;
    private int dimension;
    private BlockPos pos;

    public SyncDeathLocationMessage() {}

    public SyncDeathLocationMessage(int player, int dimension, BlockPos pos) {
        this.player = player;
        this.dimension = dimension;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        player = buf.readInt();
        dimension = buf.readInt();
        pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(player);
        buf.writeInt(dimension);
        buf.writeLong(pos.toLong());
    }

    public IMessage process(MessageContext ctx) {
        if (ctx.side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(() -> ClientProxy.syncDeathLocation(player, dimension, pos));
        return null;
    }

    public static void send(EntityPlayer player, int dimension, BlockPos pos) {
        send(player, -1, dimension, pos);
    }

    public static void send(EntityPlayer player, int id, int dimension, BlockPos pos) {
        NetworkHandler.network.sendTo(new SyncDeathLocationMessage(id, dimension, pos),
                (EntityPlayerMP) player);
    }

    public static void sendTracking(EntityPlayer player, int dimension, BlockPos pos) {
        NetworkHandler.network.sendToAllTracking(new SyncDeathLocationMessage(player.getEntityId(), dimension, pos), player);
    }


}
