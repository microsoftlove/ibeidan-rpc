package com.ibeidan.rpc.transport;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.TimeoutException;

/**
 * @author lee
 * DATE 2020/4/26 14:58
 */
public interface TransportClient extends Closeable {
    Transport createTransport(SocketAddress address,long connectionTimeOut) throws InterruptedException, TimeoutException;

    @Override
    void close() throws IOException;
}
