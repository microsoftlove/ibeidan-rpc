package com.ibeidan.rpc.client.stubs;

import com.ibeidan.rpc.client.RequestIdSupport;
import com.ibeidan.rpc.client.ServiceStub;
import com.ibeidan.rpc.client.ServiceTypes;
import com.ibeidan.rpc.serialize.SerializeSupport;
import com.ibeidan.rpc.transport.Transport;
import com.ibeidan.rpc.transport.command.Code;
import com.ibeidan.rpc.transport.command.Command;
import com.ibeidan.rpc.transport.command.Header;
import com.ibeidan.rpc.transport.command.ResponseHeader;

import java.util.concurrent.ExecutionException;

/**
 * @author lee
 * DATE 2020/4/27 18:58
 */
public abstract class AbstractStub implements ServiceStub {

    protected Transport transport;

    protected byte[] invokeRemote(RpcRequest rpcRequest){
        Header header = new Header(ServiceTypes.TYPE_RPC_REQUEST,1, RequestIdSupport.next());
        byte [] payload = SerializeSupport.serialize(rpcRequest);
        Command requestCommand = new Command(header,payload);
        try{
            Command responseCommand = transport.send(requestCommand).get();
            ResponseHeader responseHeader = (ResponseHeader) responseCommand.getHeader();

            if (responseHeader.getCode() == Code.SUCCESS.getCode()){
                return responseCommand.getPayload();
            } else {
                throw new Exception(responseHeader.getError());
            }
        } catch (ExecutionException e) {
           throw new RuntimeException(e.getCause());
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }


    @Override
    public void setTransport(Transport transport) {
        this.transport = transport;
    }
}
