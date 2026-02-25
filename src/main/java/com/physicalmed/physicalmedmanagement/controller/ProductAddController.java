package com.physicalmed.physicalmedmanagement.controller;

import com.physicalmed.physicalmedmanagement.utils.DbFunctions;
import com.physicalmed.physicalmedmanagement.utils.ScreenManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProductAddController implements Initializable {

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtCost;
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
    @FXML
    private Label labelError;
    @FXML
    private Pane paneForm;

    private File selectedImageFile;

    private final DbFunctions dbFunctions = new DbFunctions();

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
        applyNumericCommaMask(txtCost);
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

    @FXML
    private void handleSaveProduct(){
        if (checkFields()){ // Chama o método para checar se os campos estão preenchidos
            System.out.println("Todos os campos preenchidos!");
        try {
            String name = txtName.getText();
            BigDecimal cost = new BigDecimal(txtCost.getText().replace(",", "."));
            BigDecimal pixPrice = new BigDecimal(txtPricePix.getText().replace(",", "."));
            BigDecimal cardPrice = new BigDecimal(txtPriceCard.getText().replace(",", "."));
            BigDecimal minPixPrice = new BigDecimal(txtPricePixDiscount.getText().replace(",", "."));
            BigDecimal minCardPrice = new BigDecimal(txtPriceCardDiscount.getText().replace(",", "."));
            int stock = Integer.parseInt(txtStock.getText());
            byte[] imageBytes = java.nio.file.Files.readAllBytes(selectedImageFile.toPath());

            dbFunctions.saveProduct(name, cost, pixPrice, cardPrice, minPixPrice, minCardPrice, stock, imageBytes);
            System.out.println("Indo salvar o produto na DB");

            // Alerta de sucesso
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Produto Salvo");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Produto salvo com sucesso no banco de dados!");
            successAlert.showAndWait();
            cleanTxtFields();
        } catch (RuntimeException | IOException | SQLException e) {
            // Alerta de erro
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erro ao Salvar Produto");
            errorAlert.setHeaderText("Ocorreu um erro ao tentar salvar o produto.");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
            throw new RuntimeException(e);
        }
        }


    }

    private BigDecimal parseBigDecimal(String value){
        return new BigDecimal(value.replace(",", "."));
    }

    private boolean checkFields(){
        boolean allFilled = true;

        if (txtName.getText().isEmpty()
                || txtCost.getText().isEmpty()
                || txtPricePix.getText().isEmpty()
                || txtPriceCard.getText().isEmpty()
                || txtPricePixDiscount.getText().isEmpty()
                || txtPriceCardDiscount.getText().isEmpty()
                || txtStock.getText().isEmpty()
                || selectedImageFile == null) {
            labelError.setText("Preencha todos os campos para continuar!");
            paneForm.setStyle("-fx-background-color: #ffcccc; -fx-background-radius: 6;");
            allFilled = false;
        }
        else {
            paneForm.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 6;");
            labelError.setText(""); // limpa erro
        }
        return allFilled;
    }

    private void cleanTxtFields(){
        txtName.setText("");
        txtCost.setText("");
        txtPricePix.setText("");
        txtPriceCard.setText("");
        txtPricePixDiscount.setText("");
        txtPriceCardDiscount.setText("");
        txtStock.setText("");
        selectedImageFile = null; // Não usar .delete() :)
        imageProductView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/physicalmed/physicalmedmanagement/images/fundo_salvar_imagem.png"))));
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
