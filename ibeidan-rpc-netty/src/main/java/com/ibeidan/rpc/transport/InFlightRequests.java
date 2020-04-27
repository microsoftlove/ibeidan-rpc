package com.ibeidan.rpc.transport;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author lee
 * DATE 2020/4/26 15:01
 */
public class InFlightRequests implements Closeable {
    private final static long TIMEOUT_SEC = 10L;
    private final Semaphore semaphore = new Semaphore(10);
    private final Map<Integer,ResponseFuture> futureMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledFuture scheduledFuture;

    public InFlightRequests() {
        this.scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(this::removeTimeoutFutures,
                TIMEOUT_SEC,TIMEOUT_SEC,TimeUnit.SECONDS);
    }

    private void removeTimeoutFutures(){
        futureMap.entrySet().removeIf(entry -> {
           if (System.nanoTime() - entry.getValue().getTimestamp() > TIMEOUT_SEC * 1000000000L){
               semaphore.release();
               return true;
           }else{
               return false;
           }
        });
    }

    public ResponseFuture remove(int requestId){
        ResponseFuture future = futureMap.remove(requestId);
        if (null != future){
            semaphore.release();
        }
        return future;
    }


    @Override
    public void close() throws IOException {
        scheduledFuture.cancel(true);
        scheduledExecutorService.shutdown();
    }
}
