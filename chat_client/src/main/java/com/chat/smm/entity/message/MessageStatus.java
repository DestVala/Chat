package com.chat.smm.entity.message;

public enum MessageStatus {
	waited("waited"),
	received("received"),
	read("read");

	private String status;

	MessageStatus(String status){
		this.status = status;
	}

	@Override
	public String toString() {
		return status;
	}
}
