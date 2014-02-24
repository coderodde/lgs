package net.coderodde.loan.model.support;

import java.util.ArrayList;
import java.util.Comparator;
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

    /**
     * Links a group and returns the amount of edges created.
     *
     * @param positiveNodeList the list of nodes with positive equities.
     * @param negativeNodeList the list of nodes with negative equities.
     * @param positiveEquityArray the array of positive equities.
     * @param negativeEquityArray the array of absolute values of negative
     * equities.
     *
     * @return the amount of edges created.
     */
    static final int link(List<Node> positiveNodeList,
                          List<Node> negativeNodeList,
                          long[] positiveEquityArray,
                          long[] negativeEquityArray) {
        checkEquityArray(positiveEquityArray);
        checkEquityArray(negativeEquityArray);
        checkIsGroup(positiveEquityArray,
                     negativeEquityArray);

        int pi = 0;
        int ni = 0;
        int edgeAmount = 0;

        final int nPositive = positiveEquityArray.length;

        while (pi < nPositive) {
            ++edgeAmount;

            if (positiveEquityArray[pi] > negativeEquityArray[ni]) {
                positiveNodeList.get(pi).connectTo(negativeNodeList.get(ni),
                                                   negativeEquityArray[ni]);
                positiveEquityArray[pi] -= negativeEquityArray[ni++];
            } else if (positiveEquityArray[pi] < negativeEquityArray[ni]) {
                positiveNodeList.get(pi).connectTo(negativeNodeList.get(ni),
                                                   positiveEquityArray[pi]);
                negativeEquityArray[ni] -= positiveEquityArray[pi++];
            } else {
                positiveNodeList.get(pi).connectTo(negativeNodeList.get(ni),
                                                   positiveEquityArray[pi]);
                ++pi;
                ++ni;
            }
        }

        return edgeAmount;
    }

    static Pair<Pair<List<Node>, List<Node>>, Pair<long[], long[]>>
            splitFromGraph(Graph g,
                           List<Node> positiveNodeList,
                           List<Node> negativeNodeList,
                           int[] positiveIndices,
                           int[] negativeIndices) {
        List<Node> newPositiveNodeList;
        List<Node> newNegativeNodeList;

        newPositiveNodeList = new ArrayList<Node>(positiveIndices.length);
        newNegativeNodeList = new ArrayList<Node>(negativeIndices.length);

        long[] newPositiveEquityArray = new long[positiveIndices.length];
        long[] newNegativeEquityArray = new long[negativeIndices.length];

        int i = 0;

        for (int index : positiveIndices) {
            Node node = positiveNodeList.get(index);
            newPositiveNodeList.add(g.get(node.getName()));
            newPositiveEquityArray[i++] = node.getEquity();
        }

        i = 0;

        for (int index : negativeIndices) {
            Node node = negativeNodeList.get(index);
            newNegativeNodeList.add(g.get(node.getName()));
            newNegativeEquityArray[i++] = -node.getEquity();
        }

        return new Pair<Pair<List<Node>, List<Node>>,
                        Pair<long[], long[]>>(
                            new Pair<List<Node>, List<Node>>(
                                newPositiveNodeList,
                                newNegativeNodeList),
                            new Pair<long[], long[]>(
                                newPositiveEquityArray,
                                newNegativeEquityArray));
    }

    static final long sumNodeEquities(List<Node> nodeList, int[] indices) {
        long sum = 0;

        for (int index : indices) {
            sum += nodeList.get(index).getEquity();
        }

        return Math.abs(sum);
    }

    static final void checkEquityArray(long[] equities) {
        for (long l : equities) {
            if (l < 1L) {
                throw new IllegalStateException(
                        "Illegal equity: " + l + "; must be at least 1.");
            }
        }
    }

    static final void removeNodesFromLists(List<Node> positiveNodes,
                                           List<Node> negativeNodes,
                                           int[] positiveIndices,
                                           int[] negativeIndices) {
        for (int i = positiveIndices.length - 1; i >= 0; --i) {
            positiveNodes.remove(positiveIndices[i]);
        }

        for (int i = negativeIndices.length - 1; i >= 0; --i) {
            negativeNodes.remove(negativeIndices[i]);
        }
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

    static final void checkIsGroup(long[] positiveEquities,
                                   long[] negativeEquities) {
        long sum = 0L;

        for (long l : positiveEquities) {
            sum += l;
        }

        for (long l : negativeEquities) {
            sum -= l;
        }

        if (sum != 0) {
            throw new IllegalStateException(
                    "Not a group equities; sum: " + sum);
        }
    }

    /**
     * Returns the array of nodes taken from a list;
     *
     * @param nodeList the nodes to return in an array.
     *
     * @return the array of nodes.
     */
    static Node[] toArray(List<Node> nodeList) {
        Node[] array = new Node[nodeList.size()];
        int index = 0;

        for (Node node : nodeList) {
            array[index++] = node;
        }

        return array;
    }

    static List<Node> toList(Node... nodes) {
        List<Node> list = new ArrayList<Node>(nodes.length);

        for (Node node : nodes) {
            list.add(node);
        }

        return list;
    }

    final static EquityComparator equityComparator = new EquityComparator();

    /**
     * This class compares nodes by absolute values of their equities.
     */
    static class EquityComparator implements Comparator<Node> {

        /**
         * Returns following values: <ul>
         *   <li>-1 if and only if the equity of <code>o1</code> is less than
         *       <code>o2</code> in absolute value,</li>
         *   <li>1 if and only if the equity of <code>o1</code> is more than
         *       <code>o2</code> in absolute value,</li>
         *   <li>0 if and only if the equities of two nodes are same in
         *       absolute value.</li>
         * </ul>
         *
         * @param o1 the first node.
         * @param o2 the second node.
         *
         * @return (See above.)
         */
        @Override
        public int compare(Node o1, Node o2) {
            long e1 = Math.abs(o1.getEquity());
            long e2 = Math.abs(o2.getEquity());
            return e1 < e2 ? -1 : (e1 > e2 ? 1 : 0);
        }
    }
}
