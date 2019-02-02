package com.server.umm.aplication;

import com.server.umm.aplication.controllers.RestApiController;
import com.server.umm.aplication.serverTCP.ServerTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@SpringBootApplication
public class ChatServer {

    @Autowired
    public static ServerTCP serverTCP;

    public static void main(String[] args) {
        SpringApplication.run(ChatServer.class, args);
//        try {
////            ServerTCP serverTCP = new ServerTCP();
////            serverTCP.listen();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
