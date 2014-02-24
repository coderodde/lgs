package net.coderodde.loan.model.support;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.coderodde.loan.model.Algorithm;
import net.coderodde.loan.model.Graph;
import net.coderodde.loan.model.Node;
import net.coderodde.loan.model.support.Utils.Pair;
import net.coderodde.loan.model.support.Utils.Triple;
import static net.coderodde.loan.model.support.Utils.equityComparator;
import static net.coderodde.loan.model.support.Utils.removeNodesFromLists;
import static net.coderodde.loan.model.support.Utils.link;
import static net.coderodde.loan.model.support.Utils.split;
import static net.coderodde.loan.model.support.Utils.splitFromGraph;
import static net.coderodde.loan.model.support.Utils.sumNodeEquities;

/**
 * This is the implementation of a greedy combinatorial simplifier.
 *
 * @author coderodde
 * @version 1.6
 */
public class GreedyCombinatorialSimplifier implements Algorithm {

    @Override
    public Graph simplify(Graph g) {
        if (g.size() < 2) {
            return g;
        }

        Graph ret = new Graph(g);
        Triple<List<Node>, List<Node>, List<Node>> triple = split(g);

        List<Node> positiveNodeList = triple.first;
        List<Node> negativeNodeList = triple.second;

        Collections.sort(positiveNodeList, equityComparator);
        Collections.sort(negativeNodeList, equityComparator);

        CombinationIndexGenerator positiveGenerator;
        CombinationIndexGenerator negativeGenerator;

        positiveGenerator = new CombinationIndexGenerator(triple.first.size());
        negativeGenerator = new CombinationIndexGenerator(triple.second.size());

        int[] positiveIndices;
        int[] negativeIndices;

        outer:
        while ((positiveIndices = positiveGenerator.inc()) != null) {
            long currentPositiveSum = sumNodeEquities(positiveNodeList,
                                                      positiveIndices);

            while ((negativeIndices = negativeGenerator.inc()) != null) {
                long currentNegativeSum = sumNodeEquities(negativeNodeList,
                                                          negativeIndices);

                if (currentNegativeSum > currentPositiveSum) {
                    if (negativeGenerator.hasNoGaps()) {
                        negativeGenerator.reset();
                    }

                    continue outer;
                } else if (currentPositiveSum == currentNegativeSum) {
                    System.out.print(currentPositiveSum + ": ");

                    for (int i : positiveIndices) {
                        System.out.print(positiveNodeList.get(i).getEquity() + " ");
                    }

                    for (int i : negativeIndices) {
                        System.out.print(negativeNodeList.get(i).getEquity() + " ");
                    }

                    System.out.println();

                    Pair<Pair<List<Node>, List<Node>>,
                         Pair<long[], long[]>> data =
                            splitFromGraph(ret,
                                           positiveNodeList,
                                           negativeNodeList,
                                           positiveIndices,
                                           negativeIndices);

                    link(data.first.first,
                         data.first.second,
                         data.second.first,
                         data.second.second);

                    removeNodesFromLists(positiveNodeList,
                                         negativeNodeList,
                                         positiveIndices,
                                         negativeIndices);

                    positiveGenerator.remove();
                    negativeGenerator.remove();
                    continue outer;
                    // Yo, yo! :^)
                }
            }
        }

        return ret;
    }
}
