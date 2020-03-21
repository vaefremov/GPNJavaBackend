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
public class RefWithParameterAttribute extends Attribute {
    
    private transient double value;
    private transient long ref;
    private transient String refContainerName;

    public RefWithParameterAttribute() {
        valueType = "RefWithParameter";
    }
    
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getRef() {
        return ref;
    }

    public void setRef(long ref) {
        this.ref = ref;
    }

    public String getRefContainerName() {
        return refContainerName;
    }

    public void setRefContainerName(String refContainerName) {
        this.refContainerName = refContainerName;
    }

    
    
    @Override
    public String getValueRepr() {
        return String.format("{%g, %s (%d)}", value, refContainerName, ref);
    }
    
}
