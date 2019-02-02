package com.server.umm.aplication.serverTCP;

import com.server.umm.aplication.network.TcpNetworkAccess;
import com.server.umm.aplication.service.ServiceMessageDAO;
import com.server.umm.aplication.service.ServiceUserDAO;
import org.apache.logging.log4j.core.util.ExecutorServices;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ServerTCP implements InitializingBean {
    ExecutorService executorService = Executors.newFixedThreadPool(1);
    /**
     * Class to receive and send information about user and message
     * TODO - receive information and send inf.
     */
    private String ipAddress;
    private ServerSocket serverSocket;
    private Map<UUID, TcpNetworkAccess> mapThread;
    @Autowired
    private ServiceUserDAO serviceUserDAO;
    @Autowired
    private ServiceMessageDAO serviceMessageDAO;
//    private volatile List<ServerConnection> mapThread;

    public ServerTCP() throws IOException {
        this.mapThread = Collections.synchronizedMap(new HashMap<>());
        this.ipAddress = new String();
        if (ipAddress != null & !ipAddress.isEmpty()) {
            //backlog is length of queue
            this.serverSocket = new ServerSocket(6666, 50, InetAddress.getByName(ipAddress));
        } else {
            this.serverSocket = new ServerSocket(6666, 50, InetAddress.getLocalHost());
        }
    }

    public void listen() throws IOException {
        System.out.println("Starting TCP connection, waiting for a client...");

        while (true) {
            Socket clientSocket = this.serverSocket.accept();
            if (clientSocket.isConnected()) {
                //creating threadTCP with name as ipAddress
                System.out.println("Connected with client" + clientSocket.getInetAddress());
                try {
                    ServerConnection serverConnection =
                            new ServerConnection(clientSocket.getPort(),
                                    clientSocket, mapThread,serviceUserDAO, serviceMessageDAO);
//                    serverConnection.setServiceUserDAO(serviceUserDAO);
                    serverConnection.start();
                    System.out.println("run new threadTCP");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean stop() throws IOException {
        serverSocket.close();
        if (serverSocket.isClosed()) return true;
        else return false;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executorService.execute(() -> {
            try {
                listen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
