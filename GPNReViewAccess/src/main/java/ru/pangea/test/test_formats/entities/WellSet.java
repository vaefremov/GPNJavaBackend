/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pangea.test.test_formats.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author efremov
 */
@XmlRootElement
public class WellSet {
    private transient final ArrayList<Well> wells;

    public WellSet(List<Well> tmp_wells) {
        wells = new ArrayList<>(tmp_wells);
    }

    public void addWells(Well... wells) {
        this.wells.addAll(Arrays.asList(wells));
    }
    
    @XmlElementWrapper(name = "wells")
    @XmlElement(name = "well")
    public List<Well> getWells() {
        return wells;
    }
    
    public WellSet() {
        wells = new ArrayList<>();
    }
    
    
}
