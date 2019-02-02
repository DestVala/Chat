package com.chat.smm.network;

import com.chat.smm.gui.ChatController;
import com.chat.smm.gui.LoginController;
import com.chat.smm.packet.converter.MessageConverter;
import com.chat.smm.packet.message.*;
import lombok.Getter;
import lombok.Setter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;


/**
 * Klasa jest fasadą (wzorzec strukturalny) umożliwiającą wysyłanie i odbieranie wiadomości UDP.
 * W razie konieczności dodaj potrzebne metody
 */
public class TcpNetworkAccess {
    // Klasa zapewniająca funkcjonalność wysyłąnia wiadomości UDP
    private TcpSender sender;
    // Klasa zapeniająca funkcjonalność odbierania wiadomości UDP
    private TcpReceiver receiver;
    // pole do wiadomosci
    private BaseMessage receiveMsg;
    private MessageConverter messageConverter;
    @Setter
    ChatController chatController;
    @Setter
    @Getter
    LoginController loginController;
    private Socket clientSocket;
    private InputStream inFromServer;
    private DataOutputStream outToServer;

    /**
     *  Constructor with default address and port
     */
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
        this.receiver = new TcpReceiver((msg) -> receiveMessage((BaseMessage) msg),
                this.inFromServer, this.clientSocket, this.outToServer);
        this.sender = new TcpSender( this.outToServer );
        this.messageConverter = new MessageConverter();
    }

    public boolean createIngoingInformationAndSend(){

        BaseMessage login = Ingoing.builder()
                .userId(this.loginController.getUserId().toString())
                .nick(this.loginController.getLogin())
                .userStatus(UserStatus.available)
                .status("")
                .build();

        try {
            sendMessage(login);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean createOutgoingInformationAndSend(){

        BaseMessage logout = Outgoing.builder()
                .nick(this.loginController.getLogin())
                .build();

        try {
            sendMessage(logout);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean createReadMessageAndSend(String uuidSender, String uuidRecipient){
        BaseMessage readInformation = ReadMessage.builder()
                .sender(uuidSender)
                .recipient(uuidRecipient)
                .build();

        try {
            sendMessage(readInformation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Metoda wysyłająca wiadomość tekstową UDP.
     * @param message
     * @return bool (true if send was correct)
     */
    public boolean sendMessage(BaseMessage message) throws IOException {
        // Convert dto.message do UdpMessage();
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

        // checktype of packet (MessageType) and print to console
        // - wyswietlam wszytkie pola dla wiadomosci danego typu
        if (baseMessage instanceof TextMessage) {
            receiveMsg =  baseMessage;
            TextMessage receiveTextMessage = (TextMessage) receiveMsg;
            chatController.addMessageToMessageList( receiveTextMessage );
        }
        else if (baseMessage instanceof Ingoing) {
            receiveMsg =  baseMessage;
            Ingoing receiveInGoingMessage = (Ingoing) receiveMsg;
            chatController.addUsersToList();
            chatController.refreshMessageList(receiveInGoingMessage.getUserId());
        }
        else if (baseMessage instanceof Outgoing) {
            receiveMsg =  baseMessage;
            Outgoing receiveOutGoingMessage = (Outgoing) receiveMsg;
            chatController.addUsersToList();
        }
        else if (baseMessage instanceof MessageConfirmation) {
            receiveMsg =  baseMessage;
            MessageConfirmation receiveConfirmationMessage = (MessageConfirmation) receiveMsg;
        }
        else if (baseMessage instanceof ReadMessage){
            receiveMsg = baseMessage;
            ReadMessage readMessage = (ReadMessage) receiveMsg;
            System.out.println("Uzytkownik" + this.loginController.getLogin() + " otrzymal informacje o odczywaniu \n");
            System.out.println("readMessage: " + readMessage.toString());
            chatController.refreshMessageList(readMessage.getRecipient());
        }
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
