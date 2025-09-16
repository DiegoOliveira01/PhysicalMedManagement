package com.physicalmed.physicalmedmanagement;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
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
    @FXML
    private Label labelName;
    @FXML
    private Label labelCost;
    @FXML
    private Label labelPix;
    @FXML
    private Label labelCard;
    @FXML
    private Label labelPixDiscount;
    @FXML
    private Label labelCardDiscount;
    @FXML
    private Label labelStock;
    @FXML
    private ImageView imageViewProduct;
    @FXML
    private TableView<Product> tableProducts;
    @FXML
    private TableColumn<Product, String> colName;
    @FXML
    private TableColumn<Product, String> colStock;
    private DbFunctions db = new DbFunctions();
    private final DecimalFormat moneyFormat = new DecimalFormat("R$ #,##0.00"); // Para formatção dos preços


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){ // Esse método irá rodar ao iniciar a tela
        // Para o efeito dos botões
        buttonAddProduct.setOnMousePressed(e ->{
            buttonAddProduct.setScaleX(0.95);
            buttonAddProduct.setScaleY(0.95);
        });
        buttonAddProduct.setOnMouseReleased(e ->{
            buttonAddProduct.setScaleX(1);
            buttonAddProduct.setScaleY(1);
        });
        buttonUpdateProduct.setOnMousePressed(e ->{
            buttonUpdateProduct.setScaleX(0.95);
            buttonUpdateProduct.setScaleY(0.95);
        });
        buttonUpdateProduct.setOnMouseReleased(e ->{
            buttonUpdateProduct.setScaleX(1);
            buttonUpdateProduct.setScaleY(1);
        });
        buttonDeleteProduct.setOnMousePressed(e ->{
            buttonDeleteProduct.setScaleX(0.95);
            buttonDeleteProduct.setScaleY(0.95);
        });
        buttonDeleteProduct.setOnMouseReleased(e ->{
            buttonDeleteProduct.setScaleX(1);
            buttonDeleteProduct.setScaleY(1);
        });

        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));


        loadProducts(); // Carregar produtos

        tableProducts.getSelectionModel().selectedItemProperty().addListener( // Evento ao selecionar produto
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null){
                        showProductDetail(newSelection);
                    }
                }
        );

    }

    private void loadProducts(){
        List<Product> products = db.getAllProducts();
        ObservableList<Product> observableList = FXCollections.observableArrayList(products);
        tableProducts.setItems(observableList);
    }

    private void showProductDetail(Product product){
        labelName.setText(product.getProductName());
        labelCost.setText(moneyFormat.format(product.getCost()));
        labelPix.setText(moneyFormat.format(product.getPixPrice()));
        labelCard.setText(moneyFormat.format(product.getCreditPrice()));
        labelPixDiscount.setText(moneyFormat.format(product.getPixPriceDiscount()));
        labelCardDiscount.setText(moneyFormat.format(product.getCreditPriceDiscount()));
        labelStock.setText(String.valueOf(product.getStock()).concat(" Unidades"));

        if (product.getProductImage() != null){
            Image img = new Image(new ByteArrayInputStream(product.getProductImage()));
            imageViewProduct.setImage(img);
        }
        else {
            imageViewProduct.setImage(new Image(getClass().getResourceAsStream("/com/physicalmed/physicalmedmanagement/images/fundo_salvar_imagem.png")));
        }
    }

    @FXML
    private void handleReturn(){
        Stage currentStage = (Stage) buttonReturn.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/admin-screen-view.fxml", "Tela Principal", currentStage);
    }

    public void addProduct(){
        Stage currentStage = (Stage) buttonAddProduct.getScene().getWindow();
        ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/product-add-view.fxml", "Cadastrar Novo Produto", currentStage);

    }

    public void updateProduct(){
        Product selected = tableProducts.getSelectionModel().getSelectedItem();

        if (selected != null){
            int updateProductId = selected.getProductId();
            SessionManager.getInstance().setProductId(updateProductId);
            System.out.println("Seleção != NULL Id do produto: " + updateProductId);

            Stage currentStage = (Stage) buttonUpdateProduct.getScene().getWindow();
            ScreenManager.changeScreen("/com/physicalmed/physicalmedmanagement/product-update-view.fxml", "Atualizar Produto", currentStage);
        }
        else {
            System.out.println("Nenhum Item Selecionado");
        }

    }

    public void deleteProduct(){
        Product selected = tableProducts.getSelectionModel().getSelectedItem();

        if (selected != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Você tem certeza?");
            alert.setHeaderText(null);
            alert.setContentText("Você tem certeza que deseja excluir esse produto?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK){
                db.deleteProduct(selected.getProductId());
                loadProducts();
            }
            System.out.println("Exclusão do produto cancelada");
        }
        else {
            System.out.println("Selecione um produto!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Por favor selecione um produto");
            alert.setHeaderText(null);
            alert.setContentText("Para excluir um produto selecione ele na tabela na esquerda!");
            alert.showAndWait();
        }
    }


}
