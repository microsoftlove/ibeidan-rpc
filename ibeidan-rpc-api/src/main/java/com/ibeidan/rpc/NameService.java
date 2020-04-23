package com.ibeidan.rpc;


import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

/**
 * @author libeibei
 * 2020/4/22 15:49
 * 注册中心
 **/
public interface NameService {

    /**
     * 所有支持的协议
     * @return java.util.Collection<java.lang.String>
     **/
    Collection<String> supportedSchemes();

    /**
     * 连接注册中心
     * @param  nameServiceUri 注册中心地址
     **/
    void connect(URI nameServiceUri);

    /**
     * 注册服务
     * @param  serviceName 服务
     * @param  uri
     * @return void
     **/
    void registerService(String serviceName,URI uri) throws IOException ;

    /**
     * 查询服务地址
     * @param  serviceName 服务
     **/
    URI lookupService(String serviceName) throws IOException;
}
