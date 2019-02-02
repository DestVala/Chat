package com.server.umm.aplication.packet.message;

import lombok.*;

@Builder
@Setter
@Getter
@ToString
public class ReadMessage extends BaseMessage {
    String sender;
    String recipient;

    public ReadMessage(String sender, String recipient) {
        this.sender = sender;
        this.recipient = recipient;
        super.messageType = MessageType.read;
    }
    public ReadMessage() {
        super.messageType = MessageType.read;
    }
}


