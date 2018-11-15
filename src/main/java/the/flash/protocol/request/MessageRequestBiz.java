package the.flash.protocol.request;

import io.netty.channel.ChannelHandlerContext;
import the.flash.protocol.Packet;
import the.flash.protocol.response.MessageResponsePacket;

import java.util.Date;

public class MessageRequestBiz implements ChannelRead {
    @Override
    public Packet doChannelRead(ChannelHandlerContext ctx, Packet packet) {
        // 客户端发来消息
        MessageRequestPacket messageRequestPacket = ((MessageRequestPacket) packet);

        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());
        messageResponsePacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");

        return messageResponsePacket;
    }
}