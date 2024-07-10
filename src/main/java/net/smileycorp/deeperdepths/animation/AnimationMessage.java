package net.smileycorp.deeperdepths.animation;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.smileycorp.deeperdepths.common.DeeperDepths;

public class AnimationMessage implements IMessage {
    private int entityID;
    private int index;

    public AnimationMessage() {
    }

    public AnimationMessage(int entityID, int index) {
        this.entityID = entityID;
        this.index = index;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityID = buf.readInt();
        this.index = buf.readInt();

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeInt(this.index);
    }

    public static class Handler implements IMessageHandler<AnimationMessage, IMessage> {

        public Handler() {

        }



        @Override
        public IMessage onMessage(AnimationMessage message, MessageContext ctx) {
            DeeperDepths.proxy.handleAnimationPacket(message.entityID, message.index);
            return null;
        }
    }

}
