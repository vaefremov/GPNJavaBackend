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
public class PointAttribute extends Attribute {

    private transient double x;
    private transient double y;
    private transient double z;

    public PointAttribute() {
        valueType = "Point";
    }
    
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
    
    @Override
    public String getValueRepr() {
        return String.format("{%g, %g, %g}", x, y, z);
    }
    
}
