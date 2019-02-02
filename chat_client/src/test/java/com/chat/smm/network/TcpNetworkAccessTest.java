package com.chat.smm.network;

import com.chat.smm.packet.message.BaseMessage;
import com.chat.smm.packet.message.Outgoing;
import com.chat.smm.packet.message.TextMessage;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Michal Ziolecki.
 */
public class TcpNetworkAccessTest {
//    TcpNetworkAccess tcpNetworkAccess = new TcpNetworkAccess(  );
//    TcpSender sender = new TcpSender(  );
//
//    @Test
//    public void sendAndReceiveTextMessage() throws Exception {
//        tcpNetworkAccess.start();
//        TextMessage textMessageToSend = new TextMessage("1","FROM", "TO","Message");
//        tcpNetworkAccess.sendMessage( textMessageToSend );
//        Thread.sleep( 1000 );
//        //tutaj wywowalem geter do pola receiveMsg ale przypisanie nastepuje w receivemessage
//        BaseMessage receiveMessage = tcpNetworkAccess.getReceiveMsg(); //receivemessage jest null
//        Assert.assertEquals( "Send and receive message confrontation", textMessageToSend, receiveMessage );
//    }
//
//    @Test
//    public void sendAndReceiveOutGoingMessage() throws Exception {
//        tcpNetworkAccess.start();
//        Outgoing textMessageToSend = new Outgoing( "Nick" ); // drugi test na inna wiadomosc
//        tcpNetworkAccess.sendMessage( textMessageToSend );
//        Thread.sleep( 1000 );
//        // w tcpNetworkAccess wiadomosc jest nieodebrana (null) - sprawdzac z debugerem
//        BaseMessage receiveMessage = tcpNetworkAccess.getReceiveMsg();
//        Assert.assertEquals( "Send and receive message confrontation", textMessageToSend, receiveMessage );
//    }
}