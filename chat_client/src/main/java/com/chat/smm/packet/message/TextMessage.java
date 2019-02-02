package com.chat.smm.packet.message;

import com.chat.smm.entity.message.MessageStatus;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@Builder
public class TextMessage extends BaseMessage {
    String identifier;
    String sender;
    String receiver;
    String textMsg;
    Timestamp dateMsg;
    MessageStatus messageStatus;

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
