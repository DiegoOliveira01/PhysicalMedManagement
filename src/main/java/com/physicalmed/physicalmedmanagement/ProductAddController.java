package com.physicalmed.physicalmedmanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductAddController implements Initializable {

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtPricePix;
    @FXML
    private TextField txtPriceCard;
    @FXML
    private TextField txtPricePixDiscount;
    @FXML
    private TextField txtPriceCardDiscount;
    @FXML
    private TextField txtStock;
    @FXML
    private Button buttonSelectImage;
    @FXML
    private Button buttonSaveProduct;
    @FXML
    private Button buttonCancel;
    @FXML
    private Button buttonReturn;
    @FXML
    private ImageView imageProductView;
    @FXML
    private ImageView buttonReturnIcon;

    private File selectedImageFile;

    public void initialize(URL url, ResourceBundle resourceBundle){ // Esse método irá rodar ao iniciar a tela

        // Para o efeito ao clicar nos botões
        buttonSelectImage.setOnMousePressed(e ->{
            buttonSelectImage.setScaleX(0.95);
            buttonSelectImage.setScaleY(0.95);
        });
        buttonSelectImage.setOnMouseReleased(e ->{
            buttonSelectImage.setScaleX(1);
            buttonSelectImage.setScaleY(1);
        });
        buttonSaveProduct.setOnMousePressed(e ->{
            buttonSaveProduct.setScaleX(0.95);
            buttonSaveProduct.setScaleY(0.95);
        });
        buttonSaveProduct.setOnMouseReleased(e ->{
            buttonSaveProduct.setScaleX(1);
            buttonSaveProduct.setScaleY(1);
        });
        buttonCancel.setOnMousePressed(e ->{
            buttonCancel.setScaleX(0.95);
            buttonCancel.setScaleY(0.95);
        });
        buttonCancel.setOnMouseReleased(e ->{
            buttonCancel.setScaleX(1);
            buttonCancel.setScaleY(1);
        });
        buttonReturn.setOnMousePressed(e ->{
            buttonReturn.setScaleX(0.95);
            buttonReturn.setScaleY(0.95);
            buttonReturnIcon.setScaleX(0.90);
            buttonReturnIcon.setScaleY(0.90);
        });
        buttonReturn.setOnMouseReleased(e ->{
            buttonReturn.setScaleX(1);
            buttonReturn.setScaleY(1);
            buttonReturnIcon.setScaleX(1);
            buttonReturnIcon.setScaleY(1);
        });


        // Define oque pode ser digitado nos campos txt
        applyLettersOnlyMask(txtName);
        applyIntegerOnlyMask(txtStock);
        applyNumericCommaMask(txtPrice);
        applyNumericCommaMask(txtPricePix);
        applyNumericCommaMask(txtPriceCard);
        applyNumericCommaMask(txtPricePixDiscount);
        applyNumericCommaMask(txtPriceCardDiscount);

    }

    @FXML
    private void handleSelectImage(ActionEvent event){ // Abre o explorador de arquivos para escolher imagem

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Imagem");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Imagens", "*png", "*jpg", "*jpeg"));

        File file = fileChooser.showOpenDialog(buttonSelectImage.getScene().getWindow());
        if (file != null){
            selectedImageFile = file;
            Image image = new Image(file.toURI().toString());
            imageProductView.setImage(image);
        }
    }

    @FXML
    private void handleReturn(){
        Stage currentStage = (Stage) buttonReturn.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/product-menu-view.fxml", "Menu de produtos", currentStage);
    }

    private void applyNumericCommaMask(TextField textField){
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("\\d{0,7}(,\\d{0,2})?")){
                textField.setText(oldText);
            }
        });
    }

    private void applyLettersOnlyMask(TextField textField) {
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("[a-zA-ZÀ-ÿ\\s]*")) {
                textField.setText(oldText);
            }
        });
    }

    private void applyIntegerOnlyMask(TextField textField) {
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("\\d*")) {
                textField.setText(oldText);
            }
        });
    }

}
