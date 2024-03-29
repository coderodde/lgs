package net.coderodde.loan.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * This class tests <code>net.coderodde.loan.model.Graph</code>.
 *
 * @author coderodde
 * @version 1.6
 */
public class GraphTest {

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
    public void testAdd() {
        assertEquals(0, g.size());
        assertEquals(0, g.getEdgeAmount());
        
        g.add(u);
        
        assertEquals(1, g.size());
        assertEquals(0, g.getEdgeAmount());
        
        g.add(v);
        
        assertEquals(2, g.size());
        assertEquals(0, g.getEdgeAmount());
        
        u.connectToBorrower(v);
        u.setWeightTo(v, 10L);
        
        assertEquals(2, g.size());
        assertEquals(1, g.getEdgeAmount());
        assertEquals(10L, g.getTotalFlow());
    }

    @Test
    public void testContains() {
        assertFalse(g.contains(u));
        g.add(u);
        assertTrue(g.contains(u));
        assertFalse(g.contains(v));
        g.add(v);
        assertTrue(g.contains(v));
    }

    @Test
    public void testGet_int() {
        g.add(u);
        assertEquals(u, g.get(0));
        g.add(v);
        assertEquals(u, g.get(0));
        assertEquals(v, g.get(1));
    }

    @Test
    public void testGet_String() {
        assertNull(g.get("NONE"));
        g.add(u);
        g.add(v);
        assertEquals(u, g.get("A"));
        assertEquals(v, g.get("B"));
    }

    @Test
    public void testRemove() {
        assertEquals(0, g.size());
        assertEquals(0, g.getEdgeAmount());
        assertEquals(0, g.getTotalFlow());
        
        try {
            g.remove(u);
            fail("Graph.remove(Node) should have thrown an exception.");
        } catch (IllegalArgumentException ex) {
            
        }
        
        assertEquals(0, g.size());
        assertEquals(0, g.getEdgeAmount());
        assertEquals(0, g.getTotalFlow());
        
        g.add(u);
        g.add(v);
        u.connectToBorrower(v);
        u.setWeightTo(v, 10L);
        
        assertEquals(2, g.size());
        assertEquals(1, g.getEdgeAmount());
        assertEquals(10L, g.getTotalFlow());

        g.remove(v);

        assertEquals(1, g.size());
        assertEquals(0, g.getEdgeAmount());
        assertEquals(0L, g.getTotalFlow());
    }

    @Test
    public void testClone() {
        Graph clone = new Graph(g);

        assertEquals(0, clone.size());
        assertEquals(0, clone.getEdgeAmount());
        assertEquals(0L, clone.getTotalFlow());

        clone.add(u);

        assertEquals(1, clone.size());

        clone.add(v);

        assertEquals(2, clone.size());

        clone.add(w);

        clone.get(u.getName()).connectToBorrower(clone.get(v.getName()));
        clone.get(v.getName()).connectToBorrower(clone.get(w.getName()));
        clone.get(w.getName()).connectToBorrower(clone.get(u.getName()));

        clone.get(u.getName()).setWeightTo(clone.get(v.getName()), 2L);
        clone.get(v.getName()).setWeightTo(clone.get(w.getName()), 3L);
        clone.get(w.getName()).setWeightTo(clone.get(u.getName()), 4L);

        assertEquals(3, clone.getEdgeAmount());
        assertEquals(9L, clone.getTotalFlow());

        clone.get(w.getName()).connectToBorrower(clone.get(v.getName()));
        clone.get(w.getName()).setWeightTo(clone.get(v.getName()), 1L);

        assertEquals(4, clone.getEdgeAmount());
        assertEquals(10L, clone.getTotalFlow());

        Graph c = new Graph(clone);

        assertEquals(3, c.size());
        assertEquals(4, c.getEdgeAmount());
        assertEquals(10L, c.getTotalFlow());
    }

    @Test
    public void testGetEdgeAmount() {
        g.add(u);
        g.add(v);
        g.add(w);

        u.connectToBorrower(v);
        v.connectToBorrower(w);
        u.setWeightTo(v, 2L);
        v.setWeightTo(w, 3L);

        assertEquals(g.getEdgeAmount(), 2);

        w.connectToBorrower(u);
        w.setWeightTo(u, 5L);

        assertEquals(g.getEdgeAmount(), 3);

        g.remove(u);

        assertEquals(g.getEdgeAmount(), 1);
    }

    @Test
    public void testGetTotalFlow() {
        g.add(u);
        g.add(v);
        g.add(w);

        u.connectToBorrower(v);
        u.setWeightTo(v, 2L);

        assertEquals(g.getTotalFlow(), 2L);

        v.connectToBorrower(w);
        v.setWeightTo(w, 3L);

        assertEquals(g.getTotalFlow(), 5L);
        assertEquals(g.getEdgeAmount(), 2);

        w.connectToBorrower(u);
        w.setWeightTo(u, 5L);

        assertEquals(g.getTotalFlow(), 10L);
        assertEquals(g.getEdgeAmount(), 3);

        g.remove(u);

        assertEquals(g.getTotalFlow(), 3L);
        assertEquals(g.getEdgeAmount(), 1);
    }

    @Test
    public void testIterator() {
        int count = 0;

        for (Node node : g) {
            count++;
        }

        assertEquals(0, count);
        count = 0;
        g.add(u);

        for (Node node : g) {
            count++;
        }

        assertEquals(1, count);
        count = 0;
        g.add(w);

        for (Node node : g) {
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    public void removeFromIterator() {
        g.add(u);
        g.add(v);
        g.add(w);
        List<Node> nodes = new ArrayList<Node>(3);

        for (Node n : g) {
            nodes.add(n);
        }

        assertEquals(nodes.get(0), u);
        assertEquals(nodes.get(1), v);
        assertEquals(nodes.get(2), w);

        int i = 0;
        Iterator<Node> iterator = g.iterator();

        while (iterator.hasNext()) {
            iterator.next();

            if (i == 1) {
                iterator.remove();
            }

            i++;
        }

        assertEquals(2, g.size());
        nodes.clear();

        for (Node u : g) {
            nodes.add(u);
        }

        assertEquals(nodes.get(0), u);
        assertEquals(nodes.get(1), w);
    }

//    @Test
    public void testEquivalencyCheck() {
        g.add(u);
        g.add(v);
        g.add(w);

        Graph c = new Graph();

        c.add(new Node(u));
        c.add(new Node(v));

        assertFalse(g.isEquivalentTo(c));

        c.add(new Node(w));

        assertTrue(g.isEquivalentTo(c));

        Node x = new Node("X");
        c.add(x);

        assertFalse(g.isEquivalentTo(c));

        c.remove(x);

        assertTrue(g.isEquivalentTo(c));

        // Here, c and g have no arcs!
        
        u.connectToBorrower(v);
        u.setWeightTo(v, 4L);

        assertFalse(g.isEquivalentTo(c));

        c.get(0).connectToBorrower(c.get(1));
        c.get(0).setWeightTo(c.get(1), 4L);

        assertTrue(g.isEquivalentTo(c));

        // Here, c and g are equivalent with one arc!
        
        c.get(1).connectToBorrower(c.get(0));
        c.get(1).setWeightTo(c.get(0), 3L);

        assertFalse(g.isEquivalentTo(c));

        g.get(1).connectToBorrower(g.get(0));
        g.get(1).setWeightTo(g.get(0), 1L);

        assertTrue(g.isEquivalentTo(c));
    }
    
    @Test
    public void setWeightTo() {
        g.add(u);
        g.add(v);
        g.add(w);
        
        u.connectToBorrower(v);
        v.connectToBorrower(w);
        
        u.setWeightTo(v, 10L);
        v.setWeightTo(w, 5L);
        
        assertEquals(10L, u.getEquity());
        assertEquals(-5L, v.getEquity());
        assertEquals(-5L, w.getEquity());
        
        w.connectToBorrower(u);
        w.setWeightTo(u, 3L);
        
        assertEquals(7L, u.getEquity());
        assertEquals(-5L, v.getEquity());
        assertEquals(-2L, w.getEquity());
    }
    
    @Test
    public void equity() {
        g.add(u);
        g.add(v);
        
        u.connectToBorrower(v);
        u.setWeightTo(v, 100L);
        u.setWeightTo(v, 50L);
        
        assertEquals(50L, u.getWeightTo(v));
        assertEquals(50L, g.getTotalFlow());
        assertEquals(50L, u.getEquity());
        assertEquals(-50L, v.getEquity());
        
        v.connectToBorrower(u);
        v.setWeightTo(u, 1000L);
        
        assertEquals(1000L, v.getWeightTo(u));
        assertEquals(1050L, g.getTotalFlow());
        assertEquals(-950L, u.getEquity());
        assertEquals( 950L, v.getEquity());
    }
}
