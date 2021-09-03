package net.coderodde.loan.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class tests <code>net.coderodde.loan.Node</code>.
 *
 * @author coderodde
 * @version 1.6
 */
public class NodeTest {

    private Node u;
    private Node v;
    private Node w;
    private Graph g;

    @Before
    public void before() {
        u = new Node("A");
        v = new Node("B");
        w = new Node("C");
        g = new Graph();
    }

    @Test
    public void testGetName() {
        assertEquals("A", u.getName());
        assertEquals("B", v.getName());
    }

    @Test
    public void testConnectTo() {
        g.add(u);
        g.add(v);
        g.add(w);
        
        assertEquals(0L, u.getEquity());
        assertEquals(0L, v.getEquity());

        u.connectToBorrower(v);
        u.setWeightTo(v, 3L);
        
        assertEquals(3L, u.getEquity());
        assertEquals(-3L, v.getEquity());

        v.removeBorrower(u);

        assertEquals(3L, u.getEquity());
        assertEquals(-3L, v.getEquity());

        u.removeBorrower(v);

        assertEquals(0L, u.getEquity());
        assertEquals(0L, v.getEquity());
    }

    @Test
    public void testGetBorrowerAmount() {
        Graph g = new  Graph();
        
        g.add(u);
        g.add(v);
        g.add(w);
        
        assertEquals(0, u.getNumberOfBorrowers());
        u.connectToBorrower(v);
        assertEquals(1, u.getNumberOfBorrowers());
        u.connectToBorrower(w);
        assertEquals(2, u.getNumberOfBorrowers());
    }

    @Test
    public void testGetLoanTo() {
        Graph g = new  Graph();
        
        g.add(u);
        g.add(v);
        g.add(w);

        u.connectToBorrower(v);
        u.setWeightTo(v, 2L);
        v.connectToBorrower(w);
        v.setWeightTo(w, 1L);
        
        assertEquals(2L, u.getWeightTo(v));
        assertEquals(1L, v.getWeightTo(w));
    }

    @Test
    public void testToString() {
        Graph g = new Graph();
        
        g.add(u);
        g.add(v);
        g.add(w);
        
        assertEquals("[Node A; equity: 0]", u.toString());
        
        u.connectToBorrower(v);
        u.connectToBorrower(w);
        v.connectToBorrower(u);
        
        u.connectToBorrower(v);
        u.connectToBorrower(w);
        v.connectToBorrower(u);
        
        u.setWeightTo(v, 7L);
        u.setWeightTo(w, 4L);
        v.setWeightTo(u, 1L);
        
        assertEquals("[Node A; equity: 10]", u.toString());
    }

    @Test
    public void testEquals() {
        Node other = new Node("A");
        assertTrue(other.equals(u));
        assertTrue(u.equals(other));
        assertFalse(u.equals(v));
        assertFalse(v.equals(u));
    }

    @Test
    public void testIterator() {
        g.add(u);
        g.add(v);
        g.add(w);
        
        int count = 0;
        
        for (Node n : u) {
            count++;
        }

        assertEquals(0, count);

        count = 0;
        u.connectToBorrower(v);
        u.setWeightTo(v, 5L);
        
        for (Node n : u) {
            assertTrue(n == v);
            count++;
        }

        assertEquals(1, count);

        count = 0;

        u.connectToBorrower(w);
        u.setWeightTo(w, 4L);

        for (Node n : u) {
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    public void testGetEquity() {
        g.add(u);
        g.add(v);
        g.add(w);
        
        assertEquals(u.getEquity(), 0L);
        
        u.connectToBorrower(v);
        u.setWeightTo(v, 4L);
        
        assertEquals(u.getEquity(), 4L);
        
        u.connectToBorrower(w);
        u.setWeightTo(w, 3L);
        
        assertEquals(u.getEquity(), 7L);
        
        v.connectToBorrower(u);
        v.setWeightTo(u, 5L);
        
        assertEquals(u.getEquity(), 2L);
        
        w.connectToBorrower(u);
        w.setWeightTo(u, 9L);
        
        assertEquals(u.getEquity(), -7L);
    }

    @Test
    public void testIterators() {
        Graph g = new Graph();
        
        g.add(u);
        g.add(v);
        g.add(w);
        
        u.connectToBorrower(v);
        u.connectToBorrower(w);
        v.connectToBorrower(u);
        v.connectToBorrower(w);
        w.connectToBorrower(u);
        w.connectToBorrower(v);
        
        u.setWeightTo(v, 4L);
        u.setWeightTo(w, 5L);
        v.setWeightTo(u, 3L);
        v.setWeightTo(w, 1L);
        w.setWeightTo(u, 3L);
        w.setWeightTo(v, 7L);
        
        List<Node> nodes = new ArrayList<>();
        
        for (Node n : u) {
            nodes.add(n);
        }
        
        assertEquals(2, nodes.size());
        assertEquals(v, nodes.get(0));
        assertEquals(w, nodes.get(1));
        
        nodes.clear();
        
        for (Node n : u.parentIterable()) {
            nodes.add(n);
        }
        
        assertEquals(2, nodes.size());
        assertEquals(v, nodes.get(0));
        assertEquals(w, nodes.get(1));
        
        int i = 0;
        Iterator<Node> iter = u.iterator();
       
        while (iter.hasNext()) {
            iter.next();

            if (i == 1) {
                iter.remove();
            }
            
            i++;
        }
        
        assertEquals(2, i);
        assertEquals(1, u.getNumberOfBorrowers());
        assertEquals(2, u.getNumberOfLenders());
        
        iter = u.parentIterable().iterator();
        i = 0;
        
        while (iter.hasNext()) {
            iter.next();
            
            if (i == 1) {
                iter.remove();
            }
            
            i++;
        }
        
        assertEquals(2, i);
        assertEquals(1, u.getNumberOfBorrowers());
        assertEquals(1, u.getNumberOfLenders());
    }
}
