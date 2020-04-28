package com.ibeidan;

import org.junit.Test;

/**
 * @author lee
 * DATE 2020/4/28 10:28
 */
public class TestStub {

    @Test
    public void testStub(){
        String str =
                "package com.ibeidan.rpc.client.stubs;\n" +
                "import com.ibeidan.rpc.serialize.SerializeSupport;\n"+
                "\n"+
                "public class %s extends AbstractStub implements %s {\n" +
                "   public String %s(String arg) {\n" +
                "       return SerializeSupport.parse(\n" +
                        "                invokeRemote(\n" +
                        "                new RpcRequest(\n"+
                        "                    \"%s\",\n" +
                        "                    \"%s\",\n" +
                        "                    SerializeSupport.serialize(arg))));\n"+
                     "}\n"+
                     "}";
        System.out.println(str);
        System.out.println();

        String stubSimpleName = com.ibeidan.Test.class.getSimpleName() + "Stub";
        String classFullName = com.ibeidan.Test.class.getName();
        String stubFullName = "com.ibeidan.rpc.client.stubs."+stubSimpleName;
        String methodName = com.ibeidan.Test.class.getMethods()[0].getName();
        String source = String.format(str,stubSimpleName,classFullName,methodName,classFullName,methodName);
        System.out.println(source);
    }


}
