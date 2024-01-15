package com.example.exchangeratestask.model;

public class Currency {
    private String ID;
    private String NumCode;
    private String CharCode;
    private int Nominal;
    private String Name;
    private double Value;
    private double Previous;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNumCode() {
        return NumCode;
    }

    public void setNumCode(String numCode) {
        NumCode = numCode;
    }

    public String getCharCode() {
        return CharCode;
    }

    public void setCharCode(String charCode) {
        CharCode = charCode;
    }

    public int getNominal() {
        return Nominal;
    }

    public void setNominal(int nominal) {
        Nominal = nominal;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getValue() {
        return Value;
    }

    public void setValue(double value) {
        Value = value;
    }

    public double getPrevious() {
        return Previous;
    }

    public void setPrevious(double previous) {
        Previous = previous;
    }
}
