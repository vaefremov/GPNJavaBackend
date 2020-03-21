/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pangea.test.test_formats.boundary;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import ru.pangea.test.test_formats.entities.Boundaries;
import ru.pangea.test.test_formats.entities.Boundary;
import ru.pangea.test.test_formats.entities.Container;
import ru.pangea.test.test_formats.entities.ContainerWithAttributes;
import ru.pangea.test.test_formats.entities.MethodInfo;
import ru.pangea.test.test_formats.entities.Point;
import ru.pangea.test.test_formats.entities.Project;
import ru.pangea.test.test_formats.entities.Well;
import ru.pangea.test.test_formats.entities.WellSet;
import ru.pangea.test.test_formats.entities.XReferenceDouble;
import ru.pangea.test.test_formats.persistence.LowLevelReviewDAO;
import ru.pangea.test.test_formats.persistence.utilities.TVDCalculator;

/**
 *
 * @author efremov
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class UserObjectsDAO  {

    @Inject
    LowLevelReviewDAO rv;
    
    TVDCalculator calc;
    
    @PostConstruct
    public void init() {
        calc = new TVDCalculator();
    }

    @PreDestroy 
    public void terminate() {
        try {
            calc.close();
        } catch (Exception ex) {
            Logger.getLogger(UserObjectsDAO.class.getName()).log(Level.SEVERE, "Exception when closing TVD calculator", ex);
        }
    }
    
    public List<Project> getProjects() {
        List<Project> tmp = rv.getContainersByParentType(ROOT_ID, "proj")
                .stream()
                .map(c -> new Project(c.getName()))
                .collect(Collectors.toList());
        return tmp;
    }

    public WellSet getWells(String projectName) {
        long projId = rv.getContainerByNameType(ROOT_ID, projectName, "proj").getId();
        List<ContainerWithAttributes> tmp = rv.getSubcontainersOfTypeWithAttributes(projId, "wel1");
        List<Well> tmp_wells = tmp.stream()
                .map(c -> new Well(c.getContainer().getName(), c.asPoint("Coords")))
                .collect(Collectors.toList());
        WellSet res = new WellSet(tmp_wells);
        return res;
    }

    public List<MethodInfo> getMethods(String projectName, String well) {
        long projId = rv.getContainerByNameType(1L, projectName, "proj").getId();
        long wellId = rv.getContainerByNameType(projId, well, "wel1").getId();
        List<Container> tmp1 = rv.getSubContainers(wellId);
        return tmp1.stream()
                .filter(c -> "weld".equals(c.getType()) || "wbnd".equals(c.getType()))
                .map(c -> new MethodInfo(c.getName(), rawTypeToCanonical(c.getType()))).collect(Collectors.toList());
    }

    private String rawTypeToCanonical(String type) {
        switch (type) {
            case "wbnd":
                return CTN_BOUNDARIES;
            case "weld":
                return CTN_CURVE;
            default:
                return CTN_UNKNOWN;
        }
    }

    public static final String CTN_BOUNDARIES = "Boundaries";
    public static final String CTN_CURVE = "Curve";
    public static final String CTN_UNKNOWN = "Unknown";

    public Boundaries getMethod(String project, String well, String method) {
        long projId = rv.getContainerByNameType(ROOT_ID, project, "proj").getId();
        long wellId = rv.getContainerByNameType(projId, well, "wel1").getId();
        ContainerWithAttributes wellAttrs = rv.getContainerByIdWithAttributes(wellId);
        Point coords = wellAttrs.asPoint("Coords");
        
        calc.clear();
        calc.setCoords(coords);
        // Getting directional log:
        List<ContainerWithAttributes> dirLogs = rv.getSubcontainersOfTypeWithAttributes(wellId, "dirl");
        if(!dirLogs.isEmpty()) {
            Path dirLogPath = Paths.get(dirLogs.get(0).asString("Path"));
            dirLogPath = PROJECTS_DIR.resolve(dirLogPath);
            LOG.info("++ Dirlog path " + dirLogPath);
            boolean res = calc.readDLFromPath(dirLogPath);
            LOG.log(Level.INFO, "Result of reading DIrLog from {0} {1}", new Object[]{dirLogPath, res});
        } 
        
        long methId = rv.getContainerByNameType(wellId, method, "wbnd").getId();
        ContainerWithAttributes tmp = rv.getContainerByIdWithAttributes(methId);
        List<XReferenceDouble> tmp1 = tmp.asXReferenceArray("boundaries");
        List<Boundary> tmp2 = tmp1.stream()
                .map((XReferenceDouble b) -> {
                    Boundary bb = new Boundary(b.getRefName(), b.getValue());
                    Point p = calculateTvdAndCoords(coords, bb.getMd());
                    bb.setTvdss(p.getZ());
                    bb.setX(p.getX());
                    bb.setY(p.getY());
                    return bb; })
                .collect(Collectors.toList());
        Boundaries res = new Boundaries(tmp2);
        return res;
    }

    
    Point calculateTvdAndCoords(Point coords, double md) {
        return calc.findPointOnTrajectory(md);
    }

    private static final long ROOT_ID = 1L;
    private static final Path DB_ROOT = Paths.get("/opt/PANGmisc/DB_ROOT");
    private static final Path PROJECTS_DIR = DB_ROOT.resolve("PROJECTS");
    private static final Logger LOG = Logger.getLogger(UserObjectsDAO.class.getName());
    
}
