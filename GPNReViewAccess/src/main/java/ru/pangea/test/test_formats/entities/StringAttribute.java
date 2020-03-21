/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pangea.test.test_formats.entities;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author efremov
 */
@XmlRootElement
public class StringAttribute extends Attribute {
    private transient String value;

    public StringAttribute() {
        valueType = "String";
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getValueRepr() {
        return value;
    }
    
}
