package com.example.demo;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
//    private boolean customerFind = false;
    private boolean employeeFind = false;

    Connection conn;
    Statement stmt;

    @FXML
    protected void logInButtonClick() throws ClassNotFoundException, SQLException, IOException {
        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();
        stmt.execute("use ilovepizza");
        if(!checkBoxEmployee.isSelected()){
            ResultSet test = stmt.executeQuery("select cus_name,cus_password from customer");
            if(test.next()==false){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Please create a customer account at first!");
                a.show();
            }
            test.close();
            ResultSet rs = stmt.executeQuery("select cus_name,cus_password,cus_id,post_id from customer WHERE cus_name = '"+userName.getText()+"'");
            if(rs.next()) {
                String cus_name = rs.getString(1);
                String cus_password = rs.getString(2);
                if(cus_name.equals(userName.getText())&&cus_password.equals(password.getText())){
//                    customerFind = true;
                    System.out.println("a customer logged in");
                    Stage window = (Stage) login.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(ilovepizzaApplication.class.getResource("customerMenu.fxml"));
                    Parent parent = loader.load();
                    customerMenuController CMController = loader.getController();
                    CMController.customerName.setText(userName.getText());
                    CMController.cus_id = rs.getInt(3);
                    CMController.post_id = rs.getInt(4);

                    ResultSet discount = stmt.executeQuery("SELECT code FROM discount_code JOIN customer_discount ON discount_code.code_id = customer_discount.code_id JOIN customer ON customer.cus_id = "+rs.getInt(3)+" WHERE if_used = false ");
                    while (discount.next()){
                        CMController.couponCode.getItems().add(discount.getString(1));
                    }

                    Scene scene = new Scene(parent,781,394);
                    window.setScene(scene);
                    //TODO: create a new window and switch to it for customer
                }

            }else{
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("username wrong or password wrong");
                a.show();
            }

            rs.close();


        }else{
            ResultSet test = stmt.executeQuery("select deli_employee_name,employee_password from delivery_employee");
            if(test.next()==false){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Please create an employee account at first!");
                a.show();
            }
            test.close();
            ResultSet rs = stmt.executeQuery("select deli_employee_id,deli_employee_name,employee_password from delivery_employee");
            while (rs.next()) {
                String deli_name = rs.getString(2);
                String deli_password = rs.getString(3);
                if(deli_name.equals(userName.getText())&&deli_password.equals(password.getText())){
                    System.out.println("an employee logged in");
                    //
                    Stage window = (Stage) login.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(ilovepizzaApplication.class.getResource("employeePage.fxml"));
                    Parent parent = loader.load();
                    employeePageController EPController = loader.getController();
                    EPController.employee_name.setText(userName.getText());
                    EPController.employee_id = rs.getInt(1);
                    Scene scene = new Scene(parent,674,400);
                    window.setScene(scene);
                    employeeFind = true;
                    break;
                    //
                    //TODO: create a new window and switch to it for employee
                }
            }
            if(!employeeFind){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("username wrong or password wrong");
                a.show();
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