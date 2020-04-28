package com.ibeidan.rpc.transport.command;

/**
 * @author lee
 * DATE 2020/4/26 15:05
 */
public class Command {

    protected Header header;

    private byte [] payload;

    public Command(Header header, byte[] payload) {
        this.header = header;
        this.payload = payload;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
