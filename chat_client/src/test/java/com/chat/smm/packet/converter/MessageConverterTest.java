package com.chat.smm.packet.converter;

import com.chat.smm.network.TcpPacket;
import com.chat.smm.packet.message.BaseMessage;
import com.chat.smm.packet.message.TextMessage;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class MessageConverterTest {

    @Test
    public void deserializeAndSerialize() throws IOException{
        TextMessage textMessage = new TextMessage("1","FROM", "TO","Message");
        MessageConverter messageConverter = new MessageConverter();
        TcpPacket packet = messageConverter.serialize(textMessage);

        BaseMessage deserializedMessage = messageConverter.deserialize(packet);
        Assert.assertEquals(textMessage,deserializedMessage);

    }

}