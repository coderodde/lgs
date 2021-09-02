package net.coderodde.loan.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.coderodde.loan.model.Node;
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

    @Before
    public void before() {
        u = new Node("A");
        v = new Node("B");
        w = new Node("C");
    }

    @Test
    public void testGetName() {
        assertEquals("A", u.getName());
        assertEquals("B", v.getName());
    }

    @Test
    public void testConnectTo() {
        assertEquals(0L, u.getEquity());
        assertEquals(0L, v.getEquity());

        u.connectToBorrower(v, 3L);

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
        assertEquals(0, u.getNumberOfBorrowers());
        u.connectToBorrower(v, 3L);
        assertEquals(1, u.getNumberOfBorrowers());
        u.connectToBorrower(w, 2L);
        assertEquals(2, u.getNumberOfBorrowers());
    }

    @Test
    public void testGetLoanTo() {
        assertEquals(0L, u.getArcWeight(v));

        u.connectToBorrower(v, 2L);
        u.connectToBorrower(w, 1L);

        assertEquals(2L, u.getArcWeight(v));
        assertEquals(1L, u.getArcWeight(w));
    }

    @Test
    public void testToString() {
        assertEquals("[Node A; equity: 0 ]", u.toString());
        u.connectToBorrower(v, 7L);
        u.connectToBorrower(w, 4L);
        v.connectToBorrower(u, 1L);
        assertEquals("[Node A; equity: 10 ]", u.toString());
    }

    @Test
    public void testHashCode() {
        Node other = new Node("A");
        assertTrue(other.hashCode() == u.hashCode());
        other.connectToBorrower(v, 3L);
        u.connectToBorrower(w, 5L);
        assertTrue(other.hashCode() == u.hashCode());
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
        int count = 0;

        for (Node n : u) {
            count++;
        }

        assertEquals(0, count);

        count = 0;
        u.connectToBorrower(v, 5L);

        for (Node n : u) {
            assertTrue(n == v);
            count++;
        }

        assertEquals(1, count);

        count = 0;

        u.connectToBorrower(w, 4L);

        for (Node n : u) {
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    public void testGetEquity() {
        assertEquals(u.getEquity(), 0L);
        u.connectToBorrower(v, 4L);
        assertEquals(u.getEquity(), 4L);
        u.connectToBorrower(w, 3L);
        assertEquals(u.getEquity(), 7L);
        v.connectToBorrower(u, 5L);
        assertEquals(u.getEquity(), 2L);
        w.connectToBorrower(u, 9L);
        assertEquals(u.getEquity(), -7L);
    }

    @Test
    public void testIterators() {
        u.connectToBorrower(v, 4L);
        u.connectToBorrower(w, 5L);
        v.connectToBorrower(u, 3L);
        v.connectToBorrower(w, 1L);
        w.connectToBorrower(u, 3L);
        w.connectToBorrower(v, 7L);
        
        List<Node> nodes = new ArrayList<Node>();
        
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
