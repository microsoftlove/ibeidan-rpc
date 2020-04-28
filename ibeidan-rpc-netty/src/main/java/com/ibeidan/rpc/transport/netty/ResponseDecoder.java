package com.ibeidan.rpc.transport.netty;

import com.ibeidan.rpc.transport.command.Header;
import com.ibeidan.rpc.transport.command.ResponseHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

/**
 * @author lee
 * DATE 2020/4/26 17:05
 */
public class ResponseDecoder extends CommandDecoder{
    @Override
    protected Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {

        int type  =byteBuf.readInt();
        int version = byteBuf.readInt();
        int requestId = byteBuf.readInt();
        int code = byteBuf.readInt();
        int errorLength = byteBuf.readInt();
        byte[] errorBytes = new byte[errorLength];
        byteBuf.readBytes(errorBytes);
        String error = new String(errorBytes, StandardCharsets.UTF_8);
        return new ResponseHeader(type,version,requestId,code,error);
    }
}
