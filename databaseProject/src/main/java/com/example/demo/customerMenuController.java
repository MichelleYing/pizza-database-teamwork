package com.example.demo;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.*;

public class customerMenuController {
    @FXML
    public Label menu;
    @FXML
    public Label customer;
    @FXML
    public Label customerName;
    @FXML
    public TextField orderTextArea;
    @FXML
    public Button orderPlace;
    @FXML
    public TextField drinkDescription;
    @FXML
    public ComboBox<String> dessertMeal= new ComboBox<String>(FXCollections.observableArrayList(""));;

    @FXML
    public TextField dessertDescription;


    public ComboBox<String> pizzaMeal = new ComboBox<String>(FXCollections.observableArrayList(""));
    public TextField pizzaDescription;
    public ComboBox<String> drinkMeal= new ComboBox<String>(FXCollections.observableArrayList(""));;
    public Button removePizza;
    public Button addDessert;
    public Button removeDrink;
    public Button addDrink;
    public Button removeDessert;
    public Button addPizza;

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
    protected void addPizza(ActionEvent actionEvent){

    }

    public void removePizza(ActionEvent actionEvent) {
    }

    public void addDessert(ActionEvent actionEvent) {
    }

    public void removeDessert(ActionEvent actionEvent) {
    }

    public void addDrink(ActionEvent actionEvent) {
    }

    public void removeDrink(ActionEvent actionEvent) {
    }



    public void showPizzaDetail(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        String curPizza = pizzaMeal.getValue();
        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();
        stmt.execute("use ilovepizza");


    }

    public void showDessertDetail(ActionEvent actionEvent) {
    }

    public void showDrinkDetail(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        String curDrink = drinkMeal.getValue();
        Class.forName(hostInfo.getJdbcDriver());
        conn = DriverManager.getConnection(hostInfo.getDbUrl(), hostInfo.getUSER(), hostInfo.getPASS());
        stmt = conn.createStatement();
        stmt.execute("use ilovepizza");

        ResultSet rs = stmt.executeQuery("SELECT dri_description, FROM drink WHERE dri_name = '"+curDrink+"';");
        while(rs.next()){

        }
    }
}
