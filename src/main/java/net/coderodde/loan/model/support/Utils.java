package net.coderodde.loan.model.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.coderodde.loan.model.Graph;
import net.coderodde.loan.model.Node;

/**
 * This class contains the utility methods for simplifying graphs.
 * 
 * @author coderodde
 * @version 1.6
 */
public class Utils {
  
    public static final class Pair<F, S> {
        public F first;
        public S second;
        
        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }
        
        public Pair() {
            
        }
    }
    
    public static final class Triple<F, S, T> {
        public F first;
        public S second;
        public T third;
        
        public Triple(F first, S second, T third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
        
        public Triple() {
            
        }
    }
    
    /**
     * Splits the iterable in the triple of lists with positive,
     * negative and zero nodes, respectively.
     * 
     * @param iterable the iterable of nodes.
     * 
     * @return the triple of lists of nodes.
     */
    static final Triple<List<Node>, List<Node>, List<Node>> 
    split(Iterable<Node> iterable) {
        int size = 0;
        
        for (Node node : iterable) {
            ++size;
        }
        
        List<Node> positiveNodeList = new ArrayList<Node>(size);
        List<Node> negativeNodeList = new ArrayList<Node>(size);
        List<Node> zeroNodeList = new ArrayList<Node>(size);
        
        for (Node node : iterable) {
            if (node.getEquity() > 0L) {
                positiveNodeList.add(node);
            } else if (node.getEquity() < 0L) {
                negativeNodeList.add(node);
            } else {
                zeroNodeList.add(node);
            }
        }
        
        return new Triple<List<Node>, List<Node>, List<Node>>(positiveNodeList,
                                                              negativeNodeList,
                                                              zeroNodeList);
    }
    
    static final List<Node> copy(List<Node> nodeList) {
        List<Node> copyList = new ArrayList<Node>(nodeList.size());
        
        for (Node node : nodeList) {
            copyList.add(new Node(node));
        }
        
        return copyList;
    }
    
    static final long[] getEquityArray(List<Node> nodeList) {
        long[] equities = new long[nodeList.size()];
        int i = 0;
        
        for (Node node : nodeList) {
            equities[i++] = Math.abs(node.getEquity());
        }
        
        return equities;
    }
    
    static List<Node> fetchFromGraph(List<Node> nodes, Graph g) {
        List<Node> ret = new ArrayList<Node>(nodes.size());
        
        for (Node node : nodes) {
            Node tmp = g.get(node.getName());
            
            if (tmp != null) {
                ret.add(tmp);
            }
        }
        
        return ret;
    }
    
    static final int link(List<Node> positiveNodeList,
                          List<Node> negativeNodeList,
                          long[] positiveEquityArray,
                          long[] negativeEquityArray) {
        checkGroup(positiveNodeList, negativeNodeList);
        return -1;
    }
    
    static final void checkGroup(List<Node> positiveNodeList,
                                  List<Node> negativeNodeList) {
        long sum = 0L;
        
        for (Node node : positiveNodeList) {
            if (node.getEquity() < 1L) {
                throw new IllegalStateException(
                        "A non-positive node in positive list.");
            }
            
            sum += node.getEquity();
        }
        
        for (Node node : negativeNodeList) {
            if (node.getEquity() > -1L) {
                throw new IllegalStateException(
                        "A non-negative node in negative list.");
            }
            
            sum += node.getEquity();
        }
        
        if (sum != 0L) {
            throw new IllegalStateException("Not a group.");
        }
    }
}
