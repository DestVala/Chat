package com.chat.smm.network;

import com.chat.smm.packet.converter.MessageConverter;
import com.chat.smm.packet.converter.PacketConverter;
import com.chat.smm.packet.message.BaseMessage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class TcpReceiver extends Thread {
    Consumer<BaseMessage> consumer;
    private MessageConverter messageConverter = null;
    private PacketConverter packetConverter = null;
    private MulticastSocket socket = null;
    private InetAddress address = null;
    private byte[] receiveByteArray = null;
    private TcpPacket tcpPacket = null;
    private InputStream inFromServer;
    private Socket clientSocket;
    private DataOutputStream outToServer;
    /**
     * Basic constructor with default ip address and port /Michal Z
     */
    public TcpReceiver(Consumer<BaseMessage> consumer, InputStream inFromServer,
                       Socket clientSocket, DataOutputStream outToServer) {
        super();
        this.consumer = consumer;
        this.packetConverter = new PacketConverter();
        this.messageConverter = new MessageConverter();
        this.inFromServer = inFromServer;
        this.clientSocket = clientSocket;
        this.outToServer = outToServer;
    }

    /**
     * Constructor to set ip address and port /Michal Z
     */
    public TcpReceiver(Consumer<BaseMessage> consumer, String ipAddress, int portNumber) {
        super();
        this.consumer = consumer;
        this.packetConverter = new PacketConverter();
        this.messageConverter = new MessageConverter();
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
//        this.receiveByteArray = new byte[1500];
        while (true) {
            try {
                this.receiveByteArray = new byte[1500];
                inFromServer.read(receiveByteArray);
                tcpPacket = packetConverter.deserialize(receiveByteArray);
                BaseMessage message = messageConverter.deserialize(tcpPacket);
                consumer.accept(message);
            } catch (Exception e) {
//                e.printStackTrace();
                System.out.println("Zamknieto socket.");
                try {
                    inFromServer.close();
                    outToServer.close();
                    clientSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            }
        }
    }

}
