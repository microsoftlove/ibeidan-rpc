package com.ibeidan.rpc;

import com.ibeidan.rpc.spi.ServiceSupport;

import java.io.Closeable;
import java.net.URI;
import java.util.Collection;

/**
 * @author libeibei
 * 2020/4/22 15:50
 * RPC框架对外提供的服务接口
 **/
public interface RpcAccessPoint extends Closeable {
    /**
     * 客户端获取远程服务的引用
     * @author libeibei
     * 2020/4/22 16:02
     * @param  uri 远程服务地址
     * @param  serviceClass 服务的接口累的class
     * @param <T> 服务接口的类型
     * @return T 远程服务的引用
     **/
    <T> T getRemoteService(URI uri,Class<T> serviceClass);

    /**
     * 服务端注册服务的实现实例
     * @author libeibei
     * 2020/4/22 16:07
     * @param  service 实现实例对象
     * @param  serviceClass 服务接口的类型
     * @return java.net.URI 服务地址
     **/
    <T> URI addServiceProvier(T service,Class<T> serviceClass);

    /**
     * 获取注册中心的引用
     * @author libeibei
     * 2020/4/23 09:54
     * @param  nameserviceUri 注册中心URI
     * @return 注册中心引用
     **/
    default NameService getNameService(URI nameserviceUri) {
         Collection<NameService> nameServices = ServiceSupport.loadAll(NameService.class);
        for (NameService nameService:nameServices) {
            if (nameService.supportedSchemes().contains(nameserviceUri.getScheme())){
                nameService.connect(nameserviceUri);
            }
        }
        return null;
    }

    /**
     * 服务端启动RPC框架，监听接口，开始提供远程服务
     * @author libeibei
     * 2020/4/23 09:56
     * @return java.io.Closeable 服务实例，用于程序停止的时候安全关闭服务
     **/
    Closeable startServer() throws Exception;

}
