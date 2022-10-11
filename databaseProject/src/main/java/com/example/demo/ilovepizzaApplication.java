package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ilovepizzaApplication extends Application {

    @Override
    public void start(Stage stage) {
        try {
            stage.setResizable(false);
            FXMLLoader fxmlLoader = new FXMLLoader(ilovepizzaApplication.class.getResource("loginPage.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 400);
            stage.setTitle("Login");
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