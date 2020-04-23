package com.ibeidan.rpc.spi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author lee
 * DATE 2020/4/22 15:45
 * SPI类加载器帮助类
 */
public class ServiceSupport {

    private final static Map<String,Object> singletonService = new HashMap<>();

    public synchronized static <S> S load(Class<S> service){
        return StreamSupport.stream(ServiceLoader.load(service).spliterator(),false)
                .map(ServiceSupport::singletonFilter)
                .findFirst().orElseThrow(ServiceLoadExcepion::new);
    }

    public synchronized static <S> Collection<S> loadAll(Class<S> service){

        return StreamSupport.stream(ServiceLoader.load(service).spliterator(),false)
                .map(ServiceSupport::singletonFilter).collect(Collectors.toList());
    }

    /**
     * 判定服务类是否加入单例注解
     * 是的返回单例，否则直接返回
     * @author libeibei
     * 2020/4/22 17:23
     * @param  service 判定传入的服务类
     * @return S
     **/
    private static <S> S singletonFilter(S service){
        if (service.getClass().isAnnotationPresent(Singleton.class)){
            String className = service.getClass().getCanonicalName();
            Object singletonInstance = singletonService.putIfAbsent(className,service);
            return singletonInstance == null ? service : (S) singletonInstance;
        }else {
            return service;
        }
    }
}
