package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class adminLoginController {
    @FXML
    public PasswordField adminPassword;
    @FXML
    public TextField adminName;
    @FXML
    protected Button adminLogin;
    @FXML
    protected Label backToLoginAdmin;
    @FXML
    protected void backToLoginAdmin() throws IOException {
        Stage window = (Stage) backToLoginAdmin.getScene().getWindow();
        Scene scene = new Scene(new FXMLLoader(ilovepizzaApplication.class.getResource("loginPage.fxml")).load(),640,400);
        window.setScene(scene);
    }
    @FXML
    protected void adminLogin() throws IOException {
        if(adminName.getText().equals(hostInfo.getUSER())&&adminPassword.getText().equals(hostInfo.getPASS())){

            Stage window = (Stage) backToLoginAdmin.getScene().getWindow();
            Scene scene = new Scene(new FXMLLoader(ilovepizzaApplication.class.getResource("registerEmployee.fxml")).load(),640,400);
            window.setScene(scene);
        }else{
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("hostname wrong or password wrong");
            a.show();
        }
    }
}
