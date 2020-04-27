package com.ibeidan.rpc.transport.netty;

import com.ibeidan.rpc.transport.RequestHandlerRegister;
import com.ibeidan.rpc.transport.TransportServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lee
 * DATE 2020/4/26 16:50
 */
public class NettyServer implements TransportServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private int port;

    private EventLoopGroup acceptEventGroup;

    private EventLoopGroup ioEventGroup;

    private Channel channel;

    private RequestHandlerRegister requestHandlerRegister;

    @Override
    public void start(RequestHandlerRegister requestHandlerRegister, int port) throws Exception {
        this.port = port;
        this.requestHandlerRegister = requestHandlerRegister;
        EventLoopGroup acceptEventGroup = newEnentLoopGroup();
        EventLoopGroup ioEventGroup = newEnentLoopGroup();
        ChannelHandler channelHandler = newChannelHandlerPipline();
        ServerBootstrap serverBootstrap = newBootstrap(channelHandler,acceptEventGroup,ioEventGroup);
        Channel channel = doBind(serverBootstrap);

        this.acceptEventGroup = acceptEventGroup;
        this.ioEventGroup = ioEventGroup;
        this.channel = channel;
    }

    private Channel doBind(ServerBootstrap serverBootstrap) throws InterruptedException {
        return serverBootstrap.bind(port)
                .sync()
                .channel();
    }

    private EventLoopGroup newEnentLoopGroup(){
        if (Epoll.isAvailable()){
            return new EpollEventLoopGroup();
        }else {
            return new NioEventLoopGroup();
        }
    }

    private ChannelHandler newChannelHandlerPipline(){
        return new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline()
                        .addLast(new RequestDecoder())
                        .addLast(new ResponseEncoder())
                        .addLast(new RequestInvocation(requestHandlerRegister));
            }
        };
    }

    private ServerBootstrap newBootstrap(ChannelHandler channelHandler,EventLoopGroup acceptEventGroup,
                                         EventLoopGroup ioEventGroup){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(Epoll.isAvailable() ? EpollServerSocketChannel.class: NioServerSocketChannel.class)
                .group(acceptEventGroup,ioEventGroup)
                .childHandler(channelHandler)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        return serverBootstrap;

    }

    @Override
    public void stop() {
        if (acceptEventGroup != null){
            acceptEventGroup.shutdownGracefully();
        }
        if (ioEventGroup != null){
            ioEventGroup.shutdownGracefully();
        }
        if (channel != null){
            channel.close();
        }

    }
}
