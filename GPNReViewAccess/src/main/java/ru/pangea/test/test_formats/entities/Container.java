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
public class Container {
    private transient long id;
    private transient long linkUp;
    private transient long topParent;
    private transient String type;
    private transient String status;
    private transient String name;
    private transient int ownerId;
    private transient boolean isProtected;

    public Container() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLinkUp() {
        return linkUp;
    }

    public void setLinkUp(long linkUp) {
        this.linkUp = linkUp;
    }

    public long getTopParent() {
        return topParent;
    }

    public void setTopParent(long topParent) {
        this.topParent = topParent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isIsProtected() {
        return isProtected;
    }

    public void setIsProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }
    
    
}
