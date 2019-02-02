package com.server.umm.aplication.network;

import com.server.umm.aplication.packet.converter.MessageConverter;
import com.server.umm.aplication.packet.converter.PacketConverter;
import com.server.umm.aplication.packet.message.BaseMessage;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.function.Consumer;

public class TcpReceiver extends Thread {
    Consumer<BaseMessage> consumer;
    private MessageConverter messageConverter = null;
    private PacketConverter packetConverter = null;
    private MulticastSocket socket = null;
    private InetAddress address = null;
    private byte[] receiveByteArray = null;
    TcpPacket tcpPacket = null;
    InputStream inFromServer;

    /**
     * Basic constructor with default ip address and port /Michal Z
     */
    public TcpReceiver(Consumer<BaseMessage> consumer, InputStream inFromServer) {
        super();
        this.consumer = consumer;
        this.packetConverter = new PacketConverter();
        this.messageConverter = new MessageConverter();
        this.inFromServer = inFromServer;
    }

    public TcpReceiver( InputStream inFromServer) {
        super();
        this.consumer = consumer;
        this.packetConverter = new PacketConverter();
        this.messageConverter = new MessageConverter();
        this.inFromServer = inFromServer;
    }

    /**
     * Constructor to set ip address and port /Michal Z
     */
    public TcpReceiver(Consumer<BaseMessage> consumer, String ipAddress, int portNumber) {
        super();
        this.consumer = consumer;
        this.packetConverter = new PacketConverter();
        this.messageConverter = new MessageConverter();
//		this.ipAddress = ipAddress;
//		this.portNumber = portNumber;
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
//                consumer.accept(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
