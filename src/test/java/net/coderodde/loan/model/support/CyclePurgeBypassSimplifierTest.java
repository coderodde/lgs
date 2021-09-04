package net.coderodde.loan.model.support;

import net.coderodde.loan.model.Graph;
import net.coderodde.loan.model.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class CyclePurgeBypassSimplifierTest {
    
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
        
        Graph result = new CyclePurgeBypassSimplifier().simplify(g);
        
        assertEquals(3, result.size());
        assertEquals(2, result.getEdgeAmount());

        assertEquals(1L, result.get(0).getWeightTo(result.get(1)));
        assertEquals(1L, result.get(0).getWeightTo(result.get(2)));
        
        assertEquals(2L,  result.get(0).getEquity());
        assertEquals(-1L, result.get(1).getEquity());
        assertEquals(-1L, result.get(2).getEquity());
        
        assertEquals(2L, result.getTotalFlow());
    }
    
    @Test
    public void onFourNodeGraph() {
        Node a = new Node("1");
        Node b = new Node("2");
        Node c = new Node("3");
        Node d = new Node("4");
        
        Graph g = new Graph();
        
        g.add(a);
        g.add(b);
        g.add(c);
        g.add(d);
        
        a.connectToBorrower(b);
        b.connectToBorrower(c);
        c.connectToBorrower(d);
        d.connectToBorrower(a);
        
        a.setWeightTo(b, 10L);
        b.setWeightTo(c, 10L);
        c.setWeightTo(d, 10L);
        d.setWeightTo(a, 10L);
        
        Graph result = new CyclePurgeBypassSimplifier().simplify(g);
        
        assertEquals(0, result.getEdgeAmount());
        assertEquals(0L, result.getTotalFlow());
    }
}
