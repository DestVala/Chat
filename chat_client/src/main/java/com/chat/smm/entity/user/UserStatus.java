package com.chat.smm.entity.user;

public enum UserStatus {
	available("available"),
	away("away"),
	notDisturb("notDisturb");

	private String status;
	UserStatus(String status){
		this.status = status;
	}

	@Override
	public String toString(){
		return status;
	}

}
