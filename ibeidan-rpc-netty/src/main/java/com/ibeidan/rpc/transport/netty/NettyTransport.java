package com.ibeidan.rpc.transport.netty;

import com.ibeidan.rpc.transport.InFlightRequests;
import com.ibeidan.rpc.transport.ResponseFuture;
import com.ibeidan.rpc.transport.Transport;
import com.ibeidan.rpc.transport.command.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

/**
 * @author lee
 * DATE 2020/4/26 17:03
 */
public class NettyTransport implements Transport {

    private final Channel channel;

    private final InFlightRequests inFlightRequests;



    public NettyTransport(Channel channel, InFlightRequests inFlightRequests) {

        this.channel = channel;
        this.inFlightRequests = inFlightRequests;
    }

    @Override
    public CompletableFuture<Command> send(Command request) {
        //构建返回值
        CompletableFuture<Command> completableFuture = new CompletableFuture<>();
        //将在途请求放到inFlightRequests中
        try {
            inFlightRequests.put(new ResponseFuture(request.getHeader().getRequestId(),completableFuture));
            //发送命令
            channel.writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> {
                //处理发送失败的情况
                if (!channelFuture.isSuccess()){
                    completableFuture.completeExceptionally(channelFuture.cause());
                    channel.close();
                }
            });

        } catch (Throwable throwable) {
            //处理发送异常
            inFlightRequests.remove(request.getHeader().getRequestId());
            completableFuture.completeExceptionally(throwable);
        }
        return completableFuture;
    }
}
