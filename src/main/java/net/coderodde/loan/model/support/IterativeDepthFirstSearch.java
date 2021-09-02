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
            final Node node = stack.pop();
                       // a      c
            for (Node borrower : node) {
                if (!parentMap.containsKey(borrower)) {
                    stack.push(borrower);
                    parentMap.put(borrower, node);
                } else {
                    final List<Node> cycle = new ArrayList<>();
                    Node n = node;
                    
                    do {
                        cycle.add(n);
                        n = parentMap.get(n);
                    } while (n != borrower);
                    
                    cycle.add(borrower);
                    Collections.reverse(cycle);
                    return cycle;
                }
            }
        }
        
        return null;
    }
}
