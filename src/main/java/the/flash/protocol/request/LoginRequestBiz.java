package the.flash.protocol.request;

import io.netty.channel.ChannelHandlerContext;
import the.flash.protocol.Packet;
import the.flash.protocol.response.LoginResponsePacket;

import java.util.Date;

public class LoginRequestBiz implements ChannelRead {
    @Override
    public Packet doChannelRead(ChannelHandlerContext ctx, Packet packet) {
        System.out.println(new Date() + ": 收到客户端登录请求……");
        // 登录流程
        LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;

        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(packet.getVersion());
        if (valid(loginRequestPacket)) {
            loginResponsePacket.setSuccess(true);
            System.out.println(new Date() + ": 登录 成功!");
        } else {
            loginResponsePacket.setReason("账号密码校验失败");
            loginResponsePacket.setSuccess(false);
            System.out.println(new Date() + ": 登录失败!");
        }
        return loginResponsePacket;
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}