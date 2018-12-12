package com.yeahmobi.lb;

import com.yeahmobi.grpc.HelloWorldGrpc;
import com.yeahmobi.grpc.PingRequest;
import com.yeahmobi.grpc.PingResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.net.URI;

public class NameResolverImpl extends NameResolverAbstract {

    public NameResolverImpl(URI targetUri) {
        super(targetUri);
    }

    @Override
    public void initServers() {
        servers.add("127.0.0.1:8080");
        servers.add("127.0.0.1:8081");
    }

    @Override
    public boolean isActive(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        HelloWorldGrpc.HelloWorldBlockingStub stub
                = HelloWorldGrpc.newBlockingStub(channel);
        PingResponse response =stub.ping(PingRequest.newBuilder().build());
        return response.getIsOk();
    }
}
