package com.sa.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.UnsupportedEncodingException;

public class Socks5ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

//        Socks5InitialRequestDecoder
        final ByteBuf f = ctx.alloc().buffer(4);
        f.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

        final ChannelFuture res = ctx.writeAndFlush(f);
        res.addListener((e) -> ctx.close());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("reveive");
        ctx.writeAndFlush(getSendByteBuf("steve"));
    }


    private ByteBuf getSendByteBuf(String message)
            throws UnsupportedEncodingException {

        byte[] req = message.getBytes("UTF-8");
        ByteBuf pingMessage = Unpooled.buffer();
        pingMessage.writeBytes(req);

        return pingMessage;
    }
}
