package com.ibeidan.rpc;

import com.ibeidan.rpc.client.StubFactory;
import com.ibeidan.rpc.server.ServiceProviderRegistry;
import com.ibeidan.rpc.spi.ServiceSupport;
import com.ibeidan.rpc.transport.RequestHandlerRegister;
import com.ibeidan.rpc.transport.Transport;
import com.ibeidan.rpc.transport.TransportClient;
import com.ibeidan.rpc.transport.TransportServer;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author lee
 *  2020/4/22 10:22
 */
public class NettyRpcAccessPoint implements RpcAccessPoint{

    private final String host = "localhost";

    private final int port = 9999;

    private final URI uri = URI.create("rpc://"+host +":"+port);

    private TransportServer server = null;

    private TransportClient client = ServiceSupport.load(TransportClient.class);

    private final Map<URI, Transport> clientMap = new ConcurrentHashMap<>();

    private final StubFactory stubFactory = ServiceSupport.load(StubFactory.class);

    private final ServiceProviderRegistry serviceProviderRegistry = ServiceSupport.load(ServiceProviderRegistry.class);


    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {
        Transport transport = clientMap.computeIfAbsent(uri,this::createTransport);
        return stubFactory.createStub(transport,serviceClass);
    }

    private Transport createTransport(URI uri){
        try {
            return client.createTransport(new InetSocketAddress(uri.getHost(),uri.getPort()),30000L);
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public <T> URI addServiceProvier(T service, Class<T> serviceClass) {
        serviceProviderRegistry.addServiceProvider(serviceClass,service);
        return uri;
    }

    @Override
    public synchronized Closeable startServer() throws Exception {
        if (null == server){
            server = ServiceSupport.load(TransportServer.class);
            server.start(RequestHandlerRegister.getInstance(),port);
        }
        return () -> {
            if (null != server){
                server.stop();
            }
        };
    }

    @Override
    public void close() throws IOException {
        if (null != server) {
            server.stop();
        }
        client.close();
    }
}
