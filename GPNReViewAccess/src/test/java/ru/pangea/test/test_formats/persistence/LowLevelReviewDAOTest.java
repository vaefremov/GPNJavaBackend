/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pangea.test.test_formats.persistence;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import ru.pangea.test.test_formats.entities.Container;
import static org.mockito.Mockito.*;
import org.sql2o.Connection;
import org.sql2o.Query;
import ru.pangea.test.test_formats.entities.IntAttribute;
import ru.pangea.test.test_formats.entities.StringAttribute;

/**
 *
 * @author efremov
 */
public class LowLevelReviewDAOTest {

    private static LowLevelReviewDAO instance;
    private static Query query;

    public LowLevelReviewDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws NamingException, SQLException {
        instance = new LowLevelReviewDAO();
        instance.connection = mock(Connection.class);
        query = mock(Query.class);
        when(instance.connection.createQuery(LowLevelReviewDAO.SQL_GET_SUBCONTAINERS)).thenReturn(query);
        when(instance.connection.createQuery(LowLevelReviewDAO.SQL_GET_CONTAINERS)).thenReturn(query);
        when(instance.connection.createQuery(LowLevelReviewDAO.SQL_GET_STRING_ATTRIBUTES)).thenReturn(query);
        when(instance.connection.createQuery(LowLevelReviewDAO.SQL_GET_INT_ATTRIBUTES)).thenReturn(query);
        when(query.addParameter("linkUp", 1L)).thenReturn(query);
        when(query.addParameter("type", "lin1")).thenReturn(query);
        when(query.addParameter("containerId", 1L)).thenReturn(query);
        instance.setupDaoInstance();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    /**
     * Test of init method, of class LowLevelReviewDAO.
     */
    @Ignore
    @Test
    public void testInit() throws Exception {
        System.out.println("init");
        fail("The test case is a prototype.");
    }

    /**
     * Test of cleanup method, of class LowLevelReviewDAO.
     */
    @Ignore
    @Test
    public void testCleanup() throws Exception {
        System.out.println("cleanup");
        fail("The test case is a prototype.");
    }

    /**
     * Test of getContainersByParentType method, of class LowLevelReviewDAO.
     */
//    @Ignore
    @Test
    public void testGetContainersByParentType() throws Exception {
        System.out.println("getContainersByParentType");
        List<Container> ans1 = new ArrayList<>(Arrays.asList(new Container()));
        when(query.executeAndFetch(Container.class)).thenReturn(ans1);
        long parentId = 1L;
        String type = "lin1";
        List<Container> expResult = ans1;
        List<Container> result = instance.getContainersByParentType(parentId, type);
        assertEquals(expResult, result);
    }

    /**
     * Test of getSubContainers method, of class LowLevelReviewDAO.
     */
    @Test
    public void testGetSubContainers() throws Exception {
        System.out.println("getSubContainers");
        long parentId = 1L;
        List<Container> ans1 = new ArrayList<>(Arrays.asList(new Container()));
        when(query.executeAndFetch(Container.class)).thenReturn(ans1);
        List<Container> expResult = ans1;
        List<Container> result = instance.getSubContainers(parentId);
        assertEquals(expResult.size(), result.size());
        assertEquals(expResult, result);
    }

    /**
     * Test of getContainerStringAttributes method, of class LowLevelReviewDAO.
     */
    @Ignore
    @Test
    public void testGetContainerStringAttributes() throws Exception {
        System.out.println("getContainerStringAttributes");
        long containerId = 0L;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        LowLevelReviewDAO instance = (LowLevelReviewDAO)container.getContext().lookup("java:global/classes/LowLevelReviewDAO");
        List<StringAttribute> expResult = null;
        List<StringAttribute> result = instance.getContainerStringAttributes(containerId);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getContainerIntAttributes method, of class LowLevelReviewDAO.
     */
    @Ignore
    @Test
    public void testGetContainerIntAttributes() throws Exception {
        System.out.println("getContainerIntAttributes");
        long containerId = 0L;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        LowLevelReviewDAO instance = (LowLevelReviewDAO)container.getContext().lookup("java:global/classes/LowLevelReviewDAO");
        List<IntAttribute> expResult = null;
        List<IntAttribute> result = instance.getContainerIntAttributes(containerId);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGenericContainerAttributes method, of class LowLevelReviewDAO.
     */
    @Test
    public void testGetGenericContainerAttributes() throws Exception {
        System.out.println("getGenericContainerAttributes");
        Long contianerId = 1L;
        List<StringAttribute> expResult = new ArrayList<>(Arrays.asList(new StringAttribute()));
        when(query.executeAndFetch(StringAttribute.class)).thenReturn(expResult);
        List<StringAttribute> result = instance.getGenericContainerAttributes(contianerId, StringAttribute.class);
        assertEquals(expResult, result);
    }

}
