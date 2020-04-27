package com.ibeidan.rpc.transport;

import com.ibeidan.rpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lee
 * DATE 2020/4/26 15:00
 */
public class RequestHandlerRegister {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerRegister.class);
    private Map<Integer,RequestHandler> handlerMap =new HashMap<>();
    private static RequestHandlerRegister instance = null;
    public static RequestHandlerRegister getInstance(){
        if (null == instance) {
            instance = new RequestHandlerRegister();
        }
        return instance;
    }

    private RequestHandlerRegister(){
        Collection<RequestHandler> requestHandlers = ServiceSupport.loadAll(RequestHandler.class);
        for (RequestHandler requestHandler:requestHandlers) {
            handlerMap.put(requestHandler.type(),requestHandler);
            logger.info("Load request handler ,type ={}, class:{}.",requestHandler.type(),requestHandler.getClass().getCanonicalName());
        }
    }

    public RequestHandler get(int type){
        return handlerMap.get(type);
    }

}
