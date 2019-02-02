package com.server.umm.aplication.packet.message;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class BaseMessage {
    protected MessageType messageType;
}
