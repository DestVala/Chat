package com.chat.smm.gui;

import com.chat.smm.entity.message.Message;
import com.chat.smm.entity.message.MessageStatus;
import com.chat.smm.entity.user.User;
import com.chat.smm.entity.user.UserOnList;
import com.chat.smm.network.TcpNetworkAccess;
import com.chat.smm.packet.message.TextMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

@Setter
public class ChatController implements Initializable {

    TcpNetworkAccess tcpNetworkAccess;
    @FXML
    private ListView contactList;
    @FXML
    private ListView messageList;
    @FXML
    private Tab tab;
    @FXML
    Button buttonSend;
    @FXML
    private TextField textField;
    ObjectMapper objectMapper;
    private String uuidOfReceiver;
    private String uuidOfSender;
    private HashMap<UUID, String> mapOfUser;

    public ObservableList<String> items = FXCollections.observableArrayList("Chat - MUS :)");
    public ObservableList<UserOnList> users;

    public ChatController() {
        addUsersToList();
        this.objectMapper = new ObjectMapper();
        this.uuidOfReceiver = "";
        this.mapOfUser = new HashMap<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void chooseClient(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            items.clear();

            System.out.println("Clicked on: " + contactList.getSelectionModel().getSelectedItems());
            UserOnList selectedUser = users.get(contactList.getSelectionModel().getSelectedIndex());
            System.out.println("Clicked user: " + selectedUser.toString() + " uuid:" + selectedUser.getUserId());

            this.uuidOfReceiver = selectedUser.getUserId().toString();
            String nickOfReceiver = selectedUser.getNick();
            mapOfUser.put(UUID.fromString(uuidOfReceiver),nickOfReceiver);
            tab.setText("TALK with " + nickOfReceiver);

            this.uuidOfSender = tcpNetworkAccess.getLoginController().getUserId().toString();
            String nickOfSender = tcpNetworkAccess.getLoginController().getLogin();
            mapOfUser.put(UUID.fromString(uuidOfSender), nickOfSender);

            sendRequestToActualizeStatusOnRead(this.uuidOfSender ,this.uuidOfReceiver);
            this.tcpNetworkAccess.createReadMessageAndSend(uuidOfReceiver, uuidOfSender);
            downloadHistoryOfMessagesBeetweenToClient(uuidOfSender,uuidOfReceiver);
        }
    }

    private void sendRequestToActualizeStatusOnRead(String uuidOfSender,String uuidOfReceiver ) {
        String url ="http://localhost:8080/setMessageStatusReadBySenderAndRecipient?uuidSender="
                + uuidOfReceiver + "&uuidRecipient=" + uuidOfSender ;
        String response = createGETRequest(url);
        System.out.println("Response po aktualizacji na read: " + response);
    }

    public void downloadHistoryOfMessagesBeetweenToClient(String sender, String recipient){
        String response = sendRequestToGetMessages(sender,recipient);
        try {
            List<Message> listOfMsg = objectMapper.readValue(response, new TypeReference<List<Message>>() {});
            listOfMsg.stream().forEach(
                    msg -> items.add("" + msg.getDateMsg().toString() + "    (" + msg.getMessageStatus().toString() + ") " +
                            "\n" + this.mapOfUser.get(msg.getSender()) + ":   " + msg.getTextMsg())
            );
            addMessageToMessageByChooseClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String sendRequestToGetMessages(String sender, String recipient) {

        String url = "http://localhost:8080/searchMessagesBetweenToClient?sender="
                + sender + "&recipient=" + recipient;
        String responseBody = createGETRequest(url);
        return responseBody;
    }

    public void refreshMessageList(String uuid) {
        System.out.println("odswiezam liste: " + uuid);
        if(uuid.equals(this.uuidOfReceiver)){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            downloadHistoryOfMessagesBeetweenToClient(this.uuidOfSender, this.uuidOfReceiver);
        }

    }

    public void addMessageToMessageList(TextMessage textMessage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String message = textMessage.getTextMsg();
                String sender = textMessage.getSender();
                Timestamp date = textMessage.getDateMsg();
                MessageStatus messageStatus = textMessage.getMessageStatus();
                System.out.println("TEST chat controller msgstat: " + textMessage.getMessageStatus());

                if (!items.equals(messageList.getItems())) {
                    messageList.setItems(items);
                }
                items.add(date.toString() + "  (" + messageStatus.toString() + ") " + "\n" + sender + ":   " + message);

            }
        });
    }

    public void addMessageToMessageByChooseClient() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (!items.equals(messageList.getItems())) {
                    messageList.setItems(items);
                }
            }
        });
    }

    public void addUsersToList() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String response = sendRequestToGetUsers();
                try {
                    List<User> listOfUsers = objectMapper.readValue(response, new TypeReference<List<User>>() {});
                    List<UserOnList> userOnLists = new ArrayList<>();
                    listOfUsers.stream().forEach(user -> userOnLists.add(new UserOnList(user)));
                    users = FXCollections.observableArrayList(userOnLists);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!users.equals(contactList.getItems())) {
                    contactList.setItems(users);
                }
            }
        });
    }

    private String sendRequestToGetUsers() {

        String responseBody = createGETRequest("http://localhost:8080/allUserToList");
        return responseBody;
    }

    public String createGETRequest(String url){

        String responseBody = "";

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            System.out.println("Executing request " + httpGet.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status to get users: " + status);
                }
            };
            responseBody = httpclient.execute(httpGet, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;

    }

    public void sendMessageButton(ActionEvent actionEvent) {
        if (textField.getText().length() > 0 && !uuidOfReceiver.equals("")) {
            sendMessageByAction();
        }
    }

    public void sendByEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER && textField.getText().length() > 0 && !uuidOfReceiver.equals("")) {
            sendMessageByAction();
        }
    }

    public void sendMessageByAction() {
        String messageToSend = textField.getText();
        UUID messageToSendId = UUID.randomUUID();
        TextMessage textMessage = TextMessage.builder()
                .identifier(messageToSendId.toString())
                .receiver(uuidOfReceiver)
                .sender(tcpNetworkAccess.getLoginController().getLogin())
                .textMsg(messageToSend)
                .messageStatus(MessageStatus.waited)
                .build();
        try {
            tcpNetworkAccess.sendMessage(textMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textField.clear();
    }



}
