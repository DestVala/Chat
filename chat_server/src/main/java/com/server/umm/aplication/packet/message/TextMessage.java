package com.server.umm.aplication.packet.message;

import com.server.umm.aplication.entity.message.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
public class TextMessage extends BaseMessage {
    String identifier;
    String sender;
    String receiver;
    String textMsg;
    MessageStatus messageStatus;
    Timestamp dateMsg;

    public TextMessage(String identifier, String sender, String receiver, String textMsg) {
        this.identifier = identifier;
        this.sender = sender;
        this.receiver = receiver;
        this.textMsg = textMsg;
        this.messageStatus = MessageStatus.waited;
        super.messageType = MessageType.message;
    }

    public TextMessage() {
        super.messageType = MessageType.message;
    }
}
