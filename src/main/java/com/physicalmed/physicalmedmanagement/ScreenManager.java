package com.physicalmed.physicalmedmanagement;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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

            if (currentStage != null){
                currentStage.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
