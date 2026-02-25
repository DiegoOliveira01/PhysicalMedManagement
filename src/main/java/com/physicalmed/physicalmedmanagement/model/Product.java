package com.physicalmed.physicalmedmanagement.model;

import java.math.BigDecimal;

public class Product {

    private int productId;
    private String productName;
    private BigDecimal cost;
    private BigDecimal pixPrice;
    private BigDecimal creditPrice;
    private BigDecimal pixPriceDiscount;
    private BigDecimal creditPriceDiscount;
    private int stock;
    private byte[] productImage;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getPixPrice() {
        return pixPrice;
    }

    public void setPixPrice(BigDecimal pixPrice) {
        this.pixPrice = pixPrice;
    }

    public BigDecimal getCreditPrice() {
        return creditPrice;
    }

    public void setCreditPrice(BigDecimal creditPrice) {
        this.creditPrice = creditPrice;
    }

    public BigDecimal getPixPriceDiscount() {
        return pixPriceDiscount;
    }

    public void setPixPriceDiscount(BigDecimal pixPriceDiscount) {
        this.pixPriceDiscount = pixPriceDiscount;
    }

    public BigDecimal getCreditPriceDiscount() {
        return creditPriceDiscount;
    }

    public void setCreditPriceDiscount(BigDecimal creditPriceDiscount) {
        this.creditPriceDiscount = creditPriceDiscount;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public byte[] getProductImage() {
        return productImage;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }
}
