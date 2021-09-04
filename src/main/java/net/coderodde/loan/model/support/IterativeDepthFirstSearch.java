package net.coderodde.loan.model.support;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.coderodde.loan.model.Graph;
import net.coderodde.loan.model.Node;

/**
 * This class implements the iterative depth-first search for finding directed
 * cycles in an input graph.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Sep 2, 2021)
 * @since 1.6 (Sep 2, 2021)
 */
final class IterativeDepthFirstSearch {
    
    List<Node> findCycle(Graph graph) {
        for (Node root : graph) {
            List<Node> cycleCandidate = findCycleImpl(root);
            
            if (cycleCandidate != null) {
                return cycleCandidate;
            }
        }
        
        return null;
    }
    
    private List<Node> findCycleImpl(Node root) {
        Deque<Node> stack = new ArrayDeque<>(Arrays.asList(root));
        Map<Node, Node> parentMap = new HashMap<>();
        parentMap.put(root, null);
        
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            
            for (Node borrower : node) {
                if (!parentMap.containsKey(borrower)) {
                    stack.push(borrower);
                    parentMap.put(borrower, node);
                } else {
                    return tracebackCycle(borrower, node, parentMap);
                }
            }
        }
        
        return null;
    }
    
    private static List<Node> tracebackCycle(
            Node root, // borrower
            Node startNode, // node
            Map<Node, Node> parentMap) {
        
        List<Node> cycleList = new ArrayList<>();
        Set<Node> cycleSet = new HashSet<>();
        cycleSet.add(root);
        Node currentNode = startNode;

        while (!cycleSet.contains(currentNode)) {
            cycleList.add(currentNode);
            cycleSet.add(currentNode);
            currentNode = parentMap.get(currentNode);
        }

        cycleList.add(root);
        Collections.reverse(cycleList);
        return cycleList;
    }
}
