/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pangea.test.test_formats.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ejb.embeddable.EJBContainer;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyTuple;
import org.python.util.PythonInterpreter;
import ru.pangea.test.test_formats.entities.Boundaries;
import ru.pangea.test.test_formats.entities.MethodInfo;
import ru.pangea.test.test_formats.entities.Project;
import ru.pangea.test.test_formats.entities.WellSet;

/**
 *
 * @author efremov
 */
public class WellDAOTest {

    private static EJBContainer container;
    private static WellDAO instance;

    public WellDAOTest() {

    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("Before class");
        instance = new WellDAO();
        instance.init();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        System.out.println("After class");
    }

    @Before
    public void setUp() throws Exception {
        System.out.println("Before test");
    }

    /**
     * Test of init method, of class WellDAO.
     */
    @Test
    public void testInit() throws Exception {
        System.out.println("init");
//        instance.init();

    }

    /**
     * Test of getProjects method, of class WellDAO.
     */
    @Test
    public void testGetProjects() throws Exception {
        System.out.println("getProjects");
        List<Project> expResult = new ArrayList<Project>(Arrays.asList(new Project("p1"), new Project("p2")));
        List<Project> result = instance.getProjects();
        assertEquals(expResult.size(), result.size());
        assertEquals(expResult.get(0).getName(), result.get(0).getName());
    }

    /**
     * Test of getWells method, of class WellDAO.
     */
    @Ignore
    @Test
    public void testGetWells() throws Exception {
        System.out.println("getWells");
        String project = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        WellDAO instance = (WellDAO) container.getContext().lookup("java:global/classes/WellDAO");
        WellSet expResult = null;
        WellSet result = instance.getWells(project);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMethods method, of class WellDAO.
     */
    @Ignore
    @Test
    public void testGetMethods() throws Exception {
        System.out.println("getMethods");
        String project = "";
        String well = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        WellDAO instance = (WellDAO) container.getContext().lookup("java:global/classes/WellDAO");
        List<MethodInfo> expResult = null;
        List<MethodInfo> result = instance.getMethods(project, well);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMethod method, of class WellDAO.
     */
    @Ignore
    @Test
    public void testGetMethod() throws Exception {
        System.out.println("getMethod");
        String project = "";
        String well = "";
        String method = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        WellDAO instance = (WellDAO) container.getContext().lookup("java:global/classes/WellDAO");
        Boundaries expResult = null;
        Boundaries result = instance.getMethod(project, well, method);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testJythonAccess() {
        System.out.println("testJythonAccess");
        PythonInterpreter interp = new PythonInterpreter();
        interp.exec("import pickle");
        interp.exec("import pickle");
        interp.set("file_name", new PyString("src/test/data/dirlog.pickle"));
        interp.exec("dl1 = pickle.load(open(file_name, 'rb'))['data']");

        interp.exec("p = [(i.get('md', 3.4028234663852886e+38), i.get('tvd', 3.4028234663852886e+38)) for i in dl1]");
        PyObject p = interp.get("p");

        System.out.println("** p " + p + " " + p.isSequenceType());
        PyList pl = new PyList(p);
        System.out.println("** pl " + pl.__len__());
        System.out.println("++ " + pl.get(0));
        for (int i = 0; i < pl.__len__(); i++) {
            PyTuple obj = (PyTuple) pl.get(i);
            System.out.println("  == " + ((Double) obj.get(0)) + " " + ((Double) obj.get(1)));
        }
    }
}
