package com.chat.smm.packet.message;

import com.chat.smm.entity.message.MessageStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class MessageConfirmation  extends BaseMessage{
    String msgid;
    MessageStatus messageStatus;

    public MessageConfirmation(String msgid, MessageStatus messageStatus) {
        this.msgid = msgid;
        this.messageStatus = messageStatus;
    }
}
