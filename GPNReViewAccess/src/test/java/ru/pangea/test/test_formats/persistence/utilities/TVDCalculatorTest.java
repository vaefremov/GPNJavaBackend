/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pangea.test.test_formats.persistence.utilities;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
import ru.pangea.test.test_formats.entities.Point;

/**
 *
 * @author efremov
 */
public class TVDCalculatorTest {
    
    private final static String FILE_IN = "src/test/data/dirlog.pickle";
    
    public TVDCalculatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of readDL method, of class TVDCalculator.
     */
    @Test
    public void testReadDL() {
        System.out.println("readDL");
        TVDCalculator instance = new TVDCalculator();
        instance.readDL(Paths.get(FILE_IN));
        for(TVDCalculator.DLElement e: instance.getDl()) {
            System.out.println(e.toString());
        }
        List<TVDCalculator.DLElement> dl = instance.getDl();
        assertEquals(dl.size(), 298);
        assertEquals(dl.get(0).getMd(), 0.0, 0.1);
        assertEquals(dl.get(0).getTvd(), 31.0, 0.1);
        assertEquals(dl.get(297).getMd(), 3060.0, 0.1);
        assertEquals(dl.get(297).getTvd(), -2822.82, 0.1);
    }

    /**
     * Test of findTvd method, of class TVDCalculator.
     */
    @Test
    public void testFindTvd() {
        System.out.println("findTvd");
        Point coords = new Point(0, 0, 0);
        TVDCalculator instance = new TVDCalculator(FILE_IN, coords);
        double md = 0.0;
        double expResult = 31.0;
        double result = instance.findTvd(md);
        assertEquals(expResult, result, 0.1);
        
        md = 3050.0;
        expResult = -2812.88;
        result = instance.findTvd(md);
        assertEquals(expResult, result, 0.1);
        
        //md=1060.0, tvd=-923.671
        md = 1060.0;
        expResult = -923.671;
        result = instance.findTvd(md);
        assertEquals(expResult, result, 0.1);
        
        md = 3055.0;
        result = instance.findTvd(md);
        System.out.println("++ For md:" + md + " found TVD:" + result);
        assertTrue(result < -2812.88 && result > -2822.82);

        md = 5.0;
        result = instance.findTvd(md);
        System.out.println("++ For md:" + md + " found TVD:" + result);
        assertTrue(result < 31.0 && result > 21.0);
        
    }

    /**
     * Test of getDl method, of class TVDCalculator.
     */
    @Test
    public void testGetDl() {
        System.out.println("getDl");
        Point coords = new Point(0, 0, 0);
        TVDCalculator instance = new TVDCalculator(FILE_IN, coords);
//        List<TVDCalculator.DLElement> expResult = null;
        List<TVDCalculator.DLElement> result = instance.getDl();
        assertEquals(result.size(), 298);
    }

    /**
     * Test of findPointOnTrajectory method, of class TVDCalculator.
     */
    @Test
    public void testFindPointOnTrajectory() {
        //(3020.0, -2783.13, -747.157, -333.425), (3030.0, -2793.03, -748.464, -334.019)
        System.out.println("findPointOnTrajectory");
        double md = 3020.0;
        Point coords = new Point(600000.0, 1340000.0, 31.0);
        TVDCalculator instance = new TVDCalculator(FILE_IN, coords);
        Point expResult = new Point(coords.getX() -747.157, coords.getY() -333.425, -2783.13);
        Point result = instance.findPointOnTrajectory(md);
        assertEquals(expResult.getZ(), result.getZ(), 0.1);
        assertEquals(expResult.getX(), result.getX(), 0.1);
        assertEquals(expResult.getY(), result.getY(), 0.1);
    }

    /**
     * Test of setCoords method, of class TVDCalculator.
     */
    @Test
    public void testSetCoords() {
        System.out.println("setCoords");
        Point coords = new Point(600000.0, 1340000.0, 31.0);
        TVDCalculator instance = new TVDCalculator();
        instance.readDL(Paths.get(FILE_IN));
        instance.setCoords(coords);
        Point expResult = new Point(coords.getX() -747.157, coords.getY() -333.425, -2783.13);
        double md = 3020.0;
        Point result = instance.findPointOnTrajectory(md);
        assertEquals(expResult.getZ(), result.getZ(), 0.1);
        assertEquals(expResult.getX(), result.getX(), 0.1);
        assertEquals(expResult.getY(), result.getY(), 0.1);
    }

    /**
     * Test of readDLFromPath method, of class TVDCalculator.
     */
    @Test
    public void testReadDLFromPath() {
        System.out.println("readDLFromPath");
        Path pathToDirlog = Paths.get("/tmp/non-existing-dirlog-file.pickle");
        TVDCalculator instance = new TVDCalculator();
        boolean expResult = false;
        boolean result = instance.readDLFromPath(pathToDirlog);
        assertEquals(expResult, result);
        
        result = instance.readDLFromPath(Paths.get(FILE_IN));
        assertTrue(result);
    }

    /**
     * Test of clear method, of class TVDCalculator.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        Point coords = new Point(600000.0, 1340000.0, 31.0);
        TVDCalculator instance = new TVDCalculator(FILE_IN, coords);
        double md = 3055.0;
        double result1 = instance.findTvd(md);
        instance.clear();
        instance.setCoords(coords);
        double result2 = instance.findTvd(md);
        assertTrue(result1 > result2);
        System.out.println("Two TVD: dirlog vs empty dirlog " + result1 + " " + result2);
    }
    
}
