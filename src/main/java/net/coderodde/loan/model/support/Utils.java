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

    static final void checkEquityArray(long[] equities) {
        for (long l : equities) {
            if (l < 1L) {
                throw new IllegalStateException(
                        "Illegal equity: " + l + "; must be at least 1.");
            }
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
