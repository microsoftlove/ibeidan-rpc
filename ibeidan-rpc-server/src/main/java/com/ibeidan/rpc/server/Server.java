package com.ibeidan.rpc.server;

import com.ibeidan.rpc.NameService;
import com.ibeidan.rpc.RpcAccessPoint;
import com.ibeidan.rpc.hello.HelloService;
import com.ibeidan.rpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Name;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * @author lee
 * DATE 2020/4/28 16:01
 */
public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        String serviceName = HelloService.class.getCanonicalName();
        File tmpIdrfile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tmpIdrfile,"ibeidan_rpc_name_service.data");
        HelloService helloService = new HelloServiceImpl();
        logger.info("创建并启动RpcAccessPoint...");
        try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class);
             Closeable ignored = rpcAccessPoint.startServer()){
            NameService nameService = rpcAccessPoint.getNameService(file.toURI());
            assert nameService != null;
            logger.info("向RpcAccessPoint注册{}服务...",serviceName);
            URI uri = rpcAccessPoint.addServiceProvier(helloService,HelloService.class);
            logger.info("服务名:{},向NameService注册...",serviceName);
            nameService.registerService(serviceName,uri);
            logger.info("开始提供服务，按任何键退出.");
            System.in.read();
            logger.info("Bye!");
        } catch (Exception e) {
            logger.error("Server exception :",e);
        }
    }
}
