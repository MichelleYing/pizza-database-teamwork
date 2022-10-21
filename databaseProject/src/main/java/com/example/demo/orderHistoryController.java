package com.example.demo;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

import java.sql.*;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class orderHistoryController {
    public int cus_id;
    public String cus_name;
    public ComboBox orders = new ComboBox<String>(FXCollections.observableArrayList(""));
    public Button cancelOrder;
    public TextArea orderTextArea;
    public Label customerName;
    public Label goBackToMenu;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public Button queryOrder;
    private Connection conn;
    private Statement stmt;

    @FXML
    public void initialize(){
        cancelOrder.setVisible(false);
    }


    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diff = date2.getTime() - date1.getTime();
        return timeUnit.convert(diff,TimeUnit.MILLISECONDS);
    }

//    public static void main(String[] args) throws ParseException {
//        String str = "2022-10-17 13:10:42";
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date1 = (Date) format.parse(str);
//        Date date2 = new Date();
//        System.out.println(getDateDiff(date1,date2,TimeUnit.MINUTES));
//        if(getDateDiff(date1,date2,TimeUnit.MINUTES)<5){
//            System.out.println("true");
//        }else{
//            System.out.println("false");
//        }
//    }

    public void backToMenu(MouseEvent mouseEvent) throws IOException {
        Stage window = (Stage) goBackToMenu.getScene().getWindow();
        Scene scene = new Scene(new FXMLLoader(ilovepizzaApplication.class.getResource("loginPage.fxml")).load(),640,400);
        window.setScene(scene);
    }

    public void showOrderDetail(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, ParseException {
        if(!orders.getValue().equals("")){
            orderTextArea.clear();
            Class.forName(hostInfo.getJdbcDriver());
            conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
            stmt = conn.createStatement();
            stmt.execute("use ilovepizza");

            String ord_time = "1900-00-00 00:00:00";
            String query = "SELECT o.start_ord_time,o.start_deli_time,o.order_status,o.total_price,p.post_code FROM orders AS o JOIN post_code AS p ON p.post_id = o.post_id WHERE o.start_ord_time = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, String.valueOf(orders.getValue()));
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ord_time = rs.getString(1);
                    orderTextArea.setText(
                            "Order placed time: "+rs.getDate(1)+"\r\n"
                                    +"Star delivery time: "+rs.getDate(2)+"\r\n"
                                    +"Order status: "+rs.getString(3)+"\r\n"
                                    +"Total price: "+rs.getFloat(4)+"\r\n"
                                    +"Post code: "+rs.getString(5)+"\r\n"
                    );
            }

            String query1 = "SELECT d.deli_employee_name FROM orders AS o JOIN delivery_employee AS d ON d.deli_employee_id = o.deli_employee_id WHERE o.start_ord_time = ?";
            PreparedStatement preStmt = conn.prepareStatement(query1);
            preStmt.setString(1,String.valueOf(orders.getValue()));
            ResultSet rs1 = preStmt.executeQuery();
            if(rs1.next()){
                orderTextArea.setText(
                        orderTextArea.getText()
                        +"delivery man name: "+ rs1.getString(1)
                );
            }else{
                orderTextArea.setText(
                        orderTextArea.getText()
                        +"delivery man name: NULL"
                );
            }



            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start_ord_time = format.parse(ord_time);
            Date currentTime = new Date();
            if(getDateDiff(start_ord_time,currentTime,TimeUnit.MINUTES)<5){
                cancelOrder.setVisible(true);
            }
        }
    }

    public void cancelOrder(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();
        stmt.execute("use ilovepizza");

        String delete = "DELETE FROM orders AS o WHERE o.start_ord_time = ?";
        PreparedStatement ps = conn.prepareStatement(delete);
        ps.setString(1, String.valueOf(orders.getValue()));
        ps.executeUpdate();
        queryOrder(new ActionEvent());
    }

    public void queryOrder(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        orders.setItems(FXCollections.observableArrayList(""));

        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();
        stmt.execute("use ilovepizza");

        String query = "SELECT o.start_ord_time FROM orders AS o WHERE o.start_ord_time BETWEEN ? AND ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, String.valueOf(startDatePicker.getValue()));
        ps.setString(2, String.valueOf(endDatePicker.getValue()));
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            orders.getItems().add(rs.getString(1));
        }
    }
}
