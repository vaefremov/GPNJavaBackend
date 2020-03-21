/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pangea.test.test_formats.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import ru.pangea.test.test_formats.entities.Boundaries;
import ru.pangea.test.test_formats.entities.Boundary;
import ru.pangea.test.test_formats.entities.MethodInfo;
import ru.pangea.test.test_formats.entities.Project;
import ru.pangea.test.test_formats.entities.ProjectsList;
import ru.pangea.test.test_formats.entities.Well;
import ru.pangea.test.test_formats.entities.WellSet;

/**
 *
 * @author efremov
 */
@Stateless
public class WellDAO  {

    ProjectsList pl;
    HashMap<String, WellSet> ws;
    ArrayList<MethodInfo> methodsList;
    Boundaries bs;

    @PostConstruct
    public void init() {
        LOG.info("init: singleton starting...");
        LOG.info("Initializing projects and wells");
        Project p1 = new Project("p1");
        Project p2 = new Project("p2");
        pl = new ProjectsList(p1, p2);
        ws = new HashMap<>();
        Well p1w1 = new Well("w1p1", 634000.0, 1340000, 10.);
        Well p1w2 = new Well("w2", 634000.0, 1340000, 40.);
        Well p2w1 = new Well("w2_1", 632000.0, 1338000, -10.);
        Well p2w2 = new Well("w2_2", 636000.0, 1336000, 15.);
        WellSet ws1 = new WellSet();
        ws1.addWells(p1w1, p1w2);
        WellSet ws2 = new WellSet();
        ws2.addWells(p2w1, p2w2);
        ws.put("p1", ws1);
        ws.put("p2", ws2);

        methodsList = new ArrayList<>();
        methodsList.add(new MethodInfo("Method1"));
        methodsList.add(new MethodInfo("Method2"));
        methodsList.add(new MethodInfo("MethodCurve1", "Curve"));
//        ArrayList<Boundary> boundaries = new ArrayList<>();
        Boundary b1 = new Boundary("B", 1.41);
        b1.setTvdss(300.);
        b1.setX(634000.0);
        b1.setY(1340000.);
        Boundary b2 = new Boundary("A", 3.14);
//        bs = new Boundaries(boundaries);
//        Boundary b1 = new Boundary("B", 3.1415);
//        Boundary b2 = new Boundary("A", 2.717271);
        bs = new Boundaries(new ArrayList<>(Arrays.asList(b1, b2)));

    }

    public List<Project> getProjects() {
        return pl.getProjects();
    }

    public WellSet getWells(String project) {
        return ws.get(project);
    }

    public List<MethodInfo> getMethods(String project, String well) {
        return methodsList;
    }

    public Boundaries getMethod(String project, String well, String method) {
        return bs;
    }
    
    private static final Logger LOG = Logger.getLogger(WellDAO.class.getName());
}
