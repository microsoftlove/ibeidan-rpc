package com.ibeidan.rpc.transport;

import com.ibeidan.rpc.transport.command.Command;

/**
 * @author libeibei
 * 2020/4/26 15:06
 * 请求处理器
 **/
public interface RequestHandler {

    /**
     * 处理请求
     * @author libeibei
     * 2020/4/26 15:08
     * @param  requestCommand 请求命令
     * @return com.ibeidan.rpc.transport.command.Command 响应命令
     **/
    Command handle(Command requestCommand);

    /**
     * 支持的请求类型
     * @author libeibei
     * 2020/4/26 15:07
     **/
    int type();
}
