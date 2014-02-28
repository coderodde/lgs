package net.coderodde.loan.model.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.coderodde.loan.model.Algorithm;
import net.coderodde.loan.model.Graph;
import net.coderodde.loan.model.Node;
import net.coderodde.loan.model.support.Utils.Triple;
import static net.coderodde.loan.model.support.Utils.createGroup;
import static net.coderodde.loan.model.support.Utils.equityComparator;
import static net.coderodde.loan.model.support.Utils.groupOrderComparator;
import static net.coderodde.loan.model.support.Utils.linkGroup;
import static net.coderodde.loan.model.support.Utils.resolveBinaryGroups;
import static net.coderodde.loan.model.support.Utils.split;
import static net.coderodde.loan.model.support.Utils.sumNodeEquities;

/**
 * This is the implementation of an exact combinatorial simplifier.
 *
 * @author coderodde
 * @version 1.6
 */
public class ExactCombinatorialSimplifier2 implements Algorithm {

    @Override
    public Graph simplify(Graph g) {
        Graph ret = new Graph(g);

        if (g.size() < 2) {
            return ret;
        }

        Triple<List<Node>, List<Node>, List<Node>> triple = split(g);

        List<Node> positiveNodeList = triple.first;
        List<Node> negativeNodeList = triple.second;

        Collections.sort(positiveNodeList, equityComparator);
        Collections.sort(negativeNodeList, equityComparator);

        resolveBinaryGroups(positiveNodeList, negativeNodeList, ret);

        CombinationIndexGenerator positiveGenerator;
        CombinationIndexGenerator negativeGenerator;

        int[] positiveIndices;
        int[] negativeIndices;

        Deque<Set<Node>> groups = new LinkedList<Set<Node>>();

        positiveGenerator =
                new CombinationIndexGenerator(positiveNodeList.size());

        outer:
        while ((positiveIndices = positiveGenerator.inc()) != null) {
            long currentPositiveSum = sumNodeEquities(positiveNodeList,
                                                      positiveIndices);

            negativeGenerator =
                    new CombinationIndexGenerator(negativeNodeList.size());

            while ((negativeIndices = negativeGenerator.inc()) != null) {
                long currentNegativeSum = sumNodeEquities(negativeNodeList,
                                                          negativeIndices);

                if (currentNegativeSum > currentPositiveSum) {
                    if (negativeIndices[0] == 0
                            && negativeGenerator.hasNoGaps()) {
                        continue outer;
                    }
                } else if (currentPositiveSum == currentNegativeSum) {
                    groups.add(createGroup(positiveNodeList,
                                           negativeNodeList,
                                           positiveIndices,
                                           negativeIndices));
                }
            }
        }

        Set<Node>[] groupArray = new Set[groups.size()];
        int index = 0;

        for (Set<Node> group : groups) {
            groupArray[index++] = group;
        }

        Arrays.sort(groupArray, groupOrderComparator);

        int n = 0;
        int i = 0;
        final int N = positiveNodeList.size() + negativeNodeList.size();

        while (n < N) {
            n += groupArray[i++].size();
        }

        CombinationIndexGenerator cig =
                new CombinationIndexGenerator(groupArray.length);

        int bestGroupAmount = -1;
        List<Set<Node>> bestGroupList = new ArrayList<Set<Node>>(n);
        Set<Node> set = new HashSet<Node>(n);

        outer2:
        for (;;) {
            if (cig.getK() > n) {
                break;
            }

            int[] indices = cig.inc();

            if (indices == null) {
                break;
            }

            set.clear();

            for (int index2 : indices) {
                if (Collections.disjoint(set, groupArray[index2])) {
                    set.addAll(groupArray[index2]);
                } else {
                    continue outer2;
                }
            }

            if (set.size() == N) {
                if (bestGroupAmount < indices.length) {
                    bestGroupAmount = indices.length;
                    bestGroupList.clear();

                    for (int index2 : indices) {
                        bestGroupList.add(groupArray[index2]);
                    }
                }
            }
        }

        for (Set<Node> group : bestGroupList) {
            linkGroup(group, ret);
        }

        return ret;
    }
}
