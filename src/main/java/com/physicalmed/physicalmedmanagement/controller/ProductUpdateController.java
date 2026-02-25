package com.physicalmed.physicalmedmanagement.controller;

import com.physicalmed.physicalmedmanagement.utils.DbFunctions;
import com.physicalmed.physicalmedmanagement.model.Product;
import com.physicalmed.physicalmedmanagement.utils.ScreenManager;
import com.physicalmed.physicalmedmanagement.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProductUpdateController implements Initializable {

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
    private Button buttonReturn;
    @FXML
    private ImageView imageProductView;
    @FXML
    private Label labelError;
    @FXML
    private Pane paneForm;

    private int currentProductId;
    private File selectedImageFile;
    private byte[] currentImageByte;
    private final DbFunctions dbFunctions = new DbFunctions();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        currentProductId = SessionManager.getInstance().getProductId();
        System.out.println("Product Id recebido:" + currentProductId);
        loadProductData(currentProductId);

        // Define oque pode ser digitado nos campos txt
        applyLettersOnlyMask(txtName);
        applyIntegerOnlyMask(txtStock);
        applyNumericCommaMask(txtCost);
        applyNumericCommaMask(txtPricePix);
        applyNumericCommaMask(txtPriceCard);
        applyNumericCommaMask(txtPricePixDiscount);
        applyNumericCommaMask(txtPriceCardDiscount);
        replaceNumeric(txtCost);
        replaceNumeric(txtPricePix);
        replaceNumeric(txtPriceCard);
        replaceNumeric(txtPricePixDiscount);
        replaceNumeric(txtPriceCardDiscount);

    }

    private void loadProductData(int currentProductId){
        Product product = dbFunctions.getProductById(currentProductId);

        if (product != null){
            txtName.setText(product.getProductName());
            txtCost.setText(product.getCost().toString());
            txtPricePix.setText(product.getPixPrice().toString());
            txtPriceCard.setText(product.getCreditPrice().toString());
            txtPricePixDiscount.setText(product.getPixPriceDiscount().toString());
            txtPriceCardDiscount.setText(product.getCreditPriceDiscount().toString());
            txtStock.setText(String.valueOf(product.getStock()));
            if (product.getProductImage() != null){
                currentImageByte = product.getProductImage(); // Armazena os bytes da imagem atual

                Image img = new Image(new ByteArrayInputStream(product.getProductImage()));
                imageProductView.setImage(img);
            }
        }
        else {
            System.out.println("Erro ao retornar produto");
        }
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
    private void handleUpdateProduct(){
        if (checkFields()){
            System.out.println("Todos os campos preenchidos!");
            try {
                String name = txtName.getText();
                BigDecimal cost = new BigDecimal(txtCost.getText().replace(",", "."));
                BigDecimal pixPrice = new BigDecimal(txtPricePix.getText().replace(",", "."));
                BigDecimal cardPrice = new BigDecimal(txtPriceCard.getText().replace(",", "."));
                BigDecimal minPixPrice = new BigDecimal(txtPricePixDiscount.getText().replace(",", "."));
                BigDecimal minCardPrice = new BigDecimal(txtPriceCardDiscount.getText().replace(",", "."));
                int stock = Integer.parseInt(txtStock.getText());
                byte[] imageBytes;
                if (selectedImageFile != null){
                    imageBytes = java.nio.file.Files.readAllBytes(selectedImageFile.toPath());
                }
                else {
                    imageBytes = currentImageByte;
                }

                dbFunctions.updateProduct(currentProductId, name, cost, pixPrice, cardPrice, minPixPrice, minCardPrice, stock, imageBytes);

                System.out.println("Indo salvar o produto na DB");

                // Alerta de sucesso
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Produto Atualizado");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Produto atualizado com sucesso no banco de dados!");
                successAlert.showAndWait();

                handleReturn();
            } catch (RuntimeException | IOException | SQLException e) {
                // Alerta de erro
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erro ao atualizar Produto");
                errorAlert.setHeaderText("Ocorreu um erro ao tentar atualizar o produto.");
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
                throw new RuntimeException(e);
            }
        }
    }

    private boolean checkFields(){
        boolean allFilled = true;

        if (txtName.getText().isEmpty()
                || txtCost.getText().isEmpty()
                || txtPricePix.getText().isEmpty()
                || txtPriceCard.getText().isEmpty()
                || txtPricePixDiscount.getText().isEmpty()
                || txtPriceCardDiscount.getText().isEmpty()
                || txtStock.getText().isEmpty()) {
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

    private void replaceNumeric(TextField textField){
        textField.setText(textField.getText().replace(".", ","));
    }

}