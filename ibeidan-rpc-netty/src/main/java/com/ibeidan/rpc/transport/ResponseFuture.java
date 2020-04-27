package com.ibeidan.rpc.transport;

import com.ibeidan.rpc.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * @author lee
 * DATE 2020/4/26 15:01
 */
public class ResponseFuture {

    private final int requestId;
    private final CompletableFuture<Command> future;
    private final long timestamp;

    public ResponseFuture(int requestId, CompletableFuture<Command> future, long timestamp) {
        this.requestId = requestId;
        this.future = future;
        this.timestamp = timestamp;
    }

    public int getRequestId() {
        return requestId;
    }

    public CompletableFuture<Command> getFuture() {
        return future;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
