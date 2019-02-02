package com.server.umm.aplication.network;

import com.server.umm.aplication.packet.message.MessageType;
import lombok.*;

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
