package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class registerCustomerController {
    @FXML
    public PasswordField cus_password_confirmation;
    @FXML
    public PasswordField cus_password;
    @FXML
    public TextField cus_name;
    @FXML
    public Button registerCus;
    @FXML
    public TextField cus_addr;
    @FXML
    public TextField cus_tele;
    @FXML
    public TextField cus_post_code;

    @FXML
    protected Label newEmployee;
    @FXML
    protected Label backToLoginCus;
    Statement stmt;
    Connection conn;
    @FXML
    protected void newEmployee() throws IOException {
        Stage window = (Stage) newEmployee.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(ilovepizzaApplication.class.getResource("adminLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),640,400);
        window.setScene(scene);
    }

    //back to log_in

    @FXML
    protected void backToLoginCus() throws IOException{
        Stage window = (Stage) backToLoginCus.getScene().getWindow();
        Scene scene = new Scene(new FXMLLoader(ilovepizzaApplication.class.getResource("loginPage.fxml")).load(),640,400);
        window.setScene(scene);
    }

    @FXML
    protected void registerCus() throws ClassNotFoundException, SQLException, IOException {
        if(cus_password.getText().equals(cus_password_confirmation.getText())){
            Class.forName(hostInfo.getJdbcDriver());
            conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
            stmt = conn.createStatement();
            stmt.execute("use ilovepizza");

            stmt.execute("INSERT INTO customer (cus_name,cus_password,cus_tele,cus_addr,cus_post_code) VALUES ('"+cus_name.getText()+"','"
                                                                                                                 +cus_password.getText()+"','"
                                                                                                                 +cus_tele.getText()+"','"
                                                                                                                 +cus_addr.getText()+"','"
                                                                                                                 +cus_post_code.getText()
                                                                                                                 +"')");
            System.out.println("new customer created");
            Stage window = (Stage) registerCus.getScene().getWindow();
            Scene scene = new Scene(new FXMLLoader(ilovepizzaApplication.class.getResource("loginPage.fxml")).load(),640,400);

            stmt.close();
            conn.close();

        }else{
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Two input password inconsistent, please confirm to re-enter");
            a.show();
        }
    }
}
