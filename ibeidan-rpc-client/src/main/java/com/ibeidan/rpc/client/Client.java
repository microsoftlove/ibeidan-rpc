package com.ibeidan.rpc.client;

import com.ibeidan.rpc.NameService;
import com.ibeidan.rpc.RpcAccessPoint;
import com.ibeidan.rpc.hello.HelloService;
import com.ibeidan.rpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.FileLock;

/**
 * @author lee
 * DATE 2020/4/28 16:54
 */
public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        String serviceName = HelloService.class.getCanonicalName();
        File tmpDirFile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tmpDirFile,"ibeidan_rpc_name_service.data");
        String name = "Master MQ";
        try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class)){
            NameService nameService = rpcAccessPoint.getNameService(file.toURI());
            assert nameService  != null;
            URI uri = nameService.lookupService(serviceName);
            assert uri != null;
            logger.info("找到服务提供者{},服务提供者:{}.",serviceName,uri);
            HelloService helloService = rpcAccessPoint.getRemoteService(uri,HelloService.class);
            logger.info("请求服务，name:{}...",name);
            String response = helloService.hello(name);
            logger.info("收到响应:{}.",response);
        } catch (IOException e) {
            logger.error("Client Exception ：",e);
        }

    }
}
