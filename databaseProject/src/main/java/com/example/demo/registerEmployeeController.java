package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class registerEmployeeController {


    public Button registerEmp;
    public PasswordField deli_pass;
    public PasswordField deli_pass_confirmation;
    public TextField deli_name;
    public TextField deli_post_code;
    @FXML
    protected Label backToLoginEmp;
    Statement stmt;
    Connection conn;

    @FXML
    protected void registerEmp() throws ClassNotFoundException, SQLException, IOException {
        //TODO
        if(deli_pass.getText().equals(deli_pass_confirmation.getText())){
            int post_id = 0;
            Class.forName(hostInfo.getJdbcDriver());
            conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
            stmt = conn.createStatement();
            stmt.execute("use ilovepizza");

            String query = "SELECT p.post_id FROM post_code AS p WHERE post_code = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,deli_post_code.getText());
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                post_id = rs.getInt(1);
            }else{
                stmt.execute("INSERT INTO post_code (post_code) VALUES ('"+deli_post_code.getText()+"')");

                String query1 = "SELECT p.post_id FROM post_code AS p WHERE post_code = ?";
                PreparedStatement ps1 = conn.prepareStatement(query1);
                ps1.setString(1,deli_post_code.getText());
                ResultSet rs1 = ps1.executeQuery();
                if (rs1.next()) {
                    post_id = rs1.getInt(1);
                }
            }

            stmt.execute("INSERT INTO delivery_employee (deli_employee_name,employee_password,post_id) VALUES ('"
                    +deli_name.getText()+"','"
                    +deli_pass.getText()+"',"
                    +post_id
                    +")");
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
