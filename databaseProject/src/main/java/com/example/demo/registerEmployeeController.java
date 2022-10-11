package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class registerEmployeeController {


    public Button registerEmp;
    public PasswordField deli_pass;
    public PasswordField deli_pass_confirmation;
    public TextField deli_name;
    @FXML
    protected Label backToLoginEmp;
    Statement stmt;
    Connection conn;

    @FXML
    protected void registerEmp() throws ClassNotFoundException, SQLException, IOException {
        if(deli_pass.getText().equals(deli_pass_confirmation.getText())){
            Class.forName(hostInfo.getJdbcDriver());
            conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
            stmt = conn.createStatement();
            stmt.execute("use ilovepizza");

            stmt.execute("INSERT INTO delivery_employee (deli_employee_name,employee_password) VALUES ('"
                    +deli_name.getText()+"','"
                    +deli_pass.getText()
                    +"')");
            System.out.println("new employee created");
            Stage window = (Stage) registerEmp.getScene().getWindow();
            Scene scene = new Scene(new FXMLLoader(ilovepizzaApplication.class.getResource("loginPage.fxml")).load(),640,400);
            window.setScene(scene);
            stmt.close();
            conn.close();
        }else{
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Two input password inconsistent, please confirm to re-enter");
            a.show();
        }
    }

    @FXML
    protected void backToLoginEmp() throws IOException{
        Stage window = (Stage) backToLoginEmp.getScene().getWindow();
        Scene scene = new Scene(new FXMLLoader(ilovepizzaApplication.class.getResource("loginPage.fxml")).load(),640,400);
        window.setScene(scene);
    }
}
