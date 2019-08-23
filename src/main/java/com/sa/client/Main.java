package com.sa.client;

import com.sa.init.SAChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Main {

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();     // accept new connection
        EventLoopGroup workerGroup = new NioEventLoopGroup();   //

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
//                .childHandler(new ChannelInitializer<SocketChannel>() {
//                    protected void initChannel(SocketChannel socketChannel) throws Exception {
//                        ChannelPipeline p = socketChannel.pipeline();
//                        p.addLast(new HttpServerHandler());
//                    }
//                })
                .childHandler(new SAChannelInitializer())
                .option(ChannelOption.SO_BACKLOG, 128) // 连接数
                .option(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, false);

        try {
            ChannelFuture f = bootstrap.bind(1087).sync();
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
