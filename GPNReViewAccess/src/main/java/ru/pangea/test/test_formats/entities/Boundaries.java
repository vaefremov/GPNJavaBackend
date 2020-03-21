/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pangea.test.test_formats.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author efremov
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Boundaries {
    
    private transient String name;
    
    @XmlElementWrapper(name = "pointsList")
    @XmlElement(name = "point")
    private ArrayList<Boundary> boundaries;

    public void setBoundaries(ArrayList<Boundary> boundaries) {
        this.boundaries = boundaries;
    }

    public Boundaries(List<Boundary> boundaries) {
        name = "Some fancy name";
        this.boundaries = new ArrayList<>();
        this.boundaries.addAll(boundaries);
    }

    public Boundaries() {
        boundaries = new ArrayList<>();
    }
    
    
}
