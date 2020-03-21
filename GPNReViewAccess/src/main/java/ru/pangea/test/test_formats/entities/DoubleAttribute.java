/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pangea.test.test_formats.entities;

/**
 *
 * @author efremov
 */
public class DoubleAttribute extends Attribute {
    private transient double value;

    public DoubleAttribute() {
        valueType = "Double";
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    
    
    @Override
    public String getValueRepr() {
        return Double.toString(value);
    }
    
}
