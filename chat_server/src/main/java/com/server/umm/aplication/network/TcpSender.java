package com.server.umm.aplication.network;


import com.server.umm.aplication.packet.converter.PacketConverter;

import java.io.DataOutputStream;
import java.io.IOException;

public class TcpSender {

	DataOutputStream outToServer;

	/**
	 * Basic constructor with default ip address and port /Michal Z
	 * */
	public TcpSender(DataOutputStream outToServer){
		this.outToServer = outToServer;
	}

	/**
	 * Method to send message - Sylwester Garstecki /Michal Z
	 * */

	public boolean sendMessage(TcpPacket packet){
		//convert TcpPacket to byte[]
		PacketConverter packetConverter = new PacketConverter();
		byte[] messageByteToSend = packetConverter.serialize(packet);
		boolean test = sendMessage( messageByteToSend );
		return test;
	}

	private boolean sendMessage(byte[] data){

		boolean test = false;

		try{
			this.outToServer.write(data);
			test = true;

		}catch(IOException e){
			e.getStackTrace();
		}

		return test;
	}

}
