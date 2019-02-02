package com.chat.smm.packet.message;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Builder
public class Outgoing extends BaseMessage {
    String nick;

    public Outgoing(String nick) {
        this.nick = nick;
        super.messageType = MessageType.outgoing;
    }

    public Outgoing() {
        super.messageType = MessageType.outgoing;
    }
}
