package com.physicalmed.physicalmedmanagement.viewmodel;

public class ChoiceItem {
    private int id;
    private String name;

    public ChoiceItem(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // É o texto que aparecerá na ChoiceBox
    @Override
    public String toString(){
        return name;
    }

}
