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
public class XReferenceDouble {
    private final String refName;
    private final long ref;
    private final double value;

    public XReferenceDouble(String refName, long ref, double value) {
        this.refName = refName;
        this.ref = ref;
        this.value = value;
    }

    public String getRefName() {
        return refName;
    }

    public long getRef() {
        return ref;
    }

    public double getValue() {
        return value;
    }
}
