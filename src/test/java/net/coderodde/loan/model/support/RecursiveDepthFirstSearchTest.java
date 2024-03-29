package net.coderodde.loan.model.support;

import java.util.List;
import net.coderodde.loan.model.Graph;
import net.coderodde.loan.model.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecursiveDepthFirstSearchTest {

    private final RecursiveDepthFirstSearch dfs = 
              new RecursiveDepthFirstSearch();
    
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
        
        System.out.println(g.toDetailedString());
        
        List<Node> cycle = dfs.findCycle(g);
        
        assertEquals(3, cycle.size());
        
        assertEquals(nodeA, cycle.get(0));
        assertEquals(nodeB, cycle.get(1));
        assertEquals(nodeC, cycle.get(2));
        
        nodeA.removeBorrower(nodeB);
        
        cycle = dfs.findCycle(g);
        
        assertEquals(3, cycle.size());
        
        assertEquals(nodeA, cycle.get(0));
        assertEquals(nodeD, cycle.get(1));
        assertEquals(nodeC, cycle.get(2));
        
        nodeA.removeBorrower(nodeD);
        
        cycle = dfs.findCycle(g);
        
        assertNull(cycle);
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
        
        System.out.println(g.toDetailedString());
        
        List<Node> cycle = dfs.findCycle(g);
        
        assertEquals(3, cycle.size());
        
        assertEquals(a, cycle.get(0));
        assertEquals(b, cycle.get(1));
        assertEquals(c, cycle.get(2));
    }
    
    @Test
    public void bug1() {
        Graph g = new Graph();
        
        Node node0 = new Node("0");
        Node node1 = new Node("1");
        Node node2 = new Node("2");
        Node node3 = new Node("3");
        
        g.add(node0);
        g.add(node1);
        g.add(node2);
        g.add(node3);
        
        node1.connectToBorrower(node2);
        node1.connectToBorrower(node0);
        
        node1.setWeightTo(node2, 15);
        node1.setWeightTo(node0, 13);
        
        node2.connectToBorrower(node0);
        node2.connectToBorrower(node3);
        
        node2.setWeightTo(node0, 20);
        node2.setWeightTo(node3, 24);
        
        node3.connectToBorrower(node1);
        node3.setWeightTo(node1, 5);
        
        List<Node> cycle = dfs.findCycle(g);
        
        assertEquals(3, cycle.size());
        assertEquals(node1, cycle.get(0));
        assertEquals(node2, cycle.get(1));
        assertEquals(node3, cycle.get(2));
    }
}
