/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pangea.test.test_formats.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author efremov
 */
@XmlRootElement(name = "method")
public class MethodInfo {
    @XmlElement
    private final String name;
    @XmlElement
    private String methodType;
    
    public MethodInfo(String name) {
        this.name = name;
        this.methodType = "Boundaries";
    }

    public MethodInfo(String name, String methodType) {
        this.name = name;
        this.methodType = methodType;
    }
    
    public MethodInfo() {
        name = "N/A";
        methodType = "N/A";
    }

    public String getMethodType() {
        return methodType;
    }
    
}
