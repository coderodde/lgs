package net.coderodde.loan.model;

import java.util.ArrayList;
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

    private Map<String, Node> nodeMap;
    private List<Node> nodeList;
    protected int edgeAmount;
    protected long flow;

    public Graph() {
        nodeMap = new LinkedHashMap<String, Node>();
        nodeList = new ArrayList<Node>();
    }

    public void add(Node node) {
        node.ownerGraph = this;
        node.clear();
        nodeMap.put(node.getName(), node);
        nodeList.add(node);
    }

    public boolean contains(Node node) {
        return nodeMap.containsKey(node.getName());
    }

    public Node get(int index) {
        return nodeList.get(index);
    }

    public Node get(String name) {
        return nodeMap.get(name);
    }

    public void remove(Node node) {
        if (nodeMap.containsKey(node.getName())) {
            nodeMap.remove(node.getName());
            nodeList.remove(node);

            node.clear();

            node.ownerGraph = null;
        }
    }

    public Graph clone() {
        Graph g = new Graph();

        for (Node node : this) {
            g.add(new Node(node.getName()));
        }

        return g;
    }

    public int size() {
        return nodeList.size();
    }

    public int getEdgeAmount() {
        return edgeAmount;
    }

    public long getTotalFlow() {
        return flow;
    }

    @Override
    public Iterator<Node> iterator() {
        return new NodeIterator();
    }

    private class NodeIterator implements Iterator<Node> {

        private Iterator<Node> iterator = nodeList.iterator();
        private Node lastReturned;

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Node next() {
            return (lastReturned = iterator.next());
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new NoSuchElementException(
                        "There is no current node to remove.");
            }

            iterator.remove();
            nodeMap.remove(lastReturned.getName());
            lastReturned = null;
        }
    }
}
