package com.physicalmed.physicalmedmanagement;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminScreenController implements Initializable {
    @FXML
    private Label labelUserName;

    @FXML
    private Button buttonManageProduct;
    @FXML
    private Button buttonManagePayment;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        String UserName = UserSession.getInstance().getUsername();
        labelUserName.setText(UserName);

        // Para o efeito do botão
        buttonManageProduct.setOnMousePressed(e ->{
            buttonManageProduct.setScaleX(0.95);
            buttonManageProduct.setScaleY(0.95);
        });
        buttonManageProduct.setOnMouseReleased(e ->{
            buttonManageProduct.setScaleX(1);
            buttonManageProduct.setScaleY(1);
        });
    }

    public void startProductMenu(){
        Stage currentStage = (Stage) buttonManageProduct.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/product-menu-view.fxml", "Menu de produtos", currentStage);

    }
    public void startPaymentMenu(){
        Stage currentStage = (Stage) buttonManageProduct.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/payment-menu-view.fxml", "Menu de formas de pagamento", currentStage);
    }

}
