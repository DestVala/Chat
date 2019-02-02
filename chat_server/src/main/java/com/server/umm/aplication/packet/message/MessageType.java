package com.server.umm.aplication.packet.message;

import java.util.HashMap;
import java.util.Map;

public enum MessageType {
	ingoing((byte) 0),
	outgoing((byte) 1),
	message((byte) 2),
	read((byte) 3);

	private byte typeOfMessage;

	/***
	 *Methods of enum to change bytes into type message - Michal Ziolecki
	 */

	MessageType(byte typeOfMessage){
		this.typeOfMessage = typeOfMessage;
	}

	//serialize
	public byte getByteOfMessage() {
		return typeOfMessage;
	}

	/***
	 *Methods of enum to change  type message  into  bytes - Michal Ziolecki
	 */
	public static Map<Integer,MessageType> map = new HashMap <>(  );

	static {
		map.put( 0, ingoing );
		map.put( 1, outgoing );
		map.put( 2, message );
		map.put( 3, read );
	}

	//deserialize
	public static MessageType getTypeOfMessage(byte byteType){
		return map.get( (int) byteType );
	}
}
