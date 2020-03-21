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
abstract public class Attribute {
    protected transient long id;
    protected transient long linkContainer;
    protected transient long linkMetaData;
    protected transient int index;
    protected transient String status;
    protected transient String valueType;
    protected transient String attributeName;

    public Attribute() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLinkContainer() {
        return linkContainer;
    }

    public void setLinkContainer(long linkContainer) {
        this.linkContainer = linkContainer;
    }

    public long getLinkMetaData() {
        return linkMetaData;
    }

    public void setLinkMetaData(long linkMetaData) {
        this.linkMetaData = linkMetaData;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
    
    @XmlElement
    public abstract String getValueRepr();
    
    @XmlElement
    public String getValueType() {
        return valueType;
    }

}
