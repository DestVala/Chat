package com.chat.smm.packet.converter;

import com.chat.smm.network.TcpPacket;
import com.chat.smm.packet.message.*;
import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;


public class MessageConverter {
    private ObjectMapper objectMapper;

    public MessageConverter() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Method to conver udppacket to Json - Sylwester Garstecki
     **/
    public BaseMessage deserialize(TcpPacket data) throws IOException {

        //String convertedValue = Arrays.toString(data.getValue()); // to correct
        String convertedValue = new String(data.getValue());
        //System.out.println(convertedValue);
        BaseMessage result = null;

        if(data.getType() != null){
            switch (data.getType()) {
                case ingoing:
                    result = objectMapper.readValue(convertedValue, Ingoing.class);
                    break;
                case outgoing:
                    result = objectMapper.readValue(convertedValue, Outgoing.class);
                    break;
                case message:
                    result = objectMapper.readValue(convertedValue, TextMessage.class);
                    break;
                case read:
                    result = objectMapper.readValue(convertedValue, ReadMessage.class);
                    break;
            }
        }
        else{
            result = new EmptyType();
        }

        return result;
    }

    /**
     * Method to convert the message to a text form saved in the JSON format - Sylwester Garstecki
     **/
    public TcpPacket serialize(BaseMessage message) throws IOException {
        TcpPacket tcpPacket = new TcpPacket();
        String messageToJSON = objectMapper.writeValueAsString(message);
        MessageType messageType = null;

        if (message instanceof TextMessage) {
            messageType = MessageType.message;
        }
        if (message instanceof Ingoing) {
            messageType = MessageType.ingoing;
        }
        if (message instanceof Outgoing) {
            messageType = MessageType.outgoing;
        }
        if (message instanceof ReadMessage) {
            messageType = MessageType.read;
        }

        tcpPacket.setLength(messageToJSON.length());
        byte[] messageByte = messageToJSON.toString().getBytes();
        tcpPacket.setValue(messageByte);
        tcpPacket.setType(messageType);

        return new TcpPacket(tcpPacket.getType(), tcpPacket.getLength(), tcpPacket.getValue());
    }
}
