package the.flash.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;

public class NettyServer {

    private static final int PORT = 8000;

    /**
     * 要启动一个Netty服务端，必须要指定三类属性，
     * 分别是线程模型、IO 模型、连接读写处理逻辑，
     * 有了这三者，之后在调用bind(8000)
     * @param args
     */
    public static void main(String[] args) {
        //bossGroup表示监听端口，accept 新连接的线程组（接活的妈妈桑）
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        //workerGroup表示  处理每一条 连接的数据读写 的线程组（干活的小姐）
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        //引导类:配置两大线程组
        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                // 1.指定线程模型
                .group(boosGroup, workerGroup)
                // 2.指定 IO 类型为 NIO
                .channel(NioServerSocketChannel.class)
                // 3.IO 处理逻辑
                /*
                option(): channel属性设置
                系统用于临时存放已完成三次握手的请求的队列的最大长度，
                如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                 */
                .option(ChannelOption.SO_BACKLOG, 1024)
                /*
                childOption()：连接设置TCP底层相关属性
                SO_KEEPALIVE开启TCP底层心跳机制
                TCP_NODELAY表示是否开启Nagle算法
                 */
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                /*
                定义后续每条连接的数据读写，业务处理逻辑
                childHandler()用于指定处理   新连接数据的读写处理逻辑
                NioSocketChannel就是 Netty 对 NIO 类型的连接的抽象  NioServerSocketChannel也是
                 */
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new ServerHandler());
                    }
                });

        bind(serverBootstrap, PORT);
    }

    /**
     * 异步机制来实现端口递增绑定
     */
    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        //bind(8000)返回值是一个ChannelFuture，再给它添加一个监听器
        serverBootstrap.bind(port).addListener(future -> {
            //监听端口是否绑定成功
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
                //失败则回调，自动绑定递增端口
                bind(serverBootstrap, port + 1);
            }
        });
    }
}
