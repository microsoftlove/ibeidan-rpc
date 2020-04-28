package com.ibeidan.rpc.transport.netty;

import com.ibeidan.rpc.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author lee
 * DATE 2020/4/26 17:04
 */
public class RequestEncoder extends CommandEncoder{

    @Override
    protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header, ByteBuf byteBuf) throws Exception {
        super.encodeHeader(channelHandlerContext, header, byteBuf);
    }
}
