package com.ibeidan.rpc.transport.netty;

import com.ibeidan.rpc.transport.InFlightRequests;
import com.ibeidan.rpc.transport.Transport;
import com.ibeidan.rpc.transport.TransportClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @author lee
 * DATE 2020/4/26 17:03
 */
public class NettyClient implements TransportClient {
    private EventLoopGroup ioEventGroup;
    private Bootstrap bootstrap;
    private final InFlightRequests inFlightRequests;
    private List<Channel> channels = new LinkedList<>();

    public NettyClient() {
        this.inFlightRequests = new InFlightRequests();
    }

    private Bootstrap newBootstrap(ChannelHandler channelHandler,EventLoopGroup ioEventGroup){
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                .group(ioEventGroup)
                .handler(channelHandler)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        return bootstrap;
    }


    @Override
    public Transport createTransport(SocketAddress address, long connectionTimeOut) throws InterruptedException, TimeoutException {
        return new NettyTransport(createChannel(address,connectionTimeOut),inFlightRequests);
    }

    private synchronized Channel createChannel(SocketAddress address,long conectionTimeout) throws InterruptedException,TimeoutException{
        if (address == null){
            throw new IllegalArgumentException("address must not be null !");
        }
        if (ioEventGroup == null){
           ioEventGroup = newIoEventGroup();
        }
        if (bootstrap == null){
            ChannelHandler channelHandler = newChannelHandlerPipeline();
            bootstrap = newBootstrap(channelHandler,ioEventGroup);
        }
        ChannelFuture channelFuture;
        Channel channel;
        channelFuture = bootstrap.connect(address);
        if (!channelFuture.await(conectionTimeout)){
            throw new TimeoutException();
        }
        channel = channelFuture.channel();
        if (channel == null || !channel.isActive()){
            throw new IllegalStateException();
        }
        channels.add(channel);
        return channel;
    }

    private ChannelHandler newChannelHandlerPipeline(){
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline()
                        .addLast(new ResponseDecoder())
                        .addLast(new RequestEncoder())
                        .addLast(new ResponseInvocation(inFlightRequests));
            }
        };
    }

    private EventLoopGroup newIoEventGroup(){
        if (Epoll.isAvailable()){
            return new EpollEventLoopGroup();
        }else {
            return new NioEventLoopGroup();
        }
    }

    @Override
    public void close() throws IOException {

        for (Channel c: channels) {
            if (null != c){
                c.close();
            }
        }
        if (ioEventGroup !=null){
            ioEventGroup.shutdownGracefully();
        }
        inFlightRequests.close();

    }




}
