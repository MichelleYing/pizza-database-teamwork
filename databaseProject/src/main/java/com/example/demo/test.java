package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class test extends Application {

    @Override
    public void start(Stage stage) {
        try {
            stage.setResizable(false);
            FXMLLoader fxmlLoader = new FXMLLoader(ilovepizzaApplication.class.getResource("orderHistory.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setTitle("I Love Pizza");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public static void main(String[] args) {
        launch();
    }
}