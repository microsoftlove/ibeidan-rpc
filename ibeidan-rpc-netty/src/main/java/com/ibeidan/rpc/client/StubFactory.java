package com.ibeidan.rpc.client;


import com.ibeidan.rpc.transport.Transport;

public interface StubFactory {

    <T> T createStub(Transport transport,Class<T> serviceClass);

}
