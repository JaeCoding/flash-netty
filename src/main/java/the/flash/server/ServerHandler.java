package the.flash.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import the.flash.protocol.Packet;
import the.flash.protocol.PacketCodeC;
import the.flash.protocol.request.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static the.flash.protocol.command.Command.LOGIN_REQUEST;
import static the.flash.protocol.command.Command.MESSAGE_REQUEST;

/**
 * @author chao.yu
 * chao.yu@dianping.com
 * @date 2018/08/04 06:21.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static Map<Byte, Class<? extends ChannelRead>> packetTypeMap;

    static {
        packetTypeMap = new ConcurrentHashMap<>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestBiz.class);
        packetTypeMap.put(MESSAGE_REQUEST, MessageRequestBiz.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf requestByteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.INSTANCE.decode(requestByteBuf);
        //packet不同是表象，更深层次的不同是 对应的处理逻辑不同，所以将处理逻辑抽象成类
        packetTypeMap.forEach((type, classObject) -> {
            if (type.equals(packet.getCommand())) {
                try {
                    Packet resposePacket = classObject.newInstance().doChannelRead(ctx, packet);
                    ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), resposePacket);
                    ctx.channel().writeAndFlush(responseByteBuf);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
