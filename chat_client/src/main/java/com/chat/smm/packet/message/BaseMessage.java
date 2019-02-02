package com.chat.smm.packet.message;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class BaseMessage {
    protected MessageType messageType;
}
