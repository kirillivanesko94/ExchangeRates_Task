package com.example.exchangeratestask.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "exchange_rate")
public class Currency {
    @Column(name = "id")
    @NotNull
    private String id;
    @Id
    @Column(name = "num_code")
    private String numCode;
    @Column(name = "char_code")
    @NotNull
    private String charCode;
    @Column(name = "nominal")
    @NotNull
    private int nominal;
    @Column(name = "name")
    @NotNull
    private String name;
    @Column(name = "value_rate")
    @NotNull
    private double value;
    @Column(name = "previous")
    @NotNull
    private double previous;

    public Currency() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumCode() {
        return numCode;
    }

    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getPrevious() {
        return previous;
    }

    public void setPrevious(double previous) {
        this.previous = previous;
    }
}
