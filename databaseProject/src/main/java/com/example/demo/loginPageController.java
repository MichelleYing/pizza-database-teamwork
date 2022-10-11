package com.example.demo;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class loginPageController {
    public CheckBox checkBoxEmployee;
    @FXML
    protected Button login;
    @FXML
    protected TextField userName;
    @FXML
    protected PasswordField password;
    @FXML
    protected Label createAnAccount;
    @FXML
    protected Label firstTimeRunning;

    Connection conn;
    Statement stmt;

    @FXML
    protected void logInButtonClick() throws ClassNotFoundException, SQLException, IOException {
        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();
        stmt.execute("use ilovepizza");
        if(!checkBoxEmployee.isSelected()){
            ResultSet rs = stmt.executeQuery("select cus_name,cus_password from customer");
            while (rs.next()) {
                String cus_name = rs.getString(1);
                String cus_password = rs.getString(2);
                if(cus_name.equals(userName.getText())&&cus_password.equals(password.getText())){
                    System.out.println("a customer logged in");
                    Stage window = (Stage) login.getScene().getWindow();
                    //window.close();
                    Scene scene = new Scene(new FXMLLoader(ilovepizzaApplication.class.getResource("CustomerMenu.fxml")).load());
                    window.setScene(scene);

                    break;
                    //TODO: create a new window and switch to it for customer
                }else{
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("username wrong or password wrong");
                    a.show();

                }
                rs.close();
            }

        }else{
            ResultSet rs = stmt.executeQuery("select deli_employee_name,employee_password from delivery_employee");
            while (rs.next()) {
                String cus_name = rs.getString(1);
                String cus_password = rs.getString(2);
                if(cus_name.equals(userName.getText())&&cus_password.equals(password.getText())){
                    System.out.println("an employee logged in");
                    break;
                    //TODO: create a new window and switch to it for employee
                }else{
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("username wrong or password wrong");
                    a.show();
                }
            }
            rs.close();
        }
        stmt.close();
        conn.close();
    }

    @FXML
    protected void createAnAccountButtonClick() throws IOException {
        Stage window = (Stage) createAnAccount.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(ilovepizzaApplication.class.getResource("registerCustomer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),640,400);
        window.setScene(scene);

    }

    @FXML
    protected void firstTimeRunning() throws IOException {
        Stage window = (Stage) firstTimeRunning.getScene().getWindow();
        Scene scene = new Scene(new FXMLLoader(ilovepizzaApplication.class.getResource("hostConnector.fxml")).load());
        window.setScene(scene);
    }




}