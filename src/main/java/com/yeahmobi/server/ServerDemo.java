package com.yeahmobi.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ServerDemo {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(Integer.parseInt(args[0]))
                .addService(new HelloWorldImpl()).build();

        System.out.println("Starting server...");
        server.start();
        System.out.println("ServerDemo started!");
        server.awaitTermination();
    }
}
