package com.server.umm.aplication.network;

import com.server.umm.aplication.entity.message.Message;
import com.server.umm.aplication.entity.message.MessageStatus;
import com.server.umm.aplication.entity.user.User;
import com.server.umm.aplication.packet.converter.MessageConverter;
import com.server.umm.aplication.packet.message.*;
import com.server.umm.aplication.serverTCP.ServerConnection;
import com.server.umm.aplication.service.ServiceMessageDAO;
import com.server.umm.aplication.service.ServiceUserDAO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


/**
 * Klasa jest fasadą (wzorzec strukturalny) umożliwiającą wysyłanie i odbieranie wiadomości TCP.
 * W razie konieczności dodaj potrzebne metody
 */

public class TcpNetworkAccess {

    private TcpSender sender;
    private TcpReceiver receiver;
    private BaseMessage receiveMsg;
    private MessageConverter messageConverter;
    private Socket clientSocket;
    private InputStream inFromServer;
    private DataOutputStream outToServer;
    @Getter
    private TextMessage textMessage;
    @Getter
    private UUID userId;
    Map<UUID, TcpNetworkAccess> mapThread;

    private ServiceMessageDAO serviceMessageDAO;
    private ServiceUserDAO serviceUserDAO;


    public TcpNetworkAccess(Socket clientSocket, Map<UUID, TcpNetworkAccess> mapThread,
                            ServiceUserDAO serviceUserDAO, ServiceMessageDAO serviceMessageDAO){
        try {
            this.clientSocket = clientSocket;
            this.outToServer = new DataOutputStream(clientSocket.getOutputStream());
            this.inFromServer = clientSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.receiver = new TcpReceiver((msg) -> receiveMessage((BaseMessage) msg), this.inFromServer);
        this.sender = new TcpSender( this.outToServer );
        this.messageConverter = new MessageConverter();
        this.mapThread = mapThread;
        this.serviceUserDAO = serviceUserDAO;
        this.serviceMessageDAO = serviceMessageDAO;
    }

    public TcpNetworkAccess(InetAddress serverAddress, int serverPort){
        try {
            this.clientSocket = new Socket(serverAddress, serverPort);
            System.out.println("nawiazano polaczenie ip: " + serverAddress.toString()+ " port: " + serverPort );
            this.outToServer = new DataOutputStream(clientSocket.getOutputStream());
            this.inFromServer = clientSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("nie nawiazano polaczenia ip: " + serverAddress.toString()+ " port: " + serverPort );
        }
        this.receiver = new TcpReceiver((msg) -> receiveMessage((BaseMessage) msg), this.inFromServer);
        this.sender = new TcpSender( this.outToServer );
        this.messageConverter = new MessageConverter();
    }

    /**
     * Metoda wysyłająca wiadomość tekstową UDP.
     * @param message
     * @return bool (true if send was correct)
     */
    public boolean sendMessage(BaseMessage message) throws IOException {
        // Convert dto.message do UdpMessage();
        System.out.println("wysylam do " + this.userId);
        TcpPacket packet = new MessageConverter().serialize(message);
        return sender.sendMessage(packet);
}
    /**
     * Włącza odbiorcę (nasłuch) wiadomości UDP. Po uruchomieniu odbiorcy TcpReceiver otrzymuje pakiet i wywołuje
     * metodę receiveMessage. Odbiorca będzie nasłuchiwał na adresie oraz porcie przekazanym w konstrukturze klasy
     * {@link TcpNetworkAccess}
     */
    public void start() {
        receiver.start();

    }

    /**
     * ToDo Zaimplementuj metodę odbierającą pakiet
     * Tutaj dodaj kod który ma się wykonać po odebraniu pakietu
     * Po odebraniu pakietu należy sprawdzić jaki to pakiet i odpowiednio go zinterpretować.
     * Przykładowo jeśli pakiet to {@link TextMessage} to należy go wyświetlić w oknie GUI lub w konsoli
     * z datą odebrania wiadomości, użytkownikiem który wiadomość wysłał oraz treścią wiadomości.
     */
    public void receiveMessage(BaseMessage baseMessage) {

        if (baseMessage instanceof TextMessage) {
            textMessage = (TextMessage) baseMessage;
            textMessage.setDateMsg(Timestamp.valueOf(LocalDateTime.now()));
            User receiver = serviceUserDAO.getUserById(UUID.fromString(textMessage.getReceiver()));
            boolean logStatusOfReceiver = receiver.isLogStatus();
            try {
                System.out.println("receiver status: " + logStatusOfReceiver);
                if(logStatusOfReceiver){
                    textMessage.setMessageStatus(MessageStatus.received);
                    mapThread.get(UUID.fromString(textMessage.getReceiver())).sendMessage(textMessage);
                }
                sendMessage(textMessage);
                System.out.println("watek z pozycji: " + (this.userId)
                        +" o porcie" + clientSocket.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
            saveMessageToDB(textMessage);

        }
        else if (baseMessage instanceof Ingoing) {
            receiveMsg =  baseMessage;
            Ingoing receiveInGoingMessage = (Ingoing) receiveMsg;
            this.userId = UUID.fromString(receiveInGoingMessage.getUserId());
            mapThread.put(this.userId,this);
            System.out.println("watek z pozycji: " + (this.userId)
                    +" o porcie" + clientSocket.getPort());
//            updateUserToDB(receiveInGoingMessage);
            this.serviceUserDAO.updateLogStatusUser(receiveInGoingMessage.getNick());
            sendInformationAboutRefresingUserList(receiveInGoingMessage);
            serviceMessageDAO.setMessageStatusReceivedByRecipient(receiveInGoingMessage.getUserId());
        }
        else if (baseMessage instanceof Outgoing) {
            receiveMsg =  baseMessage;
            Outgoing receiveOutGoingMessage = (Outgoing) receiveMsg;
//            updateLogoutUserToDB(receiveOutGoingMessage);
            this.serviceUserDAO.updateLogoutStatusUser(receiveOutGoingMessage.getNick());
            sendInformationAboutRefresingUserList(receiveOutGoingMessage);
        }
        else if (baseMessage instanceof MessageConfirmation) {
            receiveMsg =  baseMessage;
            MessageConfirmation receiveConfirmationMessage = (MessageConfirmation) receiveMsg;
        }
        else if (baseMessage instanceof ReadMessage){
            receiveMsg = baseMessage;
            ReadMessage readMessage = (ReadMessage) receiveMsg;
            System.out.println("server przyjal read message" + readMessage.toString());
            sendInformationAboutRefreshingMessageListBcsRead(readMessage);
        }
    }

//    /**
//     * BUG !!!!!!!!!!
//     * */
    public void sendInformationAboutRefreshingMessageListBcsRead(ReadMessage readInformation){
        try {
            System.out.println("TEST na read send info " + readInformation.toString());
            System.out.println("size of map thread !!!!" + mapThread.size());
            if(this.mapThread.get(UUID.fromString(readInformation.getSender())) != null ) {
                this.mapThread.get(UUID.fromString(readInformation.getSender())).sendMessage(readInformation);
            }
            if(this.mapThread.get(UUID.fromString(readInformation.getRecipient())) != null ){
                this.mapThread.get(UUID.fromString(readInformation.getRecipient())).sendMessage(readInformation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInformationAboutRefresingUserList(BaseMessage message){
        for(Map.Entry<UUID, TcpNetworkAccess> entry : mapThread.entrySet()) {
            UUID key = entry.getKey();
            TcpNetworkAccess value = entry.getValue();
            try {
                value.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateUserToDB(Ingoing ingoing){
        String uri = "/loginTrue?nick=" + ingoing.getNick();
        String response = sendRequestGet(uri);
        System.out.println("Response from update logStatus: " + response);
    }

    public void updateLogoutUserToDB(Outgoing outgoing){
        String uri = "/logout?nick=" + outgoing.getNick();
        String response = sendRequestGet(uri);
        System.out.println("Response from update logStatus: " + response);
    }

    public void saveMessageToDB(TextMessage textMessage){
        Message message = Message.builder()
                .messageStatus(textMessage.getMessageStatus())
                .dateMsg(textMessage.getDateMsg())
                .recipient(UUID.fromString(textMessage.getReceiver()))
                .sender(this.userId)
                .textMsg(textMessage.getTextMsg())
                .messageStatus(textMessage.getMessageStatus())
                .build();
        /**
         * TODO injection of serviceMessageDAO
         * */
//        serviceMessageDAO. ()
        String json =
                "{"+
                   "\"sender\": \"" + message.getSender() + "\","+
                   "\"recipient\": \"" + message.getRecipient() + "\","+
                   "\"textMsg\": \"" + message.getTextMsg() + "\","+
//                       "\"dateMsg\": \"" + message.getDateMsg() + "\","+
                   "\"messageStatus\": \"" + message.getMessageStatus() + "\""+
                 "}";

        sendRequestPost(json, "saveMassage");
    }

    public void sendRequestPost(String json, String uri) {
        try {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                HttpPost httpPost = new HttpPost("http://localhost:8080/" + uri);
                System.out.println("json:" + json);
                StringEntity entity = new StringEntity(json);
                httpPost.setEntity(entity);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                CloseableHttpResponse response = httpclient.execute(httpPost);
                System.out.println("Saving msg to DB status: " + response.getStatusLine().getStatusCode() +
                        response.getAllHeaders() + response.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendRequestGet(String uri) {
        String responseBody = null;

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet("http://localhost:8080/" + uri);
            System.out.println("Executing request " + httpGet.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            responseBody = httpclient.execute(httpGet, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    public boolean confirmMessage(MessageConfirmation confirmation) throws IOException {
        /**
         * ToDo Zaimplementuj metodę wysyłającą potwierdznie odebrania pakietu
         * Tutaj dodaj kod, który ma się wykonać aby wysłać wiadomość potwierdzającą odebranie wiadomości
         */

        TcpPacket tcpPacketConfirmation = messageConverter.serialize(confirmation);
        return sender.sendMessage(tcpPacketConfirmation);
    }

    public boolean leaveChat(Outgoing message) throws IOException {
        /**
         * ToDo Zaimplementuj metodę wysyłającą informację o wyłączeniu czata
         * Tutaj dodaj kod, który ma się wykonać aby wysłać wiadomość informującą wszystkich użytkowników
         * o wyłączeniu aplikacji czat.
         */
        TcpPacket tcpPacketLeave = messageConverter.serialize(message);
        return sender.sendMessage(tcpPacketLeave);
    }

    public boolean joinChat(Ingoing message) throws IOException {
        /**
         * ToDo Zaimplementuj metodę wysyłającą informację o włączeniu czata
         * Tutaj dodaj kod, który ma się wykonać aby wysłać wiadomość informującą wszystkich użytkowników
         * o włączeniu aplikacji czat. Ten pakiet informuje pozostałych użytkowników, że nowy użytkownik pojawił się
         */
        TcpPacket tcpPacketJoin = messageConverter.serialize(message);
        return sender.sendMessage(tcpPacketJoin);
    }

    public BaseMessage getReceiveMsg() {
        return receiveMsg;
    }

    public void setReceiveMsg(BaseMessage receiveMsg) {
        this.receiveMsg = receiveMsg;
    }
}
