package com.ibeidan.rpc.transport.command;

/**
 * @author lee
 * DATE 2020/4/26 15:58
 */
public class Header {

    private int requestId;

    private int version ;

    private int type;

    public Header() {
    }


    public Header(int type, int version, int requestId) {
        this.requestId = requestId;
        this.version = version;
        this.type = type;
    }

    public int length(){
        return Integer.BYTES + Integer.BYTES + Integer.BYTES;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
