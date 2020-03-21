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
public class Boundary {

    private String name;
    private Double md;
    private Double tvdss;
    private Double x;
    private Double y;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Boundary(String name, Double md) {
        this.name = name;
        this.md = md;
    }

    public Boundary() {
        name = "No name!!!";
        md = Double.MAX_VALUE;
    }

    public Double getMd() {
        return md;
    }

    public void setMd(Double md) {
        this.md = md;
    }

    public Double getTvdss() {
        return tvdss;
    }

    public void setTvdss(Double tvd) {
        this.tvdss = tvd;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        StringBuilder bld = new StringBuilder();
        bld.append("Boundary {name = ").append(name)
                .append(" MD= ").append(md)
                .append(" TVDSS=").append(tvdss)
                .append(" X=").append(x)
                .append(" Y=").append(y);
        return bld.toString();
    }

}
