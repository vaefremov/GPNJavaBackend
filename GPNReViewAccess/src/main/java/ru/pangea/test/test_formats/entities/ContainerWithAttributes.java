/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pangea.test.test_formats.entities;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author efremov
 */
@XmlRootElement
public class ContainerWithAttributes {
    private transient Container container;
    private transient Map<String, List<Attribute>> attributes;

    public ContainerWithAttributes(Container container, Map<String, List<Attribute>> attributes) {
        this.container = container;
        this.attributes = attributes;
    }

    public Container getContainer() {
        return container;
    }

    public Map<String, List<Attribute>> getAttributes() {
        return attributes;
    }
    
    public Long asLong(String attrName) {
        List<Attribute> tmp = attributes.get(attrName);
        if(tmp == null || tmp.isEmpty())
            return null;
        return ((IntAttribute) tmp.get(0)).getValue();
    }
    
    public Double asDouble(String attrName) {
        List<Attribute> tmp = attributes.get(attrName);
        if(tmp == null || tmp.isEmpty())
            return null;
        return ((DoubleAttribute) tmp.get(0)).getValue();
    }
    
    public Point asPoint(String attrName) {
        List<Attribute> tmp = attributes.get(attrName);
        if(tmp == null || tmp.isEmpty())
            return null;
        return pointAttribute2Point(tmp.get(0));
    }
    
    public String asString(String attrName) {
        List<Attribute> tmp = attributes.get(attrName);
        if(tmp == null || tmp.isEmpty())
            return null;
        return ((StringAttribute) tmp.get(0)).getValue();
    }
    
    public List<Point> asPointArray(String attrName) {
        List<Attribute> tmp = attributes.get(attrName);
        if(tmp == null)
            return null;
        return tmp.stream().map(a -> pointAttribute2Point(a)).collect(Collectors.toList());
    }
    
    public List<XReferenceDouble> asXReferenceArray(String attrName) {
        List<Attribute> tmp = attributes.get(attrName);
        if(tmp == null)
            return null;
        return tmp.stream().map(a -> refWithParameter2XRef(a)).collect(Collectors.toList());        
    }
    
    private Point pointAttribute2Point(Attribute a) {
        PointAttribute tmp = (PointAttribute) a;
        return new Point(tmp.getX(), tmp.getY(), tmp.getZ());
    }
    
    private XReferenceDouble refWithParameter2XRef(Attribute a) {
        RefWithParameterAttribute tmp = (RefWithParameterAttribute) a;
        return new XReferenceDouble(tmp.getRefContainerName(), tmp.getRef(), tmp.getValue());
    }
}
