package com.sa.client.handlers.demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class OneHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        final int readerIndex = in.readerIndex();
        if (in.writerIndex() == readerIndex) {
            return;
        }

        ChannelPipeline p = ctx.pipeline();
        final byte versionVal = in.readByte();
        log.info("version: {}", versionVal);
//        SocksVersion version = SocksVersion.valueOf(versionVal);
        final byte nmethods = in.readByte();
        log.info("nmethods numer: {}", nmethods);
        byte[] methods = new byte[nmethods];
        for(int i=0; i < nmethods; i++){
            methods[i] = in.readByte();
        }
        if(in.readableBytes() > 0) {
            log.info("left data ");
//            ReferenceCountUtil.release(in);
            in.skipBytes(in.readableBytes());
            ctx.close();
            return;
        }else{
            ctx.close();
            return;
        }
    }
}
