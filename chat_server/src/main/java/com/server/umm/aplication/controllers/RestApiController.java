package com.server.umm.aplication.controllers;


import com.server.umm.aplication.entity.message.Message;
import com.server.umm.aplication.entity.message.MessageStatus;
import com.server.umm.aplication.entity.user.User;
import com.server.umm.aplication.exceptions.EntityExistException;
import com.server.umm.aplication.service.ServiceMessageDAO;
import com.server.umm.aplication.service.ServiceUserDAO;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.tomcat.jni.Thread;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class RestApiController   {

    ServiceMessageDAO serviceMessageDAO;
    ServiceUserDAO serviceUserDAO;

    @Autowired
    public RestApiController(ServiceMessageDAO serviceMessageDAO, ServiceUserDAO serviceUserDAO) {
        this.serviceMessageDAO = serviceMessageDAO;
        this.serviceUserDAO = serviceUserDAO;
    }


    @RequestMapping(value = {"/login"})
    public String logIn(@RequestParam(required = true) String nick, @RequestParam(required = true) String password){
        if(checkCorrectLoginTry(nick,password)){
            User user = serviceUserDAO.getUserByNick(nick);
            user.setLogStatus(true);
            return user.getUserId().toString();
        }
        return "";
    }

    @RequestMapping(value = {"/loginTrue"})
    public boolean logInTrue(@RequestParam(required = true) String nick){
        serviceUserDAO.updateLogStatusUser(nick);
        return true;
    }

    public boolean checkCorrectLoginTry(String nick, String password){
        return serviceUserDAO.getUserByNickAndUserPassword(nick,password);
    }

    @RequestMapping(value = {"/logout"})
    public String logOut(@RequestParam(required = true) String nick){
        serviceUserDAO.updateLogoutStatusUser(nick);
        return "Log out success.";
    }

    @RequestMapping(value = {"/textStatus"})
    public String changeTextStatus(@RequestBody User user){
        serviceUserDAO.updateUser(user);
        user = serviceUserDAO.getUserById(user.getUserId());
        return user.getStatusText() ;
    }

    @RequestMapping(value = {"/searchMessagesBetweenToClient"}, method = RequestMethod.GET, produces = "application/json")
    public List<Message> searchMessagesBetweenSenderAndRecipient(@RequestParam UUID sender,@RequestParam UUID recipient){
        return serviceMessageDAO.getMessageBetweenToClient(sender, recipient);
    }

    @RequestMapping(value = {"/searchMessagesBySenderAndRecipient"}, method = RequestMethod.GET, produces = "application/json")
    public List<Message> searchMessagesBySenderAndRecipient(@RequestParam UUID sender,@RequestParam UUID recipient){
        return serviceMessageDAO.getMessageBySenderAndRecipient(sender, recipient);
    }

    @RequestMapping(value = {"/setMessageStatusOnReceived"}, method = RequestMethod.GET, produces = "application/json")
    public void searchMessagesBySenderAndRecipient(@RequestParam String uuid){
        serviceMessageDAO.setMessageStatusReceivedByRecipient(uuid);
    }

    @RequestMapping(value = {"/setMessageStatusReadBySenderAndRecipient"}, method = RequestMethod.GET)
    public String setMessageStatusReadBySenderAndRecipient(@RequestParam String uuidSender, @RequestParam  String uuidRecipient){
        System.out.println("TEst zmiany na read: sender" + uuidSender + " recipient " + uuidRecipient);
        serviceMessageDAO.setMessageStatusReadBySenderAndRecipient(uuidSender, uuidRecipient);
        return "OK wykonalem";
    }

    @RequestMapping(value = {"/searchUserById"})
    public User searchUserById(@RequestParam UUID id){
        return serviceUserDAO.getUserById(id);
    }

    @RequestMapping(value = {"/searchUserByNick"}, method = RequestMethod.GET)
    public User searchUserByNick(@RequestParam String nick){
        return serviceUserDAO.getUserByNick(nick);
    }

    @RequestMapping(value = {"/confirmationByMsg"})
    public Message confirmationOfReadingMessage(@RequestBody Message message){
        Message msg = serviceMessageDAO.getMessageById(message.getIdentifier());
        msg.setMessageStatus(MessageStatus.read);
        serviceMessageDAO.save(msg);
        return msg;
    }

    @RequestMapping(value = {"/onlineUserToList"})
    public List<User> onlineUserToList(){
        List<User> listOfOnlineUsers = serviceUserDAO.getUsers(); //change to get by status
        List<User> safeListOfUser = new ArrayList<>();
        listOfOnlineUsers.stream().forEach(user -> {
            safeListOfUser.add(
                    User.builder().nick(user.getNick()).userId(user.getUserId()).build()
            );
        });
        return safeListOfUser;
    }

    @RequestMapping(value = {"/allUserToList"})
    public List<User> allUserToList(){
        List<User> listOfOnlineUsers = serviceUserDAO.getUsers();
        List<User> safeListOfUser = new ArrayList<>();
        listOfOnlineUsers.stream().forEach(user -> {
            safeListOfUser.add(
                    User.builder()
                            .nick(user.getNick())
                            .userId(user.getUserId())
                            .logStatus(user.isLogStatus())
                            .build()
            );
        });
        return safeListOfUser;
    }

    @RequestMapping(value = {"/confirmationByMsgId"})
    public Message confirmationOfReadingMessageId(@RequestParam UUID id){
        Message msg = serviceMessageDAO.getMessageById(id);
        msg.setMessageStatus(MessageStatus.read);
        serviceMessageDAO.save(msg);
        return msg;
    }

    @RequestMapping(value = {"/findAll"})
    public List<User> findAll(){
        List<User> listOfUser = serviceUserDAO.getUsers();
        return listOfUser;
    }

    @RequestMapping(value = {"/findAllMessages"})
    public List<Message> findAllMessages(){
        List<Message> listOfUser = serviceMessageDAO.getAllMessages();
        return listOfUser;
    }

    @RequestMapping(value = {"/findBySender"}, method = RequestMethod.GET, produces = "application/json")
    public List<Message> findBySender(@RequestParam String sender){
        List<Message> listOfUser = serviceMessageDAO.getMessageFromSender(UUID.fromString(sender));
        return listOfUser;
    }

    @RequestMapping(value = {"/findByTextMsg"})
    public Message findByTextMsg(@RequestParam String text){
        Message message= serviceMessageDAO.getMessageByTextMsg(text);
        return message;
    }

    @RequestMapping(value = {"/createUser"}, method = RequestMethod.POST, consumes = "application/json"
    , produces = "text/html")
    public String creatingANewUser(@RequestBody User user){
        try {
            serviceUserDAO.save(user);
        } catch (EntityExistException e) {
            return e.getMessage();
        }
        return "OK";
    }

    @RequestMapping(value = {"/saveMassage"}, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public void saveMessage(@RequestBody Message message){
        serviceMessageDAO.save(message);
    }
}

