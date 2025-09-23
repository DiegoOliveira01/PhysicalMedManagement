package com.physicalmed.physicalmedmanagement;

import java.math.BigDecimal;
import java.util.Map;

public class PaymentSingle {

    private int paymentId;
    private String paymentName;
    private BigDecimal tax;

    // Construtor
    public PaymentSingle(int paymentId, String paymentName, BigDecimal tax) {
        this.paymentId = paymentId;
        this.paymentName = paymentName;
        this.tax = tax;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
}
