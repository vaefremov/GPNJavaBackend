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
public class IntAttribute extends Attribute {
    
    private transient long value;

    public IntAttribute() {
        valueType = "Int";
    }

    
    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String getValueRepr() {
        return Long.toString(value);
    }
    
}
