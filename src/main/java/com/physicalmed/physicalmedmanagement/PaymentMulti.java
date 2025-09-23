package com.physicalmed.physicalmedmanagement;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PaymentMulti {

    private int paymentId;
    private String paymentName;
    private Map<Integer, BigDecimal> installmentTaxes;

    public PaymentMulti(){
        this.installmentTaxes = new HashMap<>();
    }
    public PaymentMulti(int paymentId, String paymentName) {
        this.paymentId = paymentId;
        this.paymentName = paymentName;
        this.installmentTaxes = new HashMap<>();
    }

    // Metodo para adicionar uma parcela com sua taxa
    public void addInstallmentTax(int installment, BigDecimal tax) {
        this.installmentTaxes.put(installment, tax);
    }

    // Metodo útil para pegar taxa de uma parcela específica
    public BigDecimal getTaxForInstallment(int installment) {
        return installmentTaxes.get(installment);
    }

    // Getters e Setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public String getPaymentName() { return paymentName; }
    public void setPaymentName(String paymentName) { this.paymentName = paymentName; }

    public Map<Integer, BigDecimal> getInstallmentTaxes() { return installmentTaxes; }
    public void setInstallmentTaxes(Map<Integer, BigDecimal> installmentTaxes) {
        this.installmentTaxes = installmentTaxes;
    }
}
