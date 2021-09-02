package net.coderodde.loan.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * This class models a loan graph.
 *
 * @author coderodde
 * @version 1.6
 */
public class Graph implements Iterable<Node> {

    /**
     * This map maps name of the nodes to respective node objects.
     */
    private Map<String, Node> nodeMap = new HashMap<>();

    /**
     * This list contains all the nodes currently stored in this graph.
     */
    private List<Node> nodeList = new ArrayList<>();

    /**
     * This variable caches the amount of edges in this graph.
     */
    protected int edgeAmount;

    /**
     * This variable caches the total flow of this graph
     * (sum of edge weights).
     */
    protected long flow;

    /**
     * Constructs an empty graph.
     */
    public Graph() {}

    /**
     * Constructs a graph with the same amount of nodes as in
     * <code>copy</code> with the same node names. Edges are copied as well,
     * and their respective arc weights are set correspondingly.
     *
     * @param copy the graph to copy.
     */
    public Graph(Graph copy) {
        final Map<Node, Node> map = new HashMap<>(nodeList.size());
        
        for (final Node node : copy) {
            final Node newNode = new Node(node);
            map.put(node, newNode);
            add(newNode);
        }
        
        for (final Node node : copy) {
            final Node tail = map.get(node);
            
            for (final Node borrower : node) {
                final Node head = map.get(borrower);
                tail.connectToBorrower(borrower);
                tail.setWeightTo(borrower, node.getWeightTo(borrower));
            }
        }
        
        this.edgeAmount = copy.edgeAmount;
    }
    
    @Override
    public String toString() {
        return "[" + nodeList.size() + " nodes, " + edgeAmount + " edges, " + 
               flow + " flow]";
    }

    /**
     * Adds a node to this graph if not already in this graph.
     *
     * @param node the node to add.
     */
    public void add(Node node) {
        if (nodeMap.containsKey(node.getName())) {
            return;
        }
        
        node.clear();
        node.ownerGraph = this;
        nodeMap.put(node.getName(), node);
        nodeList.add(node);
    }

    /**
     * Checks whether a node is included in this graph.
     *
     * @param node the node to query.
     * @return <code>true</code> if this graph contains the query node;
     * <code>false</code> otherwise.
     */
    public boolean contains(Node node) {
        return nodeMap.containsKey(node.getName());
    }

    /**
     * Returns a node with index <code>index</code>.
     *
     * @param index the node index.
     * @return the node at index <code>index</code>.
     */
    public Node get(int index) {
        return nodeList.get(index);
    }

    /**
     * Returns a node with name <code>name</code>.
     *
     * @param name the name of the query node.
     * @return the node with name <code>name</code>; <code>null</code>
     * otherwise.
     */
    public Node get(String name) {
        return nodeMap.get(name);
    }

    /**
     * Removes a node from this graph if present.
     *
     * @param node the node to remove.
     */
    public void remove(Node node) {
        if (nodeMap.containsKey(node.getName())) {
            nodeMap.remove(node.getName());
            nodeList.remove(node);

            node.clear();

            node.ownerGraph = null;
        }
    }

    /**
     * Returns the amount of nodes in this graph.
     *
     * @return the amount of nodes in this graph.
     */
    public int size() {
        return nodeList.size();
    }

    /**
     * Returns the amount of edges in this graph.
     *
     * @return the amount of edges in this graph.
     */
    public int getEdgeAmount() {
        return edgeAmount;
    }

    /**
     * Returns the total flow (sum of all edge weights) of this graph.
     *
     * @return the total flow of this graph.
     */
    public long getTotalFlow() {
        return flow;
    }

    /**
     * Returns an iterator over this graph's nodes.
     *
     * @return an iterator over this graph's nodes.
     */
    @Override
    public Iterator<Node> iterator() {
        return new NodeIterator();
    }

    public boolean isEquivalentTo(Graph g) {
        if (this.size() != g.size()) {
            return false;
        }

        for (Node node : this) {
            Node tmp = g.get(node.getName());

            if (tmp == null) {
                return false;
            }

            if (node.getEquity() != tmp.getEquity()) {
                return false;
            }
        }

        return true;
    }
    
    /**
     * This class implements the iterators over this graph's nodes.
     */
    private class NodeIterator implements Iterator<Node> {

        /**
         * The actual iterator.
         */
        private Iterator<Node> iterator = nodeList.iterator();

        /**
         * The last returned node.
         */
        private Node lastReturned;

        /**
         * Returns <code>true</code> if and only if there is more
         * nodes to iterate.
         *
         * @return <code>true</code> if and only if there is more nodes to
         * iterate.
         */
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        /**
         * Returns the next node or throws
         * <code>NoSuchElementException</code> if there is no more
         * nodes to iterate.
         *
         * @return the next node.
         */
        @Override
        public Node next() {
            return (lastReturned = iterator.next());
        }

        /**
         * Removes the current node from this graph.
         */
        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new NoSuchElementException(
                        "There is no current node to remove.");
            }

            iterator.remove();
            nodeMap.remove(lastReturned.getName());
            lastReturned.clear();
            lastReturned = null;
        }
    }
}
