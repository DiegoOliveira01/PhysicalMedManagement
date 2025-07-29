package com.physicalmed.physicalmedmanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField txtUser;

    @FXML
    private TextField txtPassword;

    private DbFunctions dbFunctions = new DbFunctions();

    @FXML
    private void handleLogin(){

        String username = txtUser.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() && password.isEmpty()){
            showAlert("Dados Não Preenchidos!", "Por favor, preencha todos os campos antes de continuar");
            return;
        }

        if (dbFunctions.validateLogin(username, password)){

            Stage currentStage = (Stage) txtUser.getScene().getWindow();
            ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/admin-screen-view.fxml","Tela Principal", currentStage);
        }
        else {
            showAlert("Login inválido", "Usuário ou senha incorretos.");
        }
    }

    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}