package net.coderodde.loan.model.support;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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

    static final void linkGroup(List<Node> positiveNodeList,
                                List<Node> negativeNodeList,
                                Graph graph) {
        Set<Node> group = new HashSet<Node>(positiveNodeList.size() +
                                            negativeNodeList.size());

        group.addAll(positiveNodeList);
        group.addAll(negativeNodeList);
        linkGroup(group, graph);
    }

    static final void linkGroup(Set<Node> group, Graph in) {
        List<Node> positiveNodeList = new ArrayList<Node>(group.size());
        List<Node> negativeNodeList = new ArrayList<Node>(group.size());

        for (Node node : group) {
            if (node.getEquity() > 0L) {
                positiveNodeList.add(in.get(node.getName()));
            } else if (node.getEquity() < 0L) {
                negativeNodeList.add(in.get(node.getName()));
            }
        }

        long[] positiveEquityArray = new long[positiveNodeList.size()];
        long[] negativeEquityArray = new long[negativeNodeList.size()];

        int pi = 0;
        int ni = 0;

        for (Node node : group) {
            long e = node.getEquity();

            if (e > 0L) {
                positiveEquityArray[pi++] = e;
            } else if (e < 0L) {
                negativeEquityArray[ni++] = -e;
            } else {
                throw new IllegalStateException("Zero equity node encounterd.");
            }
        }

        link(positiveNodeList,
             negativeNodeList,
             positiveEquityArray,
             negativeEquityArray);
    }

    static final int resolveBinaryGroups(List<Node> positiveNodeList,
                                          List<Node> negativeNodeList,
                                          Graph graph) {

        long[] positiveEquityArray = getEquityArray(positiveNodeList);
        long[] negativeEquityArray = getEquityArray(negativeNodeList);

        int pi = positiveEquityArray.length - 1;
        int ni = negativeEquityArray.length - 1;

        int resolved = 0;

        while (pi >= 0 && ni >= 0) {
            if (positiveEquityArray[pi] > negativeEquityArray[ni]) {
                --pi;
            } else if (positiveEquityArray[pi] < negativeEquityArray[ni]) {
                --ni;
            } else {
                graph.get(positiveNodeList.get(pi).getName())
                     .connectTo(graph.get(negativeNodeList.get(ni).getName()),
                     positiveEquityArray[pi]);
                positiveNodeList.remove(pi--);
                negativeNodeList.remove(ni--);
                resolved++;
            }
        }

        return resolved;
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

    static final long sumNodeEquities(List<Node> nodeList) {
        long sum = 0L;

        for (Node node : nodeList) {
            sum += node.getEquity();
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

    static final Set<Node> createGroup(List<Node> positiveNodeList,
                                       List<Node> negativeNodeList,
                                       int[] positiveIndices,
                                       int[] negativeIndices) {
        Set<Node> group = new HashSet<Node>(positiveIndices.length +
                                            negativeIndices.length);

        for (int i : positiveIndices) {
            group.add(positiveNodeList.get(i));
        }

        for (int i : negativeIndices) {
            group.add(negativeNodeList.get(i));
        }

        return group;
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

    final static GroupOrderComparator groupOrderComparator =
             new GroupOrderComparator();

    static class GroupOrderComparator implements Comparator<Set<Node>> {

        @Override
        public int compare(Set<Node> g1, Set<Node> g2) {
            return g1.size() - g2.size();
        }
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

    static int countLinkageEdges(List<Node> positiveNodeList,
                                 List<Node> negativeNodeList,
                                 int[] positivePartition,
                                 int[] negativePartition) {
        int k = 0;

        for (int i : positivePartition) {
            k = Math.max(k, i);
        }

        ++k;

        List<Node>[] positiveMap = new List[k];
        List<Node>[] negativeMap = new List[k];

        int i = 0;

        for (int index : positivePartition) {
            if (positiveMap[index] == null) {
                positiveMap[index] =
                        new ArrayList<Node>(positivePartition.length);
            }

            positiveMap[index].add(positiveNodeList.get(i++));
        }

        i = 0;

        for (int index : negativePartition) {
            if (negativeMap[index] == null) {
                negativeMap[index] =
                        new ArrayList<Node>(negativePartition.length);
            }

            negativeMap[index].add(negativeNodeList.get(i++));
        }

        for (i = 0; i < k; ++i) {
            if (sumNodeEquities(positiveMap[i])
                    != sumNodeEquities(negativeMap[i])) {
                return Integer.MAX_VALUE;
            }
        }

        return positiveNodeList.size() + negativeNodeList.size() - k;
    }

    static void linkPartitions(List<Node> positiveNodes,
                               List<Node> negativeNodes,
                               int[] positiveIndices,
                               int[] negativeIndices,
                               Graph result) {
        int k = 0;

        for (int i : positiveIndices) {
            if (k < i) {
                k = i;
            }
        }

        ++k;

        // Here, k is the amount of groups.

        List<Node>[] positiveGroups = new List[k];
        List<Node>[] negativeGroups = new List[k];

        int i = 0;

        for (int index : positiveIndices) {
            if (positiveGroups[index] == null) {
                positiveGroups[index] =
                        new ArrayList<Node>(positiveNodes.size());
            }

            positiveGroups[index].add(positiveNodes.get(i++));
        }

        i = 0;

        for (int index : negativeIndices) {
            if (negativeGroups[index] == null) {
                negativeGroups[index] =
                        new ArrayList<Node>(negativeNodes.size());
            }

            negativeGroups[index].add(negativeNodes.get(i++));
        }

        for (i = 0; i < k; ++i) {
            checkGroup(positiveGroups[i], negativeGroups[i]);
            linkGroup(positiveGroups[i], negativeGroups[i], result);
        }
    }
}
