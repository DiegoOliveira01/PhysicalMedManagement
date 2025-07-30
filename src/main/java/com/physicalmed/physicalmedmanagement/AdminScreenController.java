package com.physicalmed.physicalmedmanagement;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminScreenController implements Initializable {
    @FXML
    private Label labelUserName;

    @FXML
    private Button buttonAddProduct;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        String UserName = UserSession.getInstance().getUsername();
        labelUserName.setText(UserName);

        // Para o efeito do botão
        buttonAddProduct.setOnMousePressed(e ->{
                buttonAddProduct.setScaleX(0.95);
                buttonAddProduct.setScaleY(0.95);
        });
        buttonAddProduct.setOnMouseReleased(e ->{
            buttonAddProduct.setScaleX(1);
            buttonAddProduct.setScaleY(1);
        });
    }

}
