package com.example.demo;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class customerMenuController {
    public int post_id;
    public int cus_id;
    @FXML
    public TextArea orderTextArea;
    @FXML
    public Label totalPrice;
    @FXML
    public Button quit;
    @FXML
    public Button orderHistoryPage;
    @FXML
    public TextField isVegetarian;
    private  double totalCurrentPrice = 0.00;
    private double totalCurrentPriceAfterDiscount = 0.00;
    @FXML
    public CheckBox useCoupon;
    @FXML
    public ComboBox couponCode = new ComboBox<String>(FXCollections.observableArrayList(""));
    private ArrayList<Integer> currentPizza = new ArrayList<Integer>();

    private ArrayList<Integer> currentDessert = new ArrayList<Integer>();

    private ArrayList<Integer> currentDrink = new ArrayList<Integer>();

    private ArrayList<String> currentOrder = new ArrayList<String>();
    @FXML
    public Label menu;
    @FXML
    public Label customer;
    @FXML
    public Label customerName;
    @FXML
    public Button orderPlace;
    @FXML
    public TextField drinkDescription;
    @FXML
    public ComboBox<String> dessertMeal = new ComboBox<String>(FXCollections.observableArrayList(""));;

    @FXML
    public TextField dessertDescription;

    @FXML
    public ComboBox<String> pizzaMeal = new ComboBox<String>(FXCollections.observableArrayList(""));
    @FXML
    public TextField pizzaDescription;
    public ComboBox<String> drinkMeal = new ComboBox<String>(FXCollections.observableArrayList(""));;
    @FXML
    public Button removePizza;
    @FXML
    public Button addDessert;
    @FXML
    public Button removeDrink;
    @FXML
    public Button addDrink;
    @FXML
    public Button removeDessert;
    @FXML
    public Button addPizza;
    @FXML
    public TextField pizzaPrice;
    @FXML
    public TextField dessertPrice;
    @FXML
    public TextField drinkPrice;

//    private String pizzaMealStr = null;
//    private String dessertMealStr = null;
//    private String drinkMealStr = null;

    Connection conn;
    Statement stmt;
    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();
        stmt.execute("use ilovepizza");

        ResultSet pizza = stmt.executeQuery("select pi_name from pizza");
        while(pizza.next()){
            pizzaMeal.getItems().add(pizza.getString(1));
//                        System.out.println(pizza.getString(1));
        }

        ResultSet dessert = stmt.executeQuery("select dess_name from dessert");
        while(dessert.next()){
            dessertMeal.getItems().add(dessert.getString(1));
        }

        ResultSet drink = stmt.executeQuery("select dri_name from drink");
        while(drink.next()){
            drinkMeal.getItems().add(drink.getString(1));
        }

        stmt.close();
        conn.close();
    }



    @FXML
    protected void addPizza(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();
        stmt.execute("use ilovepizza");

        if(pizzaMeal.getValue()!=null&&pizzaMeal.getValue()!="Pizza"){
            String orderDetail = "";
            ResultSet rs = stmt.executeQuery("SELECT pi_id FROM pizza WHERE pi_name = '"+pizzaMeal.getValue()+"'");
            if(rs.next()){
                currentPizza.add(rs.getInt(1));
                currentOrder.add(pizzaMeal.getValue());
                totalCurrentPrice += Double.parseDouble(pizzaPrice.getText().substring(0,pizzaPrice.getText().length()-1));
            }
            for(int i = 0;i<currentOrder.size();i++){
                orderDetail += currentOrder.get(i)+"\r\n";
            }
            orderTextArea.setText(orderDetail);
            displayTotalPrice();
        }
    }

    public void removePizza(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();
        stmt.execute("use ilovepizza");

        if(pizzaMeal.getValue()!=null&&pizzaMeal.getValue()!="Pizza"){
            String orderDetail = "";
            ResultSet rs = stmt.executeQuery("SELECT pi_id FROM pizza WHERE pi_name = '"+pizzaMeal.getValue()+"'");
            if(rs.next()){
                currentPizza.remove(""+rs.getInt(1));
                currentOrder.remove(pizzaMeal.getValue());
                totalCurrentPrice -= Double.parseDouble(pizzaPrice.getText().substring(0,pizzaPrice.getText().length()-1));
            }
            for(int i = 0;i<currentOrder.size();i++){
                orderDetail += currentOrder.get(i)+"\r\n";
            }
            orderTextArea.setText(orderDetail);
            displayTotalPrice();
        }
    }

    public void addDessert(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();
        stmt.execute("use ilovepizza");

        if(dessertMeal.getValue()!=null&&dessertMeal.getValue()!="Dessert"){
            String orderDetail = "";
            ResultSet rs = stmt.executeQuery("SELECT dess_id FROM dessert WHERE dess_name = '"+dessertMeal.getValue()+"'");
            if(rs.next()){
                currentDessert.add(rs.getInt(1));
                currentOrder.add(dessertMeal.getValue());
                totalCurrentPrice += Double.parseDouble(dessertPrice.getText().substring(0,dessertPrice.getText().length()-1));
            }
            for(int i = 0;i<currentOrder.size();i++){
                orderDetail += currentOrder.get(i)+"\r\n";
            }
            orderTextArea.setText(orderDetail);
            displayTotalPrice();
        }
    }

    public void removeDessert(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();
        stmt.execute("use ilovepizza");
        if(dessertMeal.getValue()!=null&&dessertMeal.getValue()!="Dessert"){
            String orderDetail = "";
            ResultSet rs = stmt.executeQuery("SELECT dess_id FROM dessert WHERE dess_name = '"+dessertMeal.getValue()+"'");
            if(rs.next()){
                currentDessert.remove(""+rs.getInt(1));
                currentOrder.remove(dessertMeal.getValue());
                totalCurrentPrice -= Double.parseDouble(dessertPrice.getText().substring(0,dessertPrice.getText().length()-1));
            }
            for(int i = 0;i<currentOrder.size();i++){
                orderDetail += currentOrder.get(i)+"\r\n";
            }
            orderTextArea.setText(orderDetail);
            displayTotalPrice();
        }
    }

    public void addDrink(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();
        stmt.execute("use ilovepizza");
        if(drinkMeal.getValue()!=null&&drinkMeal.getValue()!="Drink"){
            String orderDetail = "";
            ResultSet rs = stmt.executeQuery("SELECT dri_id FROM drink WHERE dri_name = '"+drinkMeal.getValue()+"'");
            if(rs.next()){
                currentDrink.add(rs.getInt(1));
                currentOrder.add(drinkMeal.getValue());
                totalCurrentPrice += Double.parseDouble(drinkPrice.getText().substring(0,drinkPrice.getText().length()-1));
            }
            for(int i = 0;i<currentOrder.size();i++){
                orderDetail += currentOrder.get(i)+"\r\n";
            }
            orderTextArea.setText(orderDetail);
            displayTotalPrice();
        }
    }

    public void removeDrink(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();
        stmt.execute("use ilovepizza");
        if(drinkMeal.getValue()!=null&&drinkMeal.getValue()!="Drink"){
            String orderDetail = "";
            ResultSet rs = stmt.executeQuery("SELECT dri_id FROM drink WHERE dri_name = '"+drinkMeal.getValue()+"'");
            if(rs.next()){
                currentDrink.remove(""+rs.getInt(1));
                currentOrder.remove(drinkMeal.getValue());
                totalCurrentPrice -= Double.parseDouble(drinkPrice.getText().substring(0,drinkPrice.getText().length()-1));
            }
            for(int i = 0;i<currentOrder.size();i++){
                orderDetail += currentOrder.get(i)+"\r\n";
            }
            orderTextArea.setText(orderDetail);
            displayTotalPrice();
        }
    }



    public void showPizzaDetail(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        String curPizza = pizzaMeal.getValue();
        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();
        stmt.execute("use ilovepizza");

        ResultSet rs = stmt.executeQuery("SELECT pi_description,price_include_vat,if_vegetarian FROM pizza WHERE pi_name = '"+curPizza+"';");

        while(rs.next()){
            pizzaDescription.setText(rs.getString(1));
            pizzaPrice.setText(rs.getString(2)+"\u20ac");
            if(rs.getBoolean(3)){
                isVegetarian.setText("Is Vegetarian");
            }else{
                isVegetarian.setText("Isn't Vegetarian");
            }

        }
        rs.close();

        stmt.close();
        conn.close();
    }

    public void showDessertDetail(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        String curDessert = dessertMeal.getValue();
        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();
        stmt.execute("use ilovepizza");

        ResultSet rs = stmt.executeQuery("SELECT dess_description,dess_price FROM dessert WHERE dess_name = '"+curDessert+"';");

        while(rs.next()){
            dessertDescription.setText(rs.getString(1));
            dessertPrice.setText(rs.getString(2)+"\u20ac");
        }

        rs.close();

        stmt.close();
        conn.close();
    }

    public void showDrinkDetail(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {

        String curDrink = drinkMeal.getValue();
        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();
        stmt.execute("use ilovepizza");

        ResultSet rs = stmt.executeQuery("SELECT dri_description,dri_price FROM drink WHERE dri_name = '"+curDrink+"';");

        while(rs.next()){
            drinkDescription.setText(rs.getString(1));
            drinkPrice.setText(rs.getString(2)+"\u20ac");
        }
        rs.close();

        stmt.close();
        conn.close();

    }

    @FXML
    public void placeOrder(ActionEvent actionEvent) throws ClassNotFoundException, SQLException, IOException {
        if(currentPizza.isEmpty()){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please add at least one pizza.");
            a.show();
            return;
        }
        if(!orderTextArea.getText().isEmpty()){
            Class.forName(hostInfo.getJdbcDriver());
            conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
            stmt = conn.createStatement();
            stmt.execute("use ilovepizza");

            for(int i = 0;i<currentPizza.size();i++){
                stmt.execute("INSERT INTO pi_order (pi_id) VALUES ("+currentPizza.get(i)+")");
            }
            for(int i = 0;i<currentDessert.size();i++){
                stmt.execute("INSERT INTO dess_order (dess_id) VALUES ("+currentDessert.get(i)+")");
            }
            for(int i = 0;i<currentDrink.size();i++){
                stmt.execute("INSERT INTO dri_order (dri_id) VALUES ("+currentDrink.get(i)+")");
            }
                if(useCoupon.isSelected()){
                    ResultSet codeRs = stmt.executeQuery("SELECT code_id FROM discount_code WHERE code = '"+couponCode.getValue()+"'");
                    if(codeRs.next()){
                        stmt.execute("INSERT INTO orders (post_id,discount,cus_id,total_price,pi_order,dess_order,dri_order,code_id) VALUES ("
                                +post_id+","
                                +Float.parseFloat("0.9")+","
                                +cus_id+","
                                +Float.parseFloat(totalPrice.getText())+","
                                +currentPizza.size()+","
                                +currentDessert.size()+","
                                +currentDrink.size()+","
                                +codeRs.getInt(1)
                                +")");
                    }

                }else{
                    stmt.execute("INSERT INTO orders (post_id,cus_id,total_price,pi_order,dess_order,dri_order) VALUES ("
                            +post_id+","
                            +cus_id+","
                            +Float.parseFloat(totalPrice.getText())+","
                            +currentPizza.size()+","
                            +currentDessert.size()+","
                            +currentDrink.size()
                            +")");
                }
            if(useCoupon.isSelected()){
                String query = "update discount_code set if_used = ? where code = ? ";
                PreparedStatement preStmt = conn.prepareStatement(query);
                preStmt.setBoolean(1, true);
                preStmt.setString(2, (String) couponCode.getValue());
                preStmt.executeUpdate();
            }

            //TODO: update total_pi_order nums
                ResultSet curRs = stmt.executeQuery("SELECT total_pi_order FROM customer WHERE cus_id ="+cus_id);
                if(curRs.next()){
                    int total_pi_order = curRs.getInt(1);
                    if(total_pi_order>=10){
                        String randomCode = createRandomCode(6);
                        stmt.execute("INSERT INTO discount_code (if_used,code) VALUES (false,'"+randomCode+"')");
                        ResultSet codeSet = stmt.executeQuery("SELECT code_id FROM discount_code WHERE code = '"+randomCode+"'");
                        if(codeSet.next()){
                            stmt.execute("INSERT INTO customer_discount VALUES ("+codeSet.getInt(1)+","+cus_id+")");
                        }

                        String query = "UPDATE customer SET total_pi_order = ? WHERE cus_id=? ";
                        PreparedStatement preStmt = conn.prepareStatement(query);
                        preStmt.setInt(1, total_pi_order+currentPizza.size()-10);
                        preStmt.setInt(2, cus_id);
                        preStmt.executeUpdate();

                        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                        a.setContentText("You got a discount code: "+randomCode);
                        a.show();
                    }else{
                        String query = "UPDATE customer SET total_pi_order = ? WHERE cus_id=? ";
                        PreparedStatement preStmt = conn.prepareStatement(query);
                        preStmt.setInt(1, total_pi_order+currentPizza.size());
                        preStmt.setInt(2, cus_id);
                        preStmt.executeUpdate();
                    }
                }

                Stage window  = (Stage) orderPlace.getScene().getWindow();
                Scene scene = new Scene(new FXMLLoader(ilovepizzaApplication.class.getResource("loginPage.fxml")).load(),640,400);
                window.setScene(scene);
                getScheduledTime();

        }else{
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please add at least one item.");
            a.show();
        }
    }



    public void getScheduledTime() throws ClassNotFoundException, SQLException {
        int orderNum = 1;
        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();
        stmt.execute("use ilovepizza");

        String query = "SELECT ord_id FROM orders AS o WHERE o.order_status = 'preparing' AND o.post_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1,post_id);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            orderNum++;
        }
        Alert b = new Alert(Alert.AlertType.CONFIRMATION);
        String orderDetails = "";
        for(int i = 0;i<currentOrder.size();i++){
            orderDetails += currentOrder.get(i)+"\r\n";
        }
        b.setContentText(
                "Your order is preparing...\r\n"
                        +"Order details: "+"\r\n"
                        +orderDetails
                        +"Total price: "+totalPrice.getText()+"\r\n"
                        +"Your order will be delivered after : "+ orderNum*0.5+" hours"
        );
        b.show();
    }

    @FXML
    public void useCoupon(ActionEvent actionEvent){
        displayTotalPrice();
    }

    public void displayTotalPrice() {
        if(useCoupon.isSelected()){
//            System.out.println(totalCurrentPrice);
            try{
                if(!couponCode.getValue().equals("Code")){
                    totalCurrentPriceAfterDiscount = totalCurrentPrice*0.9;
                    totalPrice.setText(String.format("%.2f",totalCurrentPriceAfterDiscount));
                }
            }catch (Exception e){

                System.out.println(e);
            }

        }else{
//            System.out.println(totalCurrentPrice);
            totalPrice.setText(String.format("%.2f",totalCurrentPrice));
        }



    }

    /**
     * @param length
     * @return
     */
    public static String createRandomCode(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            stringBuffer.append(str.charAt(number));
        }
        return stringBuffer.toString();
    }

    public void quitSystem(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) quit.getScene().getWindow();
        Scene scene = new Scene(new FXMLLoader(ilovepizzaApplication.class.getResource("loginPage.fxml")).load(),640,400);
        window.setScene(scene);
    }

    public void goToOrderHistoryPage(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) orderHistoryPage.getScene().getWindow();
        FXMLLoader loader =  new FXMLLoader(ilovepizzaApplication.class.getResource("orderHistory.fxml"));
        Parent parent =loader.load();
        orderHistoryController oHC = loader.getController();
        oHC.cus_id = this.cus_id;
        oHC.cus_name = this.customerName.getText();
        oHC.customerName.setText(this.customerName.getText());
        Scene scene = new Scene(parent,600,400);
        window.setScene(scene);
    }
}
