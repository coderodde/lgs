package net.coderodde.loan.model.support;

import java.util.List;
import net.coderodde.loan.model.Algorithm;
import net.coderodde.loan.model.Graph;
import net.coderodde.loan.model.Node;
import net.coderodde.loan.model.support.Utils.Triple;

/**
 * This class implements the cycle purge/bypass simplifier. In cycle purge
 * technique, we search for directed cycles, choose the minimum weight 
 * {@code w}, subtract {@code w} from the weight of each arc in the cycle, and,
 * finally, remove all those arcs whose weight becomes zero.
 * <p>
 * In bypass technique, in order to lower the total flow, we choose two 
 * connected arcs {@code a_1 = (n_1, n_2), a_2 = (n_2, n_3)}, then we choose
 * the minimum weight {@code w} of the {@code a_1} and {@code a_2}, subtract
 * {@code w} from both the arcs, and rearrange the arcs such that the total flow
 * is lowered.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Sep 2, 2021)
 * @since 1.6 (Sep 2, 2021)
 */
public final class CyclePurgeBypassSimplifier implements Algorithm {

    @Override
    public Graph simplify(Graph g) {
        Graph resultGraph = new Graph(g);
        
        if (g.size() < 2) {
            return resultGraph;
        }
        
        IterativeDepthFirstSearch idfs = new IterativeDepthFirstSearch();
        List<Node> cycle;
        
        while ((cycle = idfs.findCycle(resultGraph)) != null) {
            resolveCycle(cycle);
        }
        
        Triple<Node, Node, Node> arcChainToBypass;
        
        while ((arcChainToBypass = findArcChain(resultGraph)) != null) {
            resolveArcChain(arcChainToBypass.first,
                            arcChainToBypass.second,
                            arcChainToBypass.third);
        }

        return resultGraph;
    }
    
    private static void resolveCycle(List<Node> cycle) {
        long minimumWeight = Long.MAX_VALUE;
        
        for (int i = 0; i < cycle.size(); i++) {
            Node lender = cycle.get(i);
            Node borrower = cycle.get((i + 1) % cycle.size());
            long arcWeight = lender.getWeightTo(borrower);
            minimumWeight = Math.min(minimumWeight, arcWeight);
        }
        
        for (int i = 0; i < cycle.size(); i++) {
            Node lender = cycle.get(i);
            Node borrower = cycle.get((i + 1) % cycle.size());
            long arcWeight = lender.getWeightTo(borrower);
            
            if (arcWeight == minimumWeight) {
                // Remove the minimum weight arc:
                lender.removeBorrower(borrower);
            } else {
                // Subtract 'minimumWeight' from the '(lender, borrower)' arc
                // weight:
                lender.setWeightTo(borrower, 
                        lender.getWeightTo(borrower) - minimumWeight);
            }
        }
    }
    
    private static Triple<Node, Node, Node> findArcChain(Graph graph) {
        for (Node root : graph) {
            return findArcChainImpl(root);
        }
        
        return null;
    }
    
    private static Triple<Node, Node, Node> findArcChainImpl(Node root) {
        for (Node child : root) {
            for (Node grandChild : child) {
                return new Triple<>(root, child, grandChild);
            }
        }
        
        return null;
    }
    
    private static void resolveArcChain(Node n1, Node n2, Node n3) {
        long weightN1N2 = n1.getWeightTo(n2);
        long weightN2N3 = n2.getWeightTo(n3);
        long minimumWeight = Math.min(weightN1N2, weightN2N3);
        
        n1.setWeightTo(n2, n1.getWeightTo(n2) - minimumWeight);
        n2.setWeightTo(n3, n2.getWeightTo(n3) - minimumWeight);
        
        if (n1.getWeightTo(n2) == 0L) {
            n1.removeBorrower(n2);
        }
        
        if (n2.getWeightTo(n3) == 0L) {
            n2.removeBorrower(n3);
        }
        
        if (n1.isConnectedTo(n3)) {
            n1.setWeightTo(n3, n1.getWeightTo(n3) + minimumWeight);
        } else {
            n1.connectToBorrower(n3);
            n1.setWeightTo(n3, minimumWeight);
        }
    }
}
