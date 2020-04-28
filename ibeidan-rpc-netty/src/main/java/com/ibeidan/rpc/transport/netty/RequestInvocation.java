package com.ibeidan.rpc.transport.netty;

import com.ibeidan.rpc.transport.RequestHandler;
import com.ibeidan.rpc.transport.RequestHandlerRegister;
import com.ibeidan.rpc.transport.command.Command;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lee
 * DATE 2020/4/26 17:04
 */
@ChannelHandler.Sharable
public class RequestInvocation extends SimpleChannelInboundHandler<Command> {
    private static final Logger logger = LoggerFactory.getLogger(RequestInvocation.class);

    private final RequestHandlerRegister requestHandlerRegister;

    public RequestInvocation(RequestHandlerRegister requestHandlerRegister) {
        this.requestHandlerRegister = requestHandlerRegister;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {
        RequestHandler handler = requestHandlerRegister.get(command.getHeader().getType());
        if (null != handler){
            Command respone = handler.handle(command);
            if (null != respone){
                channelHandlerContext.writeAndFlush(respone)
                        .addListener((ChannelFutureListener) channelFuture -> {
                            if (!channelFuture.isSuccess()){
                                logger.warn("Write response failed !",channelFuture.cause());
                                channelHandlerContext.channel().close();
                            }
                        });
            }else {
                  logger.warn("Response is null!");
            }
        }else {
            throw new Exception(String.format("No handler for request with type:%d!",command.getHeader().getType()));
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("Exception: ",cause);
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if (channel.isActive()){
            ctx.close();
        }
    }
}
