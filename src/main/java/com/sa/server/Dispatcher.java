package com.sa.server;

import com.sa.server.handlers.demo.OneHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dispatcher {

    private int port;

    public void run(){
        EventLoopGroup bossGroup = new NioEventLoopGroup();     // accept new connection
        EventLoopGroup workerGroup = new NioEventLoopGroup();   // deal parent connection

        ServerBootstrap bootstrap = new ServerBootstrap();
//        bootstrap.group(bossGroup, workerGroup)
//                .channel(NioServerSocketChannel.class)
//                .childHandler(new SAChannelInitializer())
//                .option(ChannelOption.SO_BACKLOG, 1024) // 连接数
//                .childOption(ChannelOption.SO_KEEPALIVE, false);

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new OneHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 1024) // 连接数
                .childOption(ChannelOption.SO_KEEPALIVE, false);

        try {
            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new Dispatcher(1088).run(); // 还可以绑定不同的 NIC， 默认是all NICs
    }
}
