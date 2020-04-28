package com.ibeidan.rpc.transport.netty;

import com.ibeidan.rpc.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author lee
 * DATE 2020/4/26 17:04
 */
public class RequestDecoder extends CommandDecoder{
    @Override
    protected Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        return new Header(
                byteBuf.readInt(),
                byteBuf.readInt(),
                byteBuf.readInt());
    }


}
