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
public class Well {

    @XmlElement
    private String name;
    @XmlElement
    private double x;
    @XmlElement
    private double y;
    @XmlElement
    private double alt;

    public Well(String name, Point coords) {
        this.name = name;
        if (coords == null) {
            this.x = 0;
            this.y = 0;
            this.alt = 0;
        } else {
            this.x = coords.getX();
            this.y = coords.getY();
            this.alt = coords.getZ();
        }
    }

    public Well(String name, double x, double y, double alt) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.alt = alt;
    }

    public Well() {
        name = "Not set!!!";
    }

    public String getName() {
        return name;
    }
    
}
