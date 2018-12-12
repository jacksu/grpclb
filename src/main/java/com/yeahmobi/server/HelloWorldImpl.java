package com.yeahmobi.server;

import com.yeahmobi.grpc.*;
import io.grpc.stub.StreamObserver;

public class HelloWorldImpl extends HelloWorldGrpc.HelloWorldImplBase {
    @Override
    public void hello(
            HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        System.out.println("Request received from client:\n" + request);

        String greeting = new StringBuilder().append("Hello, ")
                .append(request.getFirstName())
                .append(" ")
                .append(request.getLastName())
                .toString();

        HelloResponse response = HelloResponse.newBuilder()
                .setGreeting(greeting)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void Ping(
            PingRequest request, StreamObserver<PingResponse> responseObserver) {
        System.out.println("Request received from client:\n" + request);
        PingResponse response = PingResponse.newBuilder()
                .setIsOk(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
