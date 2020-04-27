package com.ibeidan.rpc.client.stubs;

/**
 * @author lee
 * DATE 2020/4/24 16:02
 */
public class RpcRequest {

    private final String interFaceName;
    private final String methodName;
    private final byte [] serializedArguments;

    public RpcRequest(String interFaceName, String methodName, byte[] serializedArguments) {
        this.interFaceName = interFaceName;
        this.methodName = methodName;
        this.serializedArguments = serializedArguments;
    }

    public String getInterFaceName() {
        return interFaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public byte[] getSerializedArguments() {
        return serializedArguments;
    }
}
