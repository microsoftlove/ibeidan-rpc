package com.ibeidan.rpc.server;

import com.ibeidan.rpc.hello.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lee
 * DATE 2020/4/28 16:07
 */
public class HelloServiceImpl implements HelloService {
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(String name) {
        logger.info("HelloServiceImpl收到：{}.",name);
        String ret = "Hello, " + name;
        logger.info("HelloServiceImpl返回：{}.",name);
        return ret;
    }
}
