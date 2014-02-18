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

        u.connectTo(v, 3L);

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
        assertEquals(0, u.getBorrowerAmount());
        u.connectTo(v, 3L);
        assertEquals(1, u.getBorrowerAmount());
        u.connectTo(w, 2L);
        assertEquals(2, u.getBorrowerAmount());
    }

    @Test
    public void testGetLoanTo() {
        assertEquals(0L, u.getLoanTo(v));

        u.connectTo(v, 2L);
        u.connectTo(w, 1L);

        assertEquals(2L, u.getLoanTo(v));
        assertEquals(1L, u.getLoanTo(w));
    }

    @Test
    public void testToString() {
        assertEquals("[Node A; equity: 0 ]", u.toString());
        u.connectTo(v, 7L);
        u.connectTo(w, 4L);
        v.connectTo(u, 1L);
        assertEquals("[Node A; equity: 10 ]", u.toString());
    }

    @Test
    public void testHashCode() {
        Node other = new Node("A");
        assertTrue(other.hashCode() == u.hashCode());
        other.connectTo(v, 3L);
        u.connectTo(w, 5L);
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
        u.connectTo(v, 5L);

        for (Node n : u) {
            assertTrue(n == v);
            count++;
        }

        assertEquals(1, count);

        count = 0;

        u.connectTo(w, 4L);

        for (Node n : u) {
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    public void testGetEquity() {
        assertEquals(u.getEquity(), 0L);
        u.connectTo(v, 4L);
        assertEquals(u.getEquity(), 4L);
        u.connectTo(w, 3L);
        assertEquals(u.getEquity(), 7L);
        v.connectTo(u, 5L);
        assertEquals(u.getEquity(), 2L);
        w.connectTo(u, 9L);
        assertEquals(u.getEquity(), -7L);
    }

    @Test
    public void testIterators() {
        u.connectTo(v, 4L);
        u.connectTo(w, 5L);
        v.connectTo(u, 3L);
        v.connectTo(w, 1L);
        w.connectTo(u, 3L);
        w.connectTo(v, 7L);
        
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
        assertEquals(1, u.getBorrowerAmount());
        assertEquals(2, u.getLenderAmount());
        
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
        assertEquals(1, u.getBorrowerAmount());
        assertEquals(1, u.getLenderAmount());
    }
}
