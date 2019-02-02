package com.chat.smm.packet.converter;

import com.chat.smm.network.TcpPacket;
import com.chat.smm.packet.message.MessageType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Michal Ziolecki.
 */
public class PacketConverterTest {
    /**
     * Test to check two method from packetConverter in one time
     * */
    @Test
    public void deserializeAndSerialize() throws Exception {
        // create byte table
        byte [] valueInByte = "123".getBytes();
        // object to send and test
        TcpPacket tcpPacketToTest = TcpPacket.builder()
                .type( MessageType.ingoing )
                .length( valueInByte.length )
                .value( valueInByte )
                .build();
        System.out.println(tcpPacketToTest.toString());
        // instance to test
        PacketConverter packetConverter = new PacketConverter();
        byte[] tableOfUdpPacketInByte = packetConverter.serialize(tcpPacketToTest);
        System.out.println("Length return byte table: " + tableOfUdpPacketInByte.length);
        // test of outgoing and incoming tale of bytes - test for packed
        Assert.assertEquals(3+valueInByte.length, tableOfUdpPacketInByte.length);
        TcpPacket tcpPacketIncoming = packetConverter.deserialize( tableOfUdpPacketInByte );
        // test of returned packet
        System.out.println(tcpPacketIncoming.toString());
        Assert.assertEquals( tcpPacketToTest.getLength(), tcpPacketIncoming.getLength() );
        Assert.assertEquals(tcpPacketToTest, tcpPacketIncoming);
    }
}