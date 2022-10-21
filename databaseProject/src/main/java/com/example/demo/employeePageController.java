package com.example.demo;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class employeePageController {
    @FXML
    public Label employee_name;
    public int employee_id;
    @FXML
    public ComboBox ordersPreparing = new ComboBox<String>(FXCollections.observableArrayList(""));
    @FXML
    public ComboBox ordersOnTheWay = new ComboBox<String>(FXCollections.observableArrayList(""));
    @FXML
    public Button takeItToDelivery;
    @FXML
    public Button orderArrived;
    @FXML
    public TextArea orderPreparingTextArea;
    @FXML
    public TextArea orderOnTheWayTextArea;
    @FXML
    public Button quit;


    Connection conn;
    Statement stmt;
    public void setComboBoxValue() throws SQLException, ClassNotFoundException {
    ordersPreparing.setItems(FXCollections.observableArrayList(""));
    ordersOnTheWay.setItems(FXCollections.observableArrayList(""));


        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();
        stmt.execute("use ilovepizza");

        String queryPreparingOrders = "SELECT ord_id FROM orders WHERE order_status = ?";
        PreparedStatement psP = conn.prepareStatement(queryPreparingOrders);
        psP.setString(1,"preparing");
        ResultSet rsP = psP.executeQuery();
        while(rsP.next()){
            ordersPreparing.getItems().add(rsP.getString(1));
        }
        String queryOnTheWay = "SELECT ord_id FROM orders WHERE order_status = ?";
        PreparedStatement psO = conn.prepareStatement(queryOnTheWay);
        psO.setString(1,"On the way");
        ResultSet rsO = psO.executeQuery();
        while(rsO.next()){
            ordersOnTheWay.getItems().add(rsO.getString(1));
        }
    }


    @FXML
    public void initialize() throws ClassNotFoundException, SQLException {
        setComboBoxValue();
    }


//    public void selectPreparingOrder(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
//        if(!ordersPreparing.getValue().equals("")&&!ordersPreparing.getItems().isEmpty()){
//            Class.forName(hostInfo.getJdbcDriver());
//            conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
//            stmt = conn.createStatement();
//            stmt.execute("use ilovepizza");
//
//            String query = "SELECT o.ord_id,c.cus_name,p.post_code,c.cus_addr,o.total_price,o.start_ord_time FROM orders AS o JOIN post_code AS p ON o.post_id = p.post_id JOIN customer AS c ON o.cus_id = c.cus_id WHERE o.ord_id = ?";
//            PreparedStatement ps = conn.prepareStatement(query);
//            ps.setInt(1, Integer.parseInt((String) ordersPreparing.getValue()));
//            ResultSet rs = ps.executeQuery();
//            if(rs.next()){
//                orderPreparingTextArea.setText(
//                        "Order id: "+rs.getInt(1)+"\r\n"+
//                                "Customer name: "+rs.getString(2)+"\r\n"+
//                                "Customer address: "+rs.getString(3)+"\r\n"+
//                                "Post code: "+rs.getString(4)+"\r\n"+
//                                "Total price: "+rs.getFloat(5)+"\r\n"+
//                                "Order placed time: "+rs.getDate(6)
//                );
//            }
//        }
//    }

    public void selectDeliveringOrder(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(!ordersOnTheWay.getValue().equals("")&&!ordersOnTheWay.getItems().isEmpty()){
            Class.forName(hostInfo.getJdbcDriver());
            conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
            stmt = conn.createStatement();
            stmt.execute("use ilovepizza");

            String query = "SELECT o.ord_id,c.cus_name,p.post_code,c.cus_addr,o.total_price,o.start_ord_time FROM orders AS o JOIN post_code AS p ON o.post_id = p.post_id JOIN customer AS c ON o.cus_id = c.cus_id WHERE o.ord_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, Integer.parseInt((String) ordersOnTheWay.getValue()));
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                orderOnTheWayTextArea.setText(
                        "Order id: "+rs.getInt(1)+"\r\n"+
                                "Customer name: "+rs.getString(2)+"\r\n"+
                                "Customer address: "+rs.getString(3)+"\r\n"+
                                "Post code: "+rs.getString(4)+"\r\n"+
                                "Total price: "+rs.getFloat(5)+"\r\n"+
                                "Order placed time: "+rs.getDate(6)
                );
            }
        }
    }

//    public void takePreparedOrder(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
//        if(!ordersPreparing.getValue().equals("Orders Preparing")&&!ordersPreparing.getValue().equals("")){
//            Class.forName(hostInfo.getJdbcDriver());
//            conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
//            stmt = conn.createStatement();
//            stmt.execute("use ilovepizza");
//
//            String query = "UPDATE orders SET order_status = ?,deli_employee_id = ? WHERE ord_id = ?";
//            PreparedStatement ps = conn.prepareStatement(query);
//            ps.setString(1, "On the way");
//            ps.setInt(2,employee_id);
//            ps.setInt(3, Integer.parseInt((String) ordersPreparing.getValue()));
//            ps.executeUpdate();
//
//            setComboBoxValue();
//        }
//    }

    public void orderArrived(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        if(!ordersOnTheWay.getValue().equals("Orders on the way")&&!ordersOnTheWay.getValue().equals("")){
            Class.forName(hostInfo.getJdbcDriver());
            conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
            stmt = conn.createStatement();
            stmt.execute("use ilovepizza");

            String query = "UPDATE orders SET order_status = ? WHERE ord_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "Arrived");
            ps.setInt(2, Integer.parseInt((String) ordersOnTheWay.getValue()));
            ps.executeUpdate();

            setComboBoxValue();
        }
    }

    public void quitSystem(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) quit.getScene().getWindow();
        Scene scene = new Scene(new FXMLLoader(ilovepizzaApplication.class.getResource("loginPage.fxml")).load(),640,400);
        window.setScene(scene);
    }
}
