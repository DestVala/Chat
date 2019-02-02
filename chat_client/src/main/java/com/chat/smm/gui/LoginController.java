package com.chat.smm.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Created by Michal Ziolecki.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginController implements Initializable {

    @FXML
    private Button loginButton;
    @FXML
    private TextField textField;
    @FXML
    private PasswordField textFieldPassword;
    @FXML
    private Label warning;
    @Getter
    private String login;
    @Getter
    private String password;
    @Getter
    private UUID userId;
    private HttpClient httpClient;
    @Getter
    private boolean checkLoginStatus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.checkLoginStatus = false;
    }

    public void logToChat(ActionEvent actionEvent) {
        if (checkFieldsNotEmpty()) {
            startAction();
        }
    }

    public void pushEnter(KeyEvent pressedKey) {
        if (checkPushEnterAndFieldsNotEmpty(pressedKey)) {
            startAction();
        }
    }

    private void startAction() {
        login = textField.getText();
        password = textFieldPassword.getText();
        String uuidChecking = sendRequestToCheckPass(login, password);
//            this.userId = UUID.randomUUID();
        //System.out.println( "Login test: " + login );
        if(!uuidChecking.isEmpty() && uuidChecking.length() >= 32){
            this.userId = UUID.fromString(uuidChecking);
            ChatApplicationView.stageLogin.close();
            this.checkLoginStatus = true;
            textField.clear();
            textFieldPassword.clear();
            warning.setText("");
        }
        else {
            textField.clear();
            textFieldPassword.clear();
            warning.setText("Zle dane logowania");
        }

    }

    private boolean checkPushEnterAndFieldsNotEmpty(KeyEvent pressedKey) {
        return pressedKey.getCode() == KeyCode.ENTER && textField.getText().length() > 0
                && textFieldPassword.getText().length() > 0;
    }

    private boolean checkFieldsNotEmpty() {
        return textField.getText().length() > 0 && textFieldPassword.getText().length() > 0;
    }

    private String sendRequestToCheckPass(String user, String password) {

        String responseBody = null;

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet("http://localhost:8080/login?nick="+ user
                    + "&password=" + password );
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
}
