package com.server.umm.aplication.serverTCP;

import com.server.umm.aplication.network.TcpNetworkAccess;
import com.server.umm.aplication.network.TcpPacket;
import com.server.umm.aplication.network.TcpSender;
import com.server.umm.aplication.packet.converter.MessageConverter;
import com.server.umm.aplication.packet.converter.PacketConverter;
import com.server.umm.aplication.packet.message.BaseMessage;
import com.server.umm.aplication.packet.message.Ingoing;
import com.server.umm.aplication.packet.message.TextMessage;
import com.server.umm.aplication.service.ServiceMessageDAO;
import com.server.umm.aplication.service.ServiceUserDAO;
import lombok.Setter;

import java.io.*;
import java.net.Socket;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class ServerConnection extends Thread{

    private Socket clientSocket;
    private InputStream inFromClient;
    private DataOutputStream outToClient;
    private byte[] receiveByteArray = null;
    private Map<UUID, TcpNetworkAccess> mapThread;
    private MessageConverter messageConverter = null;
    private PacketConverter packetConverter = null;
    TcpPacket tcpPacket = null;
    TcpNetworkAccess tcpNetworkAccess;
    TcpSender tcpSender;
    private UUID uuid;

    public ServerConnection(int threadForUser, Socket clientSocket,
                            Map<UUID, TcpNetworkAccess> mapThread, ServiceUserDAO serviceUserDAO, ServiceMessageDAO serviceMessageDAO) throws IOException {
        super(""+threadForUser);
        this.mapThread = mapThread;
        this.clientSocket = clientSocket;
        this.inFromClient = clientSocket.getInputStream();
        this.outToClient = new DataOutputStream(clientSocket.getOutputStream());
        this.packetConverter = new PacketConverter();
        this.messageConverter = new MessageConverter();
        this.tcpNetworkAccess = new TcpNetworkAccess(clientSocket, mapThread, serviceUserDAO, serviceMessageDAO);
        this.tcpSender = new TcpSender(outToClient);
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        while(true){
            try {
                this.receiveByteArray = new byte[1500];
                inFromClient.read(receiveByteArray);
                tcpPacket = packetConverter.deserialize(receiveByteArray);
                BaseMessage message = messageConverter.deserialize(tcpPacket);
                tcpNetworkAccess.receiveMessage(message);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Zamknieto socket uzytkownika: " + this.uuid);
                try {
                    inFromClient.close();
                    outToClient.close();
                    clientSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            }
        }
        mapThread.remove(this.uuid);
    }
}

