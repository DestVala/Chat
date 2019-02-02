package com.chat.smm.gui;

import com.chat.smm.network.TcpNetworkAccess;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.WindowEvent;

import java.net.InetAddress;

public class ChatApplicationView extends Application {

    Scene loginScene;
    static Stage stageLogin;


    @Override
    public void start(Stage stage) throws Exception {
        //create login window
        FXMLLoader fxmlLoginWindow = new FXMLLoader( getClass().getResource( "/fxml/Login.fxml" ) );
        Parent loginWindow = fxmlLoginWindow.load();
        loginScene = new Scene( loginWindow );
        stageLogin = new Stage(  );
        stageLogin.setScene( loginScene );
        stageLogin.setTitle( "Logowanie do Chat'a MUS" );
        stageLogin.showAndWait();
        LoginController loginController = fxmlLoginWindow.<LoginController>getController();

        if(loginController.isCheckLoginStatus()){
            //create chat window
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/chatWindow.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Chat MUS");
            stage.setScene(scene);
            stage.show();

            ChatController chatController = fxmlLoader.<ChatController>getController();
            TcpNetworkAccess tcpNetworkAccess = new TcpNetworkAccess(InetAddress.getLocalHost(), Integer.parseInt("6666"));
            tcpNetworkAccess.setChatController( chatController );
            tcpNetworkAccess.setLoginController( loginController );
            chatController.setTcpNetworkAccess(tcpNetworkAccess);
            tcpNetworkAccess.start();
            tcpNetworkAccess.createIngoingInformationAndSend();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent closeWindow) {
                    System.out.println("Stage is closing..");
                    tcpNetworkAccess.createOutgoingInformationAndSend();
                    stage.close();
                    System.exit(1);
                }
            });
            stage.setTitle("Chat MUS - Your login is " + loginController.getLogin());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
