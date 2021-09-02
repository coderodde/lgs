package net.coderodde.loan.model.support;

import net.coderodde.loan.model.Graph;
import net.coderodde.loan.model.Node;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
        
        a.connectToBorrower(b, 3L);
        b.connectToBorrower(c, 2L);
        c.connectToBorrower(a, 1L);
        
        Graph result = new CyclePurgeBypassSimplifier().simplify(g);
        
        System.out.println("DDDDD " + result);
        
        assertEquals(3, result.size());
        assertEquals(2, result.getEdgeAmount());
        
        assertEquals(1L, a.getArcWeight(c));
        assertEquals(1L, b.getArcWeight(c));
        
        assertEquals(2L, result.getTotalFlow());
    }
}
