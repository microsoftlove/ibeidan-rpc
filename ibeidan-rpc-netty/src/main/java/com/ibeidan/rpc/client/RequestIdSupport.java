package com.ibeidan.rpc.client;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lee
 * DATE 2020/4/27 18:58
 */
public class RequestIdSupport {

    private final static AtomicInteger nextRequestId = new AtomicInteger(0);

    public static int next(){
        return nextRequestId.getAndIncrement();
    }


}
