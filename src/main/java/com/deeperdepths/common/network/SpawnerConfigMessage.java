package com.deeperdepths.common.network;

import com.deeperdepths.client.ClientProxy;
import com.deeperdepths.common.blocks.tiles.TileTrialSpawner;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SpawnerConfigMessage implements IMessage {

    private BlockPos pos;
    private NBTTagCompound config, ominousConfig;

    public SpawnerConfigMessage() {}

    public SpawnerConfigMessage(BlockPos pos, TileTrialSpawner.Config config, TileTrialSpawner.Config ominousConfig) {
        this.pos = pos;
        this.config = config.writeToNBT();
        this.ominousConfig = ominousConfig.writeToNBT();
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        if (buf.isReadable()) pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        if (buf.isReadable()) config = ByteBufUtils.readTag(buf);
        if (buf.isReadable()) ominousConfig = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (pos != null) {
            buf.writeInt(pos.getX());
            buf.writeInt(pos.getY());
            buf.writeInt(pos.getZ());
        }
        if (config != null) ByteBufUtils.writeTag(buf, config);
        if (ominousConfig != null) ByteBufUtils.writeTag(buf, ominousConfig);
    }

    public IMessage process(MessageContext ctx) {
        if (ctx.side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(() -> ClientProxy.openSpawnerConfig(config, ominousConfig));
        else if (ctx.side == Side.SERVER) FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            if (pos == null || config == null || ominousConfig == null) return;
            TileEntity tile = ctx.getServerHandler().player.world.getTileEntity(pos);
            if (!(tile instanceof TileTrialSpawner)) return;
            ((TileTrialSpawner) tile).modifyConfigs(c -> c.readFromNBT(config), c -> c.readFromNBT(ominousConfig));
        });
        return null;
    }

}
