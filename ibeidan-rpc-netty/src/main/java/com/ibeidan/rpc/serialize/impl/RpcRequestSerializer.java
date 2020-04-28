package com.ibeidan.rpc.serialize.impl;

import com.ibeidan.rpc.client.stubs.RpcRequest;
import com.ibeidan.rpc.serialize.Serializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author lee
 * DATE 2020/4/24 16:01
 */
public class RpcRequestSerializer implements Serializer<RpcRequest> {
    @Override
    public int size(RpcRequest entry) {
        return Integer.BYTES + entry.getInterFaceName().getBytes(StandardCharsets.UTF_8).length +
                Integer.BYTES + entry.getMethodName().getBytes(StandardCharsets.UTF_8).length +
                Integer.BYTES + entry.getSerializedArguments().length;
    }

    @Override
    public void serialize(RpcRequest entry, byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes,offset,length);
        byte [] tmpBytes  = entry.getInterFaceName().getBytes(StandardCharsets.UTF_8);
        buffer.putInt(tmpBytes.length);
        buffer.put(tmpBytes);

        tmpBytes = entry.getMethodName().getBytes(StandardCharsets.UTF_8);
        buffer.putInt(tmpBytes.length);
        buffer.put(tmpBytes);

        tmpBytes = entry.getSerializedArguments();
        buffer.putInt(tmpBytes.length);
        buffer.put(tmpBytes);
    }

    @Override
    public RpcRequest deserialize(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes,offset,length);
        int len = buffer.getInt();
        byte [] tmpBytes = new byte[len];
        buffer.get(tmpBytes);
        String interfaceName = new String(tmpBytes,StandardCharsets.UTF_8);

        len = buffer.getInt();
        tmpBytes= new byte[len];
        buffer.get(tmpBytes);
        String methodName = new String(tmpBytes,StandardCharsets.UTF_8);

        len = buffer.getInt();
        tmpBytes = new byte[len];
        buffer.get(tmpBytes);
        byte [] serialiezedArgs = tmpBytes;

        return new RpcRequest(interfaceName,methodName,serialiezedArgs);
    }

    @Override
    public byte type() {
        return Types.TYPE_RPC_REQUEST;
    }

    @Override
    public Class<RpcRequest> getSerializeClass() {
        return RpcRequest.class;
    }
}
