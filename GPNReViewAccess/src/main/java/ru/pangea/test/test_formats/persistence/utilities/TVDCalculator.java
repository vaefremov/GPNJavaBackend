/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pangea.test.test_formats.persistence.utilities;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import org.python.core.PyList;
import org.python.core.PyString;
import org.python.core.PyTuple;
import org.python.util.PythonInterpreter;
import ru.pangea.test.test_formats.entities.Point;

/**
 *
 * @author efremov
 */
public class TVDCalculator implements AutoCloseable {

    private Path fileIn;
    private PythonInterpreter py;
    Point coords;
    private ArrayList<DLElement> dl = new ArrayList<>();

    @Override
    public void close() throws Exception {
        LOG.info("Closing Python instance");
        py.cleanup();
        py.close();
    }

    public static class DLElement {

        private final double md;
        private final double tvd;
        private final double dx;
        private final double dy;

        public DLElement(double md, double tvd, double dx, double dy) {
            this.md = md;
            this.tvd = tvd;
            this.dx = dx;
            this.dy = dy;
        }

        public double getMd() {
            return md;
        }

        public double getTvd() {
            return tvd;
        }

        public double getDx() {
            return dx;
        }

        public double getDy() {
            return dy;
        }

        @Override
        public String toString() {
            return "DLElement{" + "md=" + md + ", tvd=" + tvd + ", dx=" + dx + ", dy=" + dy + '}';
        }
        
    }

    public TVDCalculator() {
        py = new PythonInterpreter();
        coords = new Point(UNDEF, UNDEF, UNDEF);
    }

    
    
    public TVDCalculator(String pickledDirlogFileName, Point coords) {
        py = new PythonInterpreter();
        this.coords = coords;
        readDL(Paths.get(pickledDirlogFileName));
    }
    
    public boolean readDLFromPath(Path pathToDirlog) {
        if(!Files.isReadable(pathToDirlog))
            return false;
        readDL(pathToDirlog);
        return true;
    }
    
    public void clear() {
        coords = null;
        fileIn = null;
        dl.clear();
    }
    
    void readDL(Path pickledDirlogFileName) {
        // @TODO: we should make sure file is readable
        fileIn = pickledDirlogFileName;
        py.set("file_name", new PyString(fileIn.toString()));
        py.exec("import pickle");
        py.exec("dl1 = pickle.load(open(file_name, 'rb'))['data']");

        py.exec("p = [(i.get('md', 3.4028234663852886e+38),"
                + " i.get('tvd', 3.4028234663852886e+38),"
                + " i.get('dx', 0.0),"
                + " i.get('dy', 0.0)) for i in dl1]");
        PyList pl = (PyList) py.get("p");
        for(Object i: pl) {
            Double md = (Double) ((PyTuple) i).get(0);
            Double tvd = (Double) ((PyTuple) i).get(1);
            Double dx = (Double) ((PyTuple) i).get(2);
            Double dy = (Double) ((PyTuple) i).get(3);
            dl.add(new DLElement(md, tvd, dx, dy));
        }
    }

    public double findTvd(double md) {
        return findPointOnTrajectory(md).getZ();
    }
    
    public Point findPointOnTrajectory(double md) {
        if(dl.isEmpty() || dl.size() == 1)
            return new Point(coords.getX(), coords.getY(), coords.getZ() - md);
        
        int ind = findIndex(md);
        if(ind >= 0)
            return new Point(coords.getX() + dl.get(ind).getDx(),
                    coords.getY() + dl.get(ind).getDy(),
                    dl.get(ind).getTvd());
        int ip = -ind - 1;
        System.out.println("++ ind = " + ind + " ins.pnt. = " + ip);
        if(ip < 0 || ip >= dl.size())
            throw new RuntimeException("Can't determine TVD by MD, md is outside of DirLog interval");
        double md1 = dl.get(ip-1).getMd();
        double md2 = dl.get(ip).getMd();
        double tvd1 = dl.get(ip-1).getTvd();
        double tvd2 = dl.get(ip).getTvd();
        double dx1 = dl.get(ip-1).getDx();
        double dx2 = dl.get(ip).getDx();
        double dy1 = dl.get(ip-1).getDy();
        double dy2 = dl.get(ip).getDy();
        return new Point(
                coords.getX() + interpolate(md, md1, md2, dx1, dx2),
                coords.getY() + interpolate(md, md1, md2, dy1, dy2),
                interpolate(md, md1, md2, tvd1, tvd2)
        );
    }
    
    public List<DLElement> getDl() {
        return dl;
    }

    public void setCoords(Point coords) {
        this.coords = coords;
    }
    
    
    
    private int findIndex(double md) {
        DLElement key = new DLElement(md, 0, 0, 0);
        int ind = Collections.binarySearch(dl, key, new Comparator<DLElement>() {
            @Override
            public int compare(DLElement o1, DLElement o2) {
                if(o1.md > o2.md)
                    return 1;
                if(o1.md < o2.md)
                    return -1;
                return 0;
            }
        });
        return ind;
    }
    
    private double interpolate(double x, double x1, double x2, double y1, double y2) {
        return y1 + (x - x1) * ((y2 - y1)/(x2 - x1));
    }

    static final double UNDEF = 3.4028234663852886e+38;
    private static final Logger LOG = Logger.getLogger(TVDCalculator.class.getName());
}
