package com.yeahmobi.client;

import com.yeahmobi.grpc.HelloRequest;
import com.yeahmobi.grpc.HelloResponse;
import com.yeahmobi.grpc.HelloWorldGrpc;
import com.yeahmobi.lb.NameResolverProvider;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.util.RoundRobinLoadBalancerFactory;

public class GrpcClient {
    public static void main(String[] args) throws InterruptedException {
        /**
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        HelloWorldGrpc.HelloWorldBlockingStub stub
                = HelloWorldGrpc.newBlockingStub(channel);
        **/
        ManagedChannel channel=ManagedChannelBuilder.forTarget("zk://localhost:444")
                .loadBalancerFactory(RoundRobinLoadBalancerFactory.getInstance())
                .nameResolverFactory(new NameResolverProvider())
                .usePlaintext()
                .build();
        HelloWorldGrpc.HelloWorldBlockingStub stub
                = HelloWorldGrpc.newBlockingStub(channel);
        try {
            int i=0;
            while(true) {
                HelloResponse helloResponse = stub.hello(HelloRequest.newBuilder()
                        .setFirstName("Baeldung")
                        .setLastName("gRPC"+(i++))
                        .build());

                System.out.println("Response received from server:\n" + helloResponse);
                Thread.sleep(1000);
            }
        } finally {
            channel.shutdown();
        }
    }
}
