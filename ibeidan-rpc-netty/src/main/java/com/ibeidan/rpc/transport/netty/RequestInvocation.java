package com.ibeidan.rpc.transport.netty;

import com.ibeidan.rpc.transport.RequestHandlerRegister;
import com.ibeidan.rpc.transport.command.Command;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lee
 * @DATE 2020/4/26 17:04
 */
public class RequestInvocation extends SimpleChannelInboundHandler<Command> {

    private static final Logger logger = LoggerFactory.getLogger(RequestInvocation.class);

    private final RequestHandlerRegister requestHandlerRegister;

    public RequestInvocation(RequestHandlerRegister requestHandlerRegister) {
        this.requestHandlerRegister = requestHandlerRegister;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {

    }
}
