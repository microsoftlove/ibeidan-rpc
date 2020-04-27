package com.ibeidan.rpc.serialize;

/**
 * @author lee
 * DATE 2020/4/23 15:51
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String message) {
        super(message);
    }

    public SerializeException(Throwable cause) {
        super(cause);
    }
}
