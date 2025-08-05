package com.physicalmed.physicalmedmanagement;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductMenuController implements Initializable {

    @FXML
    private Button buttonAddProduct;

    @FXML
    private Button buttonUpdateProduct;

    @FXML
    private Button buttonDeleteProduct;

    @FXML
    private Button buttonReturn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        // Esse método irá rodar ao iniciar a tela
        buttonAddProduct.setOnMousePressed(e ->{
            buttonAddProduct.setScaleX(0.95);
            buttonAddProduct.setScaleY(0.95);
        });
        buttonAddProduct.setOnMouseReleased(e ->{
            buttonAddProduct.setScaleX(1);
            buttonAddProduct.setScaleY(1);
        });
    }

    public void addProduct(){

        Stage currentStage = (Stage) buttonAddProduct.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/product-add-view.fxml", "Cadastrar Novo Produto", currentStage);

    }

}
