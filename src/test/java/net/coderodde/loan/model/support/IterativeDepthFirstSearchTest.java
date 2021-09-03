package net.coderodde.loan.model.support;

import java.util.List;
import net.coderodde.loan.model.Graph;
import net.coderodde.loan.model.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class IterativeDepthFirstSearchTest {

    private final IterativeDepthFirstSearch dfs = 
              new IterativeDepthFirstSearch();
    
    public void findCyclesTrhowsOnSingleNodeSelfLoop() {
        Graph g = new Graph();
        Node nodeA = new Node("A");
        g.add(nodeA);
        nodeA.connectToBorrower(nodeA);
        nodeA.setWeightTo(nodeA, 2L);
        List<Node> cycle = dfs.findCycle(g);
        
        assertEquals(1, cycle.size());
        assertEquals(nodeA, cycle.get(0));
        assertEquals(2L, nodeA.getEquity());
        assertEquals(1, g.size());
        assertEquals(1, g.getEdgeAmount());
    }
    
    @Test
    public void findCycles1() {
        Graph g = new Graph();
        
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        
        g.add(nodeA);
        g.add(nodeB);
        
        nodeA.connectToBorrower(nodeB);
        nodeB.connectToBorrower(nodeA);
        
        nodeA.setWeightTo(nodeB, 2L);
        nodeB.setWeightTo(nodeA, 3L);
        
        List<Node> cycle = dfs.findCycle(g);
        
        assertEquals(2, cycle.size());
        assertEquals(nodeA, cycle.get(0));
        assertEquals(nodeB, cycle.get(1));
    }
    
    @Test
    public void findCycles2() {
        Graph g = new Graph();
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        
        g.add(nodeA);
        g.add(nodeB);
        g.add(nodeC);
        
        nodeA.connectToBorrower(nodeB);
        nodeB.connectToBorrower(nodeC);
        nodeC.connectToBorrower(nodeA);
        
        nodeA.setWeightTo(nodeB, 2L);
        nodeB.setWeightTo(nodeC, 3L);
        nodeC.setWeightTo(nodeA, 4L);
        
        List<Node> cycle = dfs.findCycle(g);
        
        assertEquals(3, cycle.size());
        assertEquals(nodeA, cycle.get(0));
        assertEquals(nodeB, cycle.get(1));
        assertEquals(nodeC, cycle.get(2));
    }
    
    @Test
    public void findCycles4() {
        Graph g = new Graph();
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        Node nodeD = new Node("D");
        
        g.add(nodeA);
        g.add(nodeB);
        g.add(nodeC);
        g.add(nodeD);
        
        nodeA.connectToBorrower(nodeB);
        nodeB.connectToBorrower(nodeC);
        nodeC.connectToBorrower(nodeA);
        
        nodeA.setWeightTo(nodeB, 2L);
        nodeB.setWeightTo(nodeC, 3L);
        nodeC.setWeightTo(nodeA, 10L);
        
        nodeA.connectToBorrower(nodeD);
        nodeD.connectToBorrower(nodeC);
        nodeC.connectToBorrower(nodeA);
        
        nodeA.setWeightTo(nodeD, 2L);
        nodeD.setWeightTo(nodeC, 3L);
        nodeC.setWeightTo(nodeA, 10L);
        
        List<Node> cycle = dfs.findCycle(g);
        
        assertEquals(3, cycle.size());
        
        assertEquals(nodeA, cycle.get(0));
        assertEquals(nodeD, cycle.get(1));
        assertEquals(nodeC, cycle.get(2));
        
        nodeA.removeBorrower(nodeD);
        
        cycle = dfs.findCycle(g);
        
        assertEquals(3, cycle.size());
        
        assertEquals(nodeA, cycle.get(0));
        assertEquals(nodeB, cycle.get(1));
        assertEquals(nodeC, cycle.get(2));
        
        nodeA.removeBorrower(nodeB);
        
        assertNull(dfs.findCycle(g));
    }
    
    @Test
    public void bugTail() {
        //  ROOT
        //   | 
        //   v
        //   A
        //  / \
        // B-->C
        
        Graph g = new Graph();
        
        Node root = new Node("ROOT");
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        
        g.add(root);
        g.add(a);
        g.add(b);
        g.add(c);
        
        root.connectToBorrower(a);
        a.connectToBorrower(b);
        b.connectToBorrower(c);
        c.connectToBorrower(a);
        
        root.setWeightTo(a, 1L);
        a.setWeightTo(b, 1L);
        b.setWeightTo(c, 1L);
        c.setWeightTo(a, 1L);
        
        List<Node> cycle = dfs.findCycle(g);
        
        assertEquals(3, cycle.size());
        
        assertEquals(a, cycle.get(0));
        assertEquals(b, cycle.get(1));
        assertEquals(c, cycle.get(2));
    }
}
