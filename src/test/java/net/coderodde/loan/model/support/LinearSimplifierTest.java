package net.coderodde.loan.model.support;

import net.coderodde.loan.model.Graph;
import net.coderodde.loan.model.Node;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class LinearSimplifierTest {
    
    @Test
    public void onThreeNodeGraph() {
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        
        Graph g = new Graph();
        
        g.add(a);
        g.add(b);
        g.add(c);
        
        a.connectToBorrower(b);
        b.connectToBorrower(c);
        c.connectToBorrower(a);
        
        a.setWeightTo(b, 3L);
        b.setWeightTo(c, 2L);
            c.setWeightTo(a, 1L);
        
        Graph result = new LinearSimplifier().simplify(g);
        
        assertEquals(3, result.size());
        assertEquals(2, result.getEdgeAmount());

        assertEquals(1L, result.get(0).getWeightTo(result.get(1)));
        assertEquals(1L, result.get(0).getWeightTo(result.get(2)));
        
        assertEquals(2L,  result.get(0).getEquity());
        assertEquals(-1L, result.get(1).getEquity());
        assertEquals(-1L, result.get(2).getEquity());
        
        assertEquals(2L, result.getTotalFlow());
        
        assertTrue(g.isEquivalentTo(result));
    }
}
