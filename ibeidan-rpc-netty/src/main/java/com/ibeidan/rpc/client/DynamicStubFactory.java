package com.ibeidan.rpc.client;

import com.ibeidan.rpc.transport.Transport;
import com.itranswarp.compiler.JavaStringCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author lee
 * DATE 2020/4/27 18:58
 */
public class DynamicStubFactory implements StubFactory{

    private static final Logger logger = LoggerFactory.getLogger(DynamicStubFactory.class);

    private final static String STUB_SOURCE_TEMPLATE =
            "package com.ibeidan.rpc.client.stubs;\n" +
                    "import com.ibeidan.rpc.serialize.SerializeSupport;\n"+
                    "\n"+
                    "public class %s extends AbstractStub implements %s {\n" +
                    "   public String %s(String arg) {\n" +
                    "        RpcRequest rpcRequest = new RpcRequest(\"%s\",\"%s\",SerializeSupport.serialize(arg));\n" +
                    "        byte[] bytes = super.invokeRemote(rpcRequest);\n" +
                    "        return SerializeSupport.parse(bytes);\n"+
                    "   }\n"+
                    "}";



    public <T> T createStub(Transport transport,Class<T> serviceClass){
        try {
            //填充模板
            String stubSimpleName = serviceClass.getSimpleName() + "Stub";
            String classFullName = serviceClass.getName();
            String stubFullName = "com.ibeidan.rpc.client.stubs."+stubSimpleName;
            String methodName = serviceClass.getMethods()[0].getName();
            String source = String.format(STUB_SOURCE_TEMPLATE,stubSimpleName,classFullName,methodName,classFullName,methodName);

            logger.info("proxy source:\n{}",source);

            //编译源代码
            JavaStringCompiler compiler = new JavaStringCompiler();
            Map<String,byte[]> results  = compiler.compile(stubSimpleName + ".java",
                    source);
            //加载编译好的类
            Class<?> clazz = compiler.loadClass(stubFullName,results);

            //把Transport赋值给桩
            ServiceStub stubInstance = (ServiceStub) clazz.newInstance();
            stubInstance.setTransport(transport);
            //返回这个桩
            return (T) stubInstance;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


}
