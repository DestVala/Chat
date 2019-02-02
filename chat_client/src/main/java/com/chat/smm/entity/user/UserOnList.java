package com.chat.smm.entity.user;

import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class UserOnList {

    private UUID userId;
    private String nick;
    private boolean logStatus;
    private HashMap<Boolean, String> logInformation;
//    private User user;

    public UserOnList(User user){
//        this.user = user;
        this.userId = user.getUserId();
        this.nick = user.getNick();
        this.logStatus = user.isLogStatus();
        this.logInformation = new HashMap<>();
        this.logInformation.put(true, "online");
        this.logInformation.put(false, "offline");
    }

    @Override
    public String toString() {
        return "" + nick + "\n" + logInformation.get(logStatus);
    }
}
