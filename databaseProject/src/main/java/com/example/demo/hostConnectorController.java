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
import java.sql.*;
import java.text.DecimalFormat;
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
    private boolean hostFind = false;

    hostInfo host;

    private float [] currentPizzaPrice = new float[0];
    private boolean vegetarian;

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
    protected void connect() throws IOException {
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter(new File("src/main/resources/hostInfo.csv")));
            bf.write(hostName.getText()+","+password.getText());
            bf.flush();
            bf.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        init();
        if(hostFind){
            Stage window = (Stage) connector.getScene().getWindow();
            Scene scene = new Scene(new FXMLLoader(ilovepizzaApplication.class.getResource("loginPage.fxml")).load());
            window.setScene(scene);
        }

    }
    @FXML
    protected void backToLoginPageHostConnector() throws IOException {
        Stage window = (Stage) backToLoginPageHostConnector.getScene().getWindow();
        Scene scene = new Scene(new FXMLLoader(ilovepizzaApplication.class.getResource("loginPage.fxml")).load());
        window.setScene(scene);
    }

    public float[] getPizzaPrice(int pizzaid) throws ClassNotFoundException, SQLException {

        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();

        ResultSet rs =  stmt.executeQuery("SELECT SUM(ingre_price) FROM ingredient JOIN pi_ingre ON ingredient.ingre_id = pi_ingre.ingre_id WHERE pi_ingre.pi_id = "+pizzaid+";");
        float margin = 0;
        float price_include_vat = 0;
        float price = 0;
        while(rs.next()){
            margin = rs.getFloat(1);
        }


        DecimalFormat format = new DecimalFormat( "0.00 ");



        margin = Float.parseFloat(format.format(margin));
        price = Float.parseFloat(format.format((margin*1.4)));
        price_include_vat = Float.parseFloat(format.format((price*1.09)));

        float [] returnArray = {margin, price, price_include_vat};
        return returnArray;
    }

    public boolean checkIfVegetarian(int pizzaID) throws ClassNotFoundException, SQLException {
        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();

        String query = "SELECT i.if_vegetarian FROM ingredient AS i JOIN pi_ingre AS pi ON i.ingre_id = pi.ingre_id WHERE pi.pi_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1,pizzaID);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            if(!rs.getBoolean(1)){
                return false;
            }
        }
        return true;
    }

//    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        hostConnectorController hs = new hostConnectorController();
//        System.out.println(hs.checkIfVegetarian(3));
//    }



    public void init(){
        try {
            Class.forName(hostInfo.getJdbcDriver());
            conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
            stmt = conn.createStatement();
            stmt.execute("DROP DATABASE IF EXISTS ilovepizza");
            stmt.execute("CREATE DATABASE ilovepizza");
            stmt.execute("use ilovepizza");


            //creating tables (done)
            stmt.execute("CREATE TABLE ingredient(ingre_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, ingre_name varchar(30), ingre_price float,if_vegetarian boolean)");
            stmt.execute("CREATE TABLE pi_ingre(pi_id int NOT NULL , ingre_id int)");
            stmt.execute("CREATE TABLE pizza(pi_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, pi_name varchar(60), if_vegetarian boolean,margin float,pi_price float, price_include_vat float,pi_description varchar(255))");
            stmt.execute("CREATE TABLE pi_order(pi_id int, ord_id int NOT NULL PRIMARY KEY AUTO_INCREMENT)");
            stmt.execute("CREATE TABLE dessert(dess_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, dess_name varchar(60), dess_price float,dess_description varchar(255))");
            stmt.execute("CREATE TABLE dess_order(dess_id int, ord_id int NOT NULL PRIMARY KEY AUTO_INCREMENT)");
            stmt.execute("CREATE TABLE drink(dri_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, dri_name varchar(255), dri_description varchar(255), dri_price float)");
            stmt.execute("CREATE TABLE dri_order(dri_id int, ord_id int NOT NULL PRIMARY KEY AUTO_INCREMENT)");
            stmt.execute("CREATE TABLE discount_code(code_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, if_used boolean default false, code varchar(6))");
            stmt.execute("CREATE TABLE customer(cus_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, cus_name varchar(255),cus_password varchar(255), cus_tele varchar(255), cus_addr varchar(255),post_id int, total_pi_order int default 0)");
            stmt.execute("CREATE TABLE post_code(post_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, post_code varchar(60))");
            stmt.execute("CREATE TABLE orders(ord_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, discount float default 1, order_status varchar(255) default 'preparing', cus_id int, total_price float,start_ord_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,start_deli_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,post_id int,deli_employee_id int,pi_order int,dess_order int, dri_order int,code_id int default null)");
            stmt.execute("CREATE TABLE delivery_employee(deli_employee_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, deli_employee_name varchar(255),employee_password varchar(255), post_id int, if_delivering boolean default false)");
            stmt.execute("CREATE TABLE customer_discount(code_id int, cus_id int)");


            //insert values
            //ingredients
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('tomato sauce',1.12,true)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES (' domino pepperoni',0.32,false)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('fresh tomatoes',0.51,true)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('kebab meat',1.20,false)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('kebab sauce',0.20,true)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('gherkins',0.30,true)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('burger sauce',0.20,true)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('mozzarella',0.92,true)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('premium beef',1.23,false)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('hot salami',1.50,false)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('jalapeno',0.70,true)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('cheddar',0.40,false)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('shepard cheese',0.60,false)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('blue cheese',0.60,false)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('white cremefine',1.32,false)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('grilled chicken',1.41,false)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('bacon',1.50,false)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('red onion',0.73,true)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('corn',0.50,true)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('peppers',0.23,true)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('mixed peppers',0.23,true)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('mushroooms',0.82,true)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('black olives',0.80,true)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('bbq sauce',0.38,true)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('sweat corn',0.50,true)");
            stmt.execute("INSERT INTO ingredient (ingre_name,ingre_price,if_vegetarian) VALUES ('hot sauce',0.23,true)");



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



            //stmt = conn.createStatement();
            //stmt.execute("use ilovepizza");

            //calculate pizza price

            //pizza
            currentPizzaPrice = getPizzaPrice(1);
            vegetarian = checkIfVegetarian(1);
            stmt.execute("INSERT INTO pizza VALUES (1,'Pepperoni Passion',"+vegetarian+","+currentPizzaPrice[0]+","+currentPizzaPrice[1]+","+currentPizzaPrice[2]+",'The tomato sauce was rich and tasty and the extra cheese piled on really gave satisfaction with it’s indulgence and it was chewy.')");
            currentPizzaPrice = getPizzaPrice(2);
            vegetarian = checkIfVegetarian(2);
            stmt.execute("INSERT INTO pizza VALUES (2,'Kebab Pizza',"+vegetarian+","+currentPizzaPrice[0]+","+currentPizzaPrice[1]+","+currentPizzaPrice[2]+",'The kebab sauce in Sweden is a mixture of yogurt or sour cream and various spices.')");
            currentPizzaPrice = getPizzaPrice(3);
            vegetarian = checkIfVegetarian(3);
            stmt.execute("INSERT INTO pizza VALUES (3,'Chickenburger Pizza',"+vegetarian+","+currentPizzaPrice[0]+","+currentPizzaPrice[1]+","+currentPizzaPrice[2]+",'Chickenburger Pizza loaded with the goodness of burger and the greatness of pizza.')");
            currentPizzaPrice = getPizzaPrice(4);
            vegetarian = checkIfVegetarian(4);
            stmt.execute("INSERT INTO pizza VALUES (4,'Burger Pizza',"+vegetarian+","+currentPizzaPrice[0]+","+currentPizzaPrice[1]+","+currentPizzaPrice[2]+",'Bite into delight! Witness the epic combination of pizza and burger with our classic Burger Pizza, that looks good and tastes great!')");
            currentPizzaPrice = getPizzaPrice(5);
            vegetarian = checkIfVegetarian(5);
            stmt.execute("INSERT INTO pizza VALUES (5,'Diavolo',"+vegetarian+","+currentPizzaPrice[0]+","+currentPizzaPrice[1]+","+currentPizzaPrice[2]+",'Looking to amp up your pizza night? This spicy pizza diavola is a fan favorite starring Kalamata olives, spicy peppers, and gooey mozzarella cheese.')");
            currentPizzaPrice = getPizzaPrice(6);
            vegetarian = checkIfVegetarian(6);
            stmt.execute("INSERT INTO pizza VALUES (6,'Quattro Formaggi',"+vegetarian+","+currentPizzaPrice[0]+","+currentPizzaPrice[1]+","+currentPizzaPrice[2]+",'Pizza quattro formaggi is a variety of pizza in Italian cuisine that is topped with a combination of four kinds of cheese, melted together. ')");
            currentPizzaPrice = getPizzaPrice(7);
            vegetarian = checkIfVegetarian(7);
            stmt.execute("INSERT INTO pizza VALUES (7,'Carbonara',"+vegetarian+","+currentPizzaPrice[0]+","+currentPizzaPrice[1]+","+currentPizzaPrice[2]+",'Topped with crisp pieces of salt bacon and a perfectly cooked egg (runny yolk is a must).')");
            currentPizzaPrice = getPizzaPrice(8);
            vegetarian = checkIfVegetarian(8);
            stmt.execute("INSERT INTO pizza VALUES (8,'Tex Mex Chicken',"+vegetarian+","+currentPizzaPrice[0]+","+currentPizzaPrice[1]+","+currentPizzaPrice[2]+",'Red onion add toasty sweetness to this taco-inspired dish that comes together fast with grilled chicken and a pre-baked crust.')");
            currentPizzaPrice = getPizzaPrice(9);
            vegetarian = checkIfVegetarian(9);
            stmt.execute("INSERT INTO pizza VALUES (9,'Vegetariana',"+vegetarian+","+currentPizzaPrice[0]+","+currentPizzaPrice[1]+","+currentPizzaPrice[2]+",'Pizza vegetariana can easily become a vegan-friendly dish by omitting the meat.')");
            currentPizzaPrice = getPizzaPrice(10);
            vegetarian = checkIfVegetarian(10);
            stmt.execute("INSERT INTO pizza VALUES (10,'BBQ Chicken',"+vegetarian+","+currentPizzaPrice[0]+","+currentPizzaPrice[1]+","+currentPizzaPrice[2]+",'This is a recipe for classic BBQ chicken pizza with tangy BBQ sauce, chicken, and red onion. BBQ sauce gives the pizza the most delicious smoky flavor.')");
            currentPizzaPrice = getPizzaPrice(11);
            vegetarian = checkIfVegetarian(11);
            stmt.execute("INSERT INTO pizza VALUES (11,'BBQ Texas Beef',"+vegetarian+","+currentPizzaPrice[0]+","+currentPizzaPrice[1]+","+currentPizzaPrice[2]+",'These thick chunks of premium beef are sauced with BBQ sauce, topped with mozzarella, red onion, finished with sweet corn for what is going to be your new favorite pizza.')");

            //dessert
            stmt.execute("INSERT INTO dessert VALUES (1,'Cinnabites',3.30,'Breadsticks with cinnamon & honey.')");
            stmt.execute("INSERT INTO dessert VALUES (2,'Choco Pie',4.40,'Puff Pastry stuffed with chocolate cream.')");
            stmt.execute("INSERT INTO dessert VALUES (3,'Lave cake',3.50,'hocolate cake with warm liquid chocolate center.')");
            stmt.execute("INSERT INTO dessert VALUES (4,'Brownie',2.80,'Chocolate cake with fudgy dough and pecan pieces')");

            //f

            //drink
            stmt.execute("INSERT INTO drink VALUES (1,'Coke','original taste coca-cola',2.60)");
            stmt.execute("INSERT INTO drink VALUES (2,'Coke Zero','coca-cola with zero sugar',2.60)");
            stmt.execute("INSERT INTO drink VALUES (3,'Cappy Orange G‘sprizt','a kind of orange juice drink',2.60)");
            stmt.execute("INSERT INTO drink VALUES (4,'RED BULL','energy drink, useful when you feel tired',3.30)");
            stmt.execute("INSERT INTO drink VALUES (5,'Fuzetea Lemon Lemongrass', 'tea drink, refreshing taste',2.60)");
            stmt.execute("INSERT INTO drink VALUES (6,'Sprite','lemon flavoured carbonated drinks',2.60)");
            stmt.execute("INSERT INTO drink VALUES (7,'JACKIE WELLES','A shot of vodka, lime juice, ginger beer and, most importantly… a splash of love.',150)");
            stmt.execute("INSERT INTO drink VALUES (8,'THE DAVID MARTINEZ','A shot of vodka on the rocks, tapped with Nicola, Aim high and go out with a bang. The carbonated drinks are for lucy, David doesnt like them.',150)");
            System.out.println("reached");
            hostFind = true;
            stmt.close();
            conn.close();

//            Stage window = (Stage) connector.getScene().getWindow();
//            Scene scene = new Scene(new FXMLLoader(ilovepizzaApplication.class.getResource("loginPage.fxml")).load(),640,400);
//            window.setScene(scene);

            //stmt.execute("");
        } catch (Exception e) {
            System.out.println(e.toString());
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("hostname wrong or password wrong");
            a.show();
        }
    }


}
