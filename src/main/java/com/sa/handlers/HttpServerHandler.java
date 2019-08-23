package com.sa.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.io.UnsupportedEncodingException;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            System.out.println("接收到数据");
            ByteBuf buf = (ByteBuf) msg;
            String message = getMessage(buf);
            System.out.println(message);
            ctx.writeAndFlush(getSendByteBuf("steve")); // 如果 msg 发送出去了，消息引用计数的减少
//        super.channelRead(ctx, msg);
//        ((ByteBuf) msg).release();
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("连接激活");
//    }
//
//    @Override
//    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channel 注册");
//    }
//
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channel 读取完成");
//    }
//
//    @Override
//    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channel 未注册");
//    }
//
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("连接取消");
//    }
//
//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        System.out.println("事件驱动");
//    }

    private String getMessage(ByteBuf buf) {
        System.out.println(buf.readableBytes());
        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        try {
            return new String(con, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ByteBuf getSendByteBuf(String message)
            throws UnsupportedEncodingException {

        byte[] req = message.getBytes("UTF-8");
        ByteBuf pingMessage = Unpooled.buffer();
        pingMessage.writeBytes(req);

        return pingMessage;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
