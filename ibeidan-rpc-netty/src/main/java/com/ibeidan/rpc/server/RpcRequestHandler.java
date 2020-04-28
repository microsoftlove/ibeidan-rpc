package com.ibeidan.rpc.server;

import com.ibeidan.rpc.client.ServiceTypes;
import com.ibeidan.rpc.client.stubs.RpcRequest;
import com.ibeidan.rpc.serialize.SerializeSupport;
import com.ibeidan.rpc.spi.Singleton;
import com.ibeidan.rpc.transport.RequestHandler;
import com.ibeidan.rpc.transport.command.Code;
import com.ibeidan.rpc.transport.command.Command;
import com.ibeidan.rpc.transport.command.Header;
import com.ibeidan.rpc.transport.command.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lee
 * DATE 2020/4/27 17:48
 */
@Singleton
public class RpcRequestHandler implements RequestHandler ,ServiceProviderRegistry {
    private static final Logger logger = LoggerFactory.getLogger(RpcRequestHandler.class);

    private Map<String/*service name*/,Object/*service provider*/> serviceProviders = new HashMap<>();


    @Override
    public <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider) {
        serviceProviders.put(serviceClass.getCanonicalName(),serviceProvider);
        logger.info("Add service :{},provider:{}",serviceClass.getCanonicalName(),
                serviceProvider.getClass().getCanonicalName());
    }

    @Override
    public Command handle(Command requestCommand) {
        Header header =  requestCommand.getHeader();
        //从payload中反序列化RPCRequest
        RpcRequest rpcRequest = SerializeSupport.parse(requestCommand.getPayload());
        try {
            //查找所有已注册的服务提供方，寻找rpcRequest中需要的服务
            Object serviceProvider = serviceProviders.get(rpcRequest.getInterFaceName());
            if (serviceProvider != null){
                //找到服务提供者，利用Java的反射机制调用服务的对应方法
                String arg = SerializeSupport.parse(rpcRequest.getSerializedArguments());
                Method method = serviceProvider.getClass().getMethod(rpcRequest.getMethodName(),String.class);
                String result = (String) method.invoke(serviceProvider,arg);
                //把结果封装成响应命令并返回
                return new Command(new ResponseHeader(type(),header.getVersion(),header.getRequestId()), SerializeSupport.serialize(result));
              }
              //如果没有找到，返回NO_PROVIDER错误响应
            logger.warn("No service provider of {}#{}(String)!",rpcRequest.getInterFaceName(),rpcRequest.getMethodName());
            return new Command(new ResponseHeader(type(),header.getVersion(),header.getRequestId(),Code.NO_PROVIDER.getCode(),"No provider!"),new byte[0]);
        } catch (Throwable t) {
           //发生异常，返回UNKNOW_ERROR错误响应。
            logger.warn("Exception: ",t);
            return new Command(new ResponseHeader(type(),header.getVersion(),header.getRequestId(),Code.UNKNOWN_ERROR.getCode(),t.getMessage()),new byte[0]);
        }
    }

    @Override
    public int type() {
        return ServiceTypes.TYPE_RPC_REQUEST;
    }
}
