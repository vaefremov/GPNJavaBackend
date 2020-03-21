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
@XmlRootElement
public class Project {
    private final String name;
    @XmlElement
    private String cs;
    
    @XmlElement
    public String getName() {
        return name;
    }

    public Project(String name) {
        this.name = name;
        this.cs = "Custom";
    }

    public Project() {
        this.name = "N/A";
        this.cs = "Custom";
    }
    
}
