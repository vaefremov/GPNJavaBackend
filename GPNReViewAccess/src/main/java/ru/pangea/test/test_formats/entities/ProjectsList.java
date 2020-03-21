/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pangea.test.test_formats.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author efremov
 */
@XmlRootElement
public class ProjectsList {
    private final ArrayList<Project> projects;

    public ProjectsList(Project... projects) {
        this.projects = new ArrayList<>();
        this.projects.addAll(Arrays.asList(projects));
    }

    public ProjectsList(List<Project> projects) {
        this.projects = new ArrayList<>(projects);
    }
    
    public List<Project> getProjects() {
        return projects;
    }
    
}
