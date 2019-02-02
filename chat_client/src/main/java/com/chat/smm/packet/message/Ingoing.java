package com.chat.smm.packet.message;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Builder
public class Ingoing extends BaseMessage{
	String userId;
	String nick;
	String status;
	UserStatus userStatus;

	public Ingoing(String userId, String nick, String status, UserStatus userStatus) {
		this.userId = userId;
		this.nick = nick;
		this.status = status;
		this.userStatus = userStatus;
		super.messageType = MessageType.ingoing;
	}
	public Ingoing() {
		super.messageType = MessageType.ingoing;
	}

}
