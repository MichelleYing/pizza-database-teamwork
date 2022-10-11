package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;


public class hostConnectorController {
    @FXML
    protected Label backToLoginPageHostConnector;
    @FXML
    protected TextField hostName;
    @FXML
    protected PasswordField password;
    @FXML
    protected Button connector;

    hostInfo host;

    Connection conn;
    Statement stmt;

//    public static void main(String[] args) {
//        try {
//            Scanner scan = new Scanner(System.in);
//            BufferedWriter bf = new BufferedWriter(new FileWriter(new File("src/main/resources/hostInfo.csv")));
//            bf.write(scan.next()+","+scan.next());
//            bf.flush();
//            bf.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
    @FXML
    protected void connect(){
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter(new File("src/main/resources/hostInfo.csv")));
            bf.write(hostName.getText()+","+password.getText());
            bf.flush();
            bf.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        init();
    }
    @FXML
    protected void backToLoginPageHostConnector() throws IOException {
        Stage window = (Stage) backToLoginPageHostConnector.getScene().getWindow();
        Scene scene = new Scene(new FXMLLoader(ilovepizzaApplication.class.getResource("loginPage.fxml")).load());
        window.setScene(scene);
    }

    public void init(){

        try {
            Class.forName(hostInfo.getJdbcDriver());
            conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
            stmt = conn.createStatement();
            stmt.execute("DROP DATABASE IF EXISTS ilovepizza");
            stmt.execute("CREATE DATABASE ilovepizza");
            stmt.execute("use ilovepizza");

            //creating tables
            stmt.execute("CREATE TABLE orders(ord_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, discount float, order_status varchar(255), cus_id int, postal_code varchar(255), total_price float,ord_time DATETIME,start_deli_time DATETIME)");
            stmt.execute("CREATE TABLE delivery_employee(deli_employee_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, deli_employee_name varchar(255),employee_password varchar(255), ord_id int)");
            stmt.execute("CREATE TABLE customer(cus_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, cus_name varchar(255),cus_password varchar(255), cus_tele varchar(255), cus_addr varchar(255),cus_post_code varchar(255), total_pi_order int)");
            stmt.execute("CREATE TABLE dess_menu_order(dess_item_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, ord_id int)");
            stmt.execute("CREATE TABLE discount_code(code_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, has_given boolean, if_used boolean, code varchar(255))");
            stmt.execute("CREATE TABLE dri_menu_order(dir_item_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, ord_id int)");
            stmt.execute("CREATE TABLE dri_menu_item(dri_item_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, dri_id int, dri_price float)");
            stmt.execute("CREATE TABLE drink(dri_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, dri_name varchar(255), dri_description varchar(255))");
            stmt.execute("CREATE TABLE dess_menu_item(dess_item_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, dess_id int, dess_price float)");
            stmt.execute("CREATE TABLE dessert(dess_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, dess_name varchar(60))");
            stmt.execute("CREATE TABLE pi_menu_order(pi_item_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, ord_id int)");
            stmt.execute("CREATE TABLE pi_menu_item(pi_item_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, pi_id int, margin int, pi_price float, price_include_vat float)");
            stmt.execute("CREATE TABLE pizza(pi_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, pi_name varchar(60), if_vegetarian boolean)");
            stmt.execute("CREATE TABLE ingredient(ingre_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, ingre_name varchar(30), ingre_price float)");
            stmt.execute("CREATE TABLE pi_ingre(pi_id int NOT NULL , ingre_id int)");

            //insert values
            //ingredients
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (1,'tomato sauce',1.12)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (2,' domino pepperoni',0.32)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (3,'fresh tomatoes',0.51)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (4,'kebab meat',1.20)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (5,'kebab sauce',0.20)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (6,'gherkins',0.30)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (7,'burger sauce',0.20)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (8,'mozzarella',0.92)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (9,'premium beef',1.23)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (10,'hot salami',1.50)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (11,'jalapeno',0.70)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (12,'cheddar',0.40)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (13,'shepard cheese',0.60)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (14,'blue cheese',0.60)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (15,'white cremefine',1.32)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (16,'grilled chicken',1.41)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (17,'bacon',1.50)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (18,'red onion',0.73)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (19,'corn',0.50)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (20,'peppers',0.23)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (21,'mixed peppers',0.23)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (22,'mushroooms',0.82)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (23,'black olives',0.80)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (24,'bbq sauce',0.38)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (25,'sweat corn',0.50)");
            stmt.execute("INSERT INTO ingredient (ingre_id,ingre_name,ingre_price) VALUES (26,'hot sauce',0.23)");

            //pizza
            stmt.execute("INSERT INTO pizza VALUES (1,'Pepperoni Passion',false)");
            stmt.execute("INSERT INTO pizza VALUES (2,'Kebab Pizza',false)");
            stmt.execute("INSERT INTO pizza VALUES (3,'Chickenburger Pizza',false)");
            stmt.execute("INSERT INTO pizza VALUES (4,'Burger Pizza',false)");
            stmt.execute("INSERT INTO pizza VALUES (5,'Diavolo',false)");
            stmt.execute("INSERT INTO pizza VALUES (6,'Quattro Formaggi',false)");
            stmt.execute("INSERT INTO pizza VALUES (7,'Carbonara',false)");
            stmt.execute("INSERT INTO pizza VALUES (8,'Tex Mex Chicken',false)");
            stmt.execute("INSERT INTO pizza VALUES (9,'Vegetariana',true)");
            stmt.execute("INSERT INTO pizza VALUES (10,'BBQ Chicken',false)");
            stmt.execute("INSERT INTO pizza VALUES (11,'BBQ Texas Beef',false)");

            //pi_ingre
            stmt.execute("INSERT INTO pi_ingre VALUES (1,1)");
            stmt.execute("INSERT INTO pi_ingre VALUES (1,2)");
            stmt.execute("INSERT INTO pi_ingre VALUES (1,8)");
            stmt.execute("INSERT INTO pi_ingre VALUES (2,1)");
            stmt.execute("INSERT INTO pi_ingre VALUES (2,8)");
            stmt.execute("INSERT INTO pi_ingre VALUES (2,18)");
            stmt.execute("INSERT INTO pi_ingre VALUES (2,3)");
            stmt.execute("INSERT INTO pi_ingre VALUES (2,4)");
            stmt.execute("INSERT INTO pi_ingre VALUES (2,5)");
            stmt.execute("INSERT INTO pi_ingre VALUES (3,1)");
            stmt.execute("INSERT INTO pi_ingre VALUES (3,8)");
            stmt.execute("INSERT INTO pi_ingre VALUES (3,16)");
            stmt.execute("INSERT INTO pi_ingre VALUES (3,18)");
            stmt.execute("INSERT INTO pi_ingre VALUES (3,3)");
            stmt.execute("INSERT INTO pi_ingre VALUES (3,6)");
            stmt.execute("INSERT INTO pi_ingre VALUES (3,12)");
            stmt.execute("INSERT INTO pi_ingre VALUES (3,7)");
            stmt.execute("INSERT INTO pi_ingre VALUES (4,1)");
            stmt.execute("INSERT INTO pi_ingre VALUES (4,8)");
            stmt.execute("INSERT INTO pi_ingre VALUES (4,9)");
            stmt.execute("INSERT INTO pi_ingre VALUES (4,18)");
            stmt.execute("INSERT INTO pi_ingre VALUES (4,3)");
            stmt.execute("INSERT INTO pi_ingre VALUES (4,6)");
            stmt.execute("INSERT INTO pi_ingre VALUES (4,12)");
            stmt.execute("INSERT INTO pi_ingre VALUES (4,7)");
            stmt.execute("INSERT INTO pi_ingre VALUES (5,1)");
            stmt.execute("INSERT INTO pi_ingre VALUES (5,8)");
            stmt.execute("INSERT INTO pi_ingre VALUES (5,10)");
            stmt.execute("INSERT INTO pi_ingre VALUES (5,11)");
            stmt.execute("INSERT INTO pi_ingre VALUES (5,18)");
            stmt.execute("INSERT INTO pi_ingre VALUES (6,1)");
            stmt.execute("INSERT INTO pi_ingre VALUES (6,8)");
            stmt.execute("INSERT INTO pi_ingre VALUES (6,12)");
            stmt.execute("INSERT INTO pi_ingre VALUES (6,13)");
            stmt.execute("INSERT INTO pi_ingre VALUES (6,14)");
            stmt.execute("INSERT INTO pi_ingre VALUES (6,15)");
            stmt.execute("INSERT INTO pi_ingre VALUES (6,8)");
            stmt.execute("INSERT INTO pi_ingre VALUES (6,16)");
            stmt.execute("INSERT INTO pi_ingre VALUES (6,17)");
            stmt.execute("INSERT INTO pi_ingre VALUES (6,18)");
            stmt.execute("INSERT INTO pi_ingre VALUES (7,15)");
            stmt.execute("INSERT INTO pi_ingre VALUES (7,8)");
            stmt.execute("INSERT INTO pi_ingre VALUES (7,16)");
            stmt.execute("INSERT INTO pi_ingre VALUES (7,17)");
            stmt.execute("INSERT INTO pi_ingre VALUES (7,18)");
            stmt.execute("INSERT INTO pi_ingre VALUES (8,1)");
            stmt.execute("INSERT INTO pi_ingre VALUES (8,8)");
            stmt.execute("INSERT INTO pi_ingre VALUES (8,16)");
            stmt.execute("INSERT INTO pi_ingre VALUES (8,19)");
            stmt.execute("INSERT INTO pi_ingre VALUES (8,18)");
            stmt.execute("INSERT INTO pi_ingre VALUES (8,20)");
            stmt.execute("INSERT INTO pi_ingre VALUES (9,1)");
            stmt.execute("INSERT INTO pi_ingre VALUES (9,8)");
            stmt.execute("INSERT INTO pi_ingre VALUES (9,18)");
            stmt.execute("INSERT INTO pi_ingre VALUES (9,21)");
            stmt.execute("INSERT INTO pi_ingre VALUES (9,22)");
            stmt.execute("INSERT INTO pi_ingre VALUES (9,23)");
            stmt.execute("INSERT INTO pi_ingre VALUES (9,19)");
            stmt.execute("INSERT INTO pi_ingre VALUES (10,24)");
            stmt.execute("INSERT INTO pi_ingre VALUES (10,8)");
            stmt.execute("INSERT INTO pi_ingre VALUES (10,16)");
            stmt.execute("INSERT INTO pi_ingre VALUES (10,18)");
            stmt.execute("INSERT INTO pi_ingre VALUES (10,17)");
            stmt.execute("INSERT INTO pi_ingre VALUES (11,24)");
            stmt.execute("INSERT INTO pi_ingre VALUES (11,8)");
            stmt.execute("INSERT INTO pi_ingre VALUES (11,9)");
            stmt.execute("INSERT INTO pi_ingre VALUES (11,18)");
            stmt.execute("INSERT INTO pi_ingre VALUES (11,21)");
            stmt.execute("INSERT INTO pi_ingre VALUES (11,25)");
            stmt.execute("INSERT INTO pi_ingre VALUES (11,26)");

            //dessert
            stmt.execute("INSERT INTO dessert VALUES (1,'Cinnabites')");
            stmt.execute("INSERT INTO dessert VALUES (2,'Choco Pie')");
            stmt.execute("INSERT INTO dessert VALUES (3,'Lave cake')");
            stmt.execute("INSERT INTO dessert VALUES (4,'Brownie')");

            //f

            //drink
            stmt.execute("INSERT INTO drink VALUES (1,'Coke','original taste coca-cola')");
            stmt.execute("INSERT INTO drink VALUES (2,'Coke Zero','coca-cola with zero sugar')");
            stmt.execute("INSERT INTO drink VALUES (3,'Cappy Orange G‘sprizt','a kind of orange juice drink')");
            stmt.execute("INSERT INTO drink VALUES (4,'RED BULL','energy drink, useful when you feel tired')");
            stmt.execute("INSERT INTO drink VALUES (5,'Fuzetea Lemon Lemongrass', 'tea drink, refreshing taste')");
            stmt.execute("INSERT INTO drink VALUES (6,'Sprite','lemon flavoured carbonated drinks')");
            stmt.execute("INSERT INTO drink VALUES (7,'JACKIE WELLES','A shot of vodka, lime juice, ginger beer and, most importantly… a splash of love.')");
            stmt.execute("INSERT INTO drink VALUES (8,'THE DAVID MARTINEZ','A shot of vodka on the rocks, tapped with Nicola, Aim high and go out with a bang. The carbonated drinks are for lucy, David doesnt like them.')");
            System.out.println("reached");
            stmt.close();
            conn.close();

            Stage window = (Stage) connector.getScene().getWindow();
            Scene scene = new Scene(new FXMLLoader(ilovepizzaApplication.class.getResource("loginPage.fxml")).load(),640,400);
            window.setScene(scene);

            //stmt.execute("");
        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("hostname wrong or password wrong");
            a.show();
        }
    }


}
