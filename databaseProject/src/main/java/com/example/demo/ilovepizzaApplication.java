package com.example.demo;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class ilovepizzaApplication extends Application {
    Connection conn;
    Statement stmt;

    public void distributeOrder() throws ClassNotFoundException, SQLException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/hostInfo.csv"));
            if(reader.readLine() !=null){
                Class.forName(hostInfo.getJdbcDriver());
                conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
                stmt = conn.createStatement();
                stmt.execute("use ilovepizza");

                String queryOrder = "SELECT o.ord_id,c.post_id from orders AS o JOIN customer AS c ON o.cus_id = c.cus_id WHERE o.order_status = 'preparing' AND TIMESTAMPDIFF(MINUTE,o.start_ord_time,current_timestamp)>10";
                PreparedStatement psOrder = conn.prepareStatement(queryOrder);
                ResultSet rsOrder = psOrder.executeQuery();
                while(rsOrder.next()){
                    int ord_id = rsOrder.getInt(1);
                    int post_id = rsOrder.getInt(2);
                    String queryDM = "SELECT d.deli_employee_id from delivery_employee AS d WHERE d.if_delivering = false AND d.post_id = ?";
                    PreparedStatement psDM = conn.prepareStatement(queryDM);
                    psDM.setInt(1,post_id);
                    ResultSet rsDM = psDM.executeQuery();
                    if(rsDM.next()){
                        int delivery_employee_id = rsDM.getInt(1);
                        String update = "UPDATE orders SET order_status = 'On the way',deli_employee_id = ? WHERE ord_id = ?";
                        PreparedStatement preStmt = conn.prepareStatement(update);
                        preStmt.setInt(1,ord_id);
                        preStmt.setInt(2,delivery_employee_id);
                        preStmt.executeUpdate();
                        preStmt.close();

                        String updateDeli = "UPDATE delivery_employee SET if_delivering = true WHERE deli_employee_id = ?";
                        PreparedStatement preStmtDeli = conn.prepareStatement(updateDeli);
                        preStmtDeli.setInt(1,delivery_employee_id);
                        preStmtDeli.executeUpdate();
                        preStmtDeli.close();
                    }
                    rsDM.close();
                    psDM.close();
                }
                rsOrder.close();
                psOrder.close();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void deliveringOrders(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/hostInfo.csv"));
            if(reader.readLine() !=null){
                Class.forName(hostInfo.getJdbcDriver());
                conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
                stmt = conn.createStatement();
                stmt.execute("use ilovepizza");

                String queryDeliveryMan = "SELECT d.deli_employee_id,o.ord_id FROM delivery_employee AS d JOIN orders AS o ON d.deli_employee_id = o.deli_employee_id WHERE TIMESTAMPDIFF(MINUTE,o.start_deli_time,current_timestamp)>30 AND o.order_status = 'On the way'";
                PreparedStatement preStmt = conn.prepareStatement(queryDeliveryMan);
                ResultSet rs = preStmt.executeQuery();
                while (rs.next()) {
                    int deli_id = rs.getInt(1);
                    int ord_id = rs.getInt(2);
                    String updateD = "UPDATE delivery_employee AS d SET d.if_delivering = false WHERE d.deli_employee_id = ?";
                    PreparedStatement psd = conn.prepareStatement(updateD);
                    psd.setInt(1,deli_id);
                    psd.executeUpdate();
                    psd.close();

                    String updateO = "UPDATE orders AS o SET o.order_status = 'Arrived' WHERE o.ord_id = ?";
                    PreparedStatement psO = conn.prepareStatement(updateO);
                    psO.setInt(1,ord_id);
                    psO.executeUpdate();
                    psO.close();
                }
                rs.close();
                preStmt.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    Service<Task> distributeOrders = new <Task>Service(){
        @Override
        protected Task createTask() {
            return new Task() {
                @Override
                protected Object call() throws Exception {
                    while(true){
                        Thread.sleep(10000);
//                        System.out.println("reach");
                            distributeOrder();
                    }
                }
            };
        }
    };

    Service<Task> deliveryOrders = new Service<Task>() {
        @Override
        protected Task<Task> createTask() {
            return new Task<Task>() {
                @Override
                protected Task call() throws Exception {
                    while (true) {
                        Thread.sleep(10000);
                        deliveringOrders();
                    }
                }
            };
        }
    };


    @Override
    public void start(Stage stage) {
        try {
            stage.setResizable(false);
            FXMLLoader fxmlLoader = new FXMLLoader(ilovepizzaApplication.class.getResource("loginPage.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 400);
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();

            distributeOrders.start();
            deliveryOrders.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        launch();
    }
}