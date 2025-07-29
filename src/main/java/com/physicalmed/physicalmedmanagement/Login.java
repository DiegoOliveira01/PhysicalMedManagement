package com.physicalmed.physicalmedmanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.Objects;

public class Login extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 520);
        stage.setTitle("Login");

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/physicalmed/physicalmedmanagement/images/Physical_Med.png")));
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) {

        launch();
    }
}