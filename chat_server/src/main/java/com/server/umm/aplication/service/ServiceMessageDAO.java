package com.server.umm.aplication.service;

import com.server.umm.aplication.entity.message.Message;
import com.server.umm.aplication.repository.MessageCRUD;
import com.server.umm.aplication.repository.MessageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServiceMessageDAO implements MessageDAO {

    /**
     * Class to create queries to DB - Message Type are inserted and selected.
     * TODO - query to group conversation - needed to create new DB table?
     * TODO - confirmation of receive message - save to DB (needed new field in class Message)
     * */

    MessageCRUD messageCRUD;

    @Autowired
    public ServiceMessageDAO(MessageCRUD messageCRUD){
        this.messageCRUD = messageCRUD;
    }

    @Override
    public boolean save(Message message) {
        messageCRUD.save(message);
        Optional<Message> existingTest = messageCRUD
                .findBySenderAndRecipientAndTextMsgAndDateMsg(message.getSender(),message.getRecipient(),
                        message.getTextMsg(), message.getDateMsg());
        if(existingTest.isPresent()) return true;
        else return false;
    }

    @Override
    public List<Message> getAll() {
        List<Message> listOfMessage = new ArrayList<>();
        messageCRUD.findAll().forEach(msg -> listOfMessage.add(msg));
        return listOfMessage;
    }

    public List<Message> getMessageBetweenToClient(UUID sender, UUID recipient){
        List<Message> listOfMessage = messageCRUD.findBySenderAndRecipientOrSenderAndRecipient(sender,recipient, recipient, sender);
        return listOfMessage;
    }

    public void setMessageStatusReceivedByRecipient(String uuid){
        messageCRUD.setMessageStatusReceivedByRecipient(uuid);
    }

    public void setMessageStatusReadBySenderAndRecipient(String uuidSender, String uuidRecipient){
        messageCRUD.setMessageStatusReadBySenderAndRecipient(uuidSender, uuidRecipient);
    }

    public List<Message> getMessageBySenderAndRecipient(UUID sender, UUID recipient){
        List<Message> listOfMessage = messageCRUD.findBySenderAndRecipient(sender,recipient);
        return listOfMessage;
    }

    public List<Message> getAllMessages(){
        List<Message> listOfMessage = new ArrayList<>();
        messageCRUD.findAll().forEach(msg -> listOfMessage.add(msg));
        return listOfMessage;
    }
    //something is not correct -> needed is two users to find conversation
    // check places of using this methods
    @Override
    public List<Message> getMessageFromSender(UUID user) {
        List<Message> listOfMessage = messageCRUD.findBySender(user);
        return listOfMessage;
    }


    public Message getMessageByTextMsg(String text) {
        Message message = messageCRUD.findByTextMsg(text);
        return message;
    }

    //something is not correct -> needed is two users to find conversation
    // check places of using this methods
    @Override
    public List<Message> getMessageFromRecipient(UUID user) {
        List<Message> listOfMessage = messageCRUD.findByRecipient(user);
        return listOfMessage;
    }

    public Message getMessageById(UUID id){
        return messageCRUD.findByIdentifier(id);
    }
}


