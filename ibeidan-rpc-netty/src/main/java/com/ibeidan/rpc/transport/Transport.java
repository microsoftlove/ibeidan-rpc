package com.ibeidan.rpc.transport;

import com.ibeidan.rpc.transport.command.Command;

import java.util.concurrent.CompletableFuture;

public interface Transport {

    /**
     * 发送请求命令
     * @author libeibei
     * 2020/4/26 15:05
     * @param  request 请求命令
     * @return java.util.concurrent.CompletableFuture<com.ibeidan.rpc.transport.command.Command>
     *     返回值是一个Future
     **/
    CompletableFuture<Command> send(Command request);
}
