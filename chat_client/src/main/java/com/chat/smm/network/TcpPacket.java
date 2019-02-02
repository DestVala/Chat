package com.chat.smm.network;

import lombok.*;
import com.chat.smm.packet.message.MessageType;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TcpPacket {
	private MessageType type;
	private int length;
	private byte[] value;
}
