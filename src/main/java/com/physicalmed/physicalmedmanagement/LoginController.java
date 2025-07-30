package com.physicalmed.physicalmedmanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField txtUser;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button buttonLogin;

    private DbFunctions dbFunctions = new DbFunctions();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonLogin.setDefaultButton(true); // Ao apertar "Enter" no teclado handleLogin será chamado
    }

    @FXML
    private void handleLogin(){

        // Para o efeito do botão
        buttonLogin.setOnMousePressed(e ->{
            buttonLogin.setScaleX(0.95);
            buttonLogin.setScaleY(0.95);
        });
        buttonLogin.setOnMouseReleased(e ->{
            buttonLogin.setScaleX(1);
            buttonLogin.setScaleY(1);
        });

        String username = txtUser.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() && password.isEmpty()){
            showAlert("Dados Não Preenchidos!", "Por favor, preencha todos os campos antes de continuar");
            return;
        }

        if (dbFunctions.validateLogin(username, password)){
            dbFunctions.getUserData(username);
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