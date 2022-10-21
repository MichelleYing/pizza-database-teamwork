package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

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

    private boolean postcodeFind = false;
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

            ResultSet rs = stmt.executeQuery("SELECT post_id from post_code WHERE post_code = '"+cus_post_code.getText()+"'");
            if(!rs.next()){
                stmt.execute("INSERT INTO post_code (post_code) VALUES ('"+cus_post_code.getText()+"')");
                ResultSet rs1 = stmt.executeQuery("SELECT post_id from post_code WHERE post_code = '"+cus_post_code.getText()+"'");
                if(rs1.next()){
                    stmt.execute("INSERT INTO customer (cus_name,cus_password,cus_tele,cus_addr,post_id) VALUES ('"+cus_name.getText()+"','"
                            +cus_password.getText()+"','"
                            +cus_tele.getText()+"','"
                            +cus_addr.getText()+"','"
                            +rs1.getInt(1)
                            +"')");
                }
            }else{
                stmt.execute("INSERT INTO customer (cus_name,cus_password,cus_tele,cus_addr,post_id) VALUES ('"+cus_name.getText()+"','"
                        +cus_password.getText()+"','"
                        +cus_tele.getText()+"','"
                        +cus_addr.getText()+"','"
                        +rs.getInt(1)
                        +"')");
            }


            System.out.println("new customer created");
            Stage window = (Stage) registerCus.getScene().getWindow();
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
}
