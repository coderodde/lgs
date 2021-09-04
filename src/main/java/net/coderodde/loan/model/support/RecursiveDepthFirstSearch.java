package net.coderodde.loan.model.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.coderodde.loan.model.Graph;
import net.coderodde.loan.model.Node;

/**
 * This class implements a recursive depth-first search variant returning a 
 * directed cycle.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Sep 4, 2021)
 * @since 1.6 (Sep 4, 2021)
 */
public class RecursiveDepthFirstSearch {

    private final Set<Node> marked = new HashSet<>();
    private final Set<Node> stack = new HashSet<>();
    private final Map<Node, Node> parents = new HashMap<>();
    
    public List<Node> findCycle(Graph graph) {
        for (Node root : graph) {
            if (!marked.contains(root)) {
                parents.put(root, null);
                
                List<Node> cycle = findCycleImpl(root);

                if (cycle != null) {
                    clearDataStructures();
                    return cycle;
                }
            }
        }
        
        clearDataStructures();
        return null;
    }
    
    private void clearDataStructures() {
        marked.clear();
        stack.clear();
        parents.clear();
    }
    
    private List<Node> findCycleImpl(Node root) {
        if (marked.contains(root)) {
            return null;
        }
        
        if (stack.contains(root)) {
            List<Node> cycle = new ArrayList<>();
            Node currentNode = parents.get(root);
            
            while (currentNode != root) {
                cycle.add(currentNode);
                currentNode = parents.get(currentNode);
            }
            
            cycle.add(root);
            Collections.<Node>reverse(cycle);
            return cycle;
        }
        
        stack.add(root);
        
        for (Node child : root) {
            parents.put(child, root);
            List<Node> cycleCandidate = findCycleImpl(child);
            
            if (cycleCandidate != null) {
                return cycleCandidate;
            }
        }
        
        stack.remove(root);
        marked.add(root);
        return null;
    }
}
