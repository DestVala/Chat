package com.chat.smm.packet.converter;

import com.chat.smm.network.TcpPacket;
import com.chat.smm.packet.message.MessageType;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class PacketConverter {

	/**
	 * Method to deserialize table od bytes to TcpPacket type - Michal Ziolecki
	 * */
	public TcpPacket deserialize(byte[] data) {

		// instatnt of tcpPacket - to return
		TcpPacket tcpPacket;

		// separate to type of udp packet
		ByteBuffer buffer = ByteBuffer.wrap(data);
		MessageType typeOfMessage = MessageType.getTypeOfMessage(buffer.get(0));
		short length = buffer.getShort(1);
		byte[] dataOfValue = Arrays.copyOfRange(data, 3, length + 3);

		//-- filling the instant of tcpPacket by a value
		tcpPacket = TcpPacket.builder()
				.type( typeOfMessage )
				.length( length )
				.value( dataOfValue )
				.build();

		return tcpPacket;
	}

	/**
	 * Method to serialize instant of TcpPacket into byte table - Michal Ziolecki
	 * */
	public byte[] serialize(TcpPacket message) {

		ByteBuffer buffer = ByteBuffer.allocate(message.getLength() + 3);
		buffer.put(message.getType().getByteOfMessage());
		buffer.putShort((short) message.getLength());
		buffer.put(message.getValue());

		//create and return table of bytes
		return buffer.array();
	}
}
