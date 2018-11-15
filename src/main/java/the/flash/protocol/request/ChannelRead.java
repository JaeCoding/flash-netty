package the.flash.protocol.request;

import io.netty.channel.ChannelHandlerContext;
import the.flash.protocol.Packet;

/**
 * 策略模式接口
 * 处理不同的Packet
 * @author Jae
 */
public interface ChannelRead {
    void doChannelRead(ChannelHandlerContext ctx, Packet packet);
}
