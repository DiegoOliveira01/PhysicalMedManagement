package com.physicalmed.physicalmedmanagement.viewmodel;

public class ChoiceItemPayment {
    private String name;
    private String type; // "SINGLE" ou "MULTI"

    public ChoiceItemPayment(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        // Isso é o que aparece na ChoiceBox
        return name + (type.equals("MULTI") ? " (Parcelado)" : " (À vista)");
    }
}
