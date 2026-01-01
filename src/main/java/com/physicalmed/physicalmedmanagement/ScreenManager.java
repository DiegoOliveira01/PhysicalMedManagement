package com.physicalmed.physicalmedmanagement;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class ScreenManager {

    public static void changeScreen(String fxmlPath, String title, Stage currentStage){
        try{
            FXMLLoader loader = new FXMLLoader(ScreenManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.setResizable(false);
            Image icon = new Image(Objects.requireNonNull(ScreenManager.class.getResourceAsStream("/com/physicalmed/physicalmedmanagement/images/Physical_Med.png")));
            stage.getIcons().add(icon);
            stage.show();

            // --- EFEITO FADE IN ---
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            if (currentStage != null){
                // fade-out antes de fechar
                FadeTransition fadeOut = new FadeTransition(Duration.millis(300), currentStage.getScene().getRoot());
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(event -> currentStage.close());
                fadeOut.play();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
