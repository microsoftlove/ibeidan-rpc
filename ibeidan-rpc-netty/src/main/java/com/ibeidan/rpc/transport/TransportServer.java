package com.ibeidan.rpc.transport;

/**
 * @author lee
 * DATE 2020/4/26 14:57
 */
public interface TransportServer {
    void start(RequestHandlerRegister requestHandlerRegister,int port) throws Exception;

    void stop();
}
