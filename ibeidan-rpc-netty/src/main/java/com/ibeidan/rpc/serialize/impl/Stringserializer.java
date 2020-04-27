package com.ibeidan.rpc.serialize.impl;

import com.ibeidan.rpc.serialize.Serializer;

import java.nio.charset.StandardCharsets;

/**
 * @author lee
 * DATE 2020/4/24 15:18
 */
public class Stringserializer implements Serializer<String> {
    @Override
    public int size(String entry) {
        return entry.getBytes(StandardCharsets.UTF_8).length;
    }

    @Override
    public void serialize(String entry, byte[] bytes, int offset, int length) {

        byte [] strBytes  = entry.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(strBytes,0,bytes,offset,strBytes.length);

    }

    @Override
    public String deserialize(byte[] bytes, int offset, int length) {
        return new String(bytes,offset,length,StandardCharsets.UTF_8);
    }

    @Override
    public byte type() {
        return Types.TYPE_STRING;
    }

    @Override
    public Class<String> getSerializeClass() {
        return String.class;
    }
}
