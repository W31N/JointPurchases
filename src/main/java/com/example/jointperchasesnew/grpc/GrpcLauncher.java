package com.example.jointperchasesnew.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class  GrpcLauncher implements CommandLineRunner {

    private final GroupPurchaseGrpcService groupPurchaseGrpcService;

    @Autowired
    public GrpcLauncher(GroupPurchaseGrpcService groupPurchaseGrpcService) {
        this.groupPurchaseGrpcService = groupPurchaseGrpcService;
    }

    @Override
    public void run(String... args) throws Exception {
        Server server = ServerBuilder
                .forPort(8088)
                .addService(groupPurchaseGrpcService)
                .build();

        server.start();
        System.out.println("gRPC Server started on port 8088");
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        server.awaitTermination();
    }
}
