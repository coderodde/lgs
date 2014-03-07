package net.coderodde.loan.model.support;

import java.util.List;
import net.coderodde.loan.model.Algorithm;
import net.coderodde.loan.model.Graph;
import net.coderodde.loan.model.Node;
import net.coderodde.loan.model.support.Utils.Triple;
import static net.coderodde.loan.model.support.Utils.countLinkageEdges;
import static net.coderodde.loan.model.support.Utils.linkPartitions;
import static net.coderodde.loan.model.support.Utils.split;

/**
 * This is the implementation of a partitional simplifier.
 *
 * @author coderodde
 * @version 1.6
 */
public class PartitionalSimplifier implements Algorithm {

    @Override
    public Graph simplify(Graph g) {
        Graph ret = new Graph(g);

        if (ret.size() < 2) {
            return ret;
        }

        Triple<List<Node>, List<Node>, List<Node>> triple = split(g);

        return (triple.first.size() < triple.second.size() ?
                simplifyImplPositiveOutermost(ret,
                                              triple.first,
                                              triple.second) :
                simplifyImplNegativeOutermost(ret,
                                              triple.first,
                                              triple.second));
    }

    private Graph simplifyImplPositiveOutermost(Graph graph,
                                                List<Node> positiveNodeList,
                                                List<Node> negativeNodeList) {
        int bestEdgeAmount = Integer.MAX_VALUE;
        PartitionGenerator outermostGenerator =
                new PartitionGenerator(positiveNodeList.size());

        int[] bestOutermostIndices = new int[positiveNodeList.size()];
        int[] bestInnermostIndices = new int[negativeNodeList.size()];

        do {
            int[] outermostIndices = outermostGenerator.getIndices();
            int k = outermostGenerator.getk();

            SpecialPartitionGenerator innermostGenerator =
                    new SpecialPartitionGenerator(negativeNodeList.size(), k);

            do {
                int[] innermostIndices = innermostGenerator.getIndices();
                int linkageEdges = countLinkageEdges(positiveNodeList,
                                                     negativeNodeList,
                                                     outermostIndices,
                                                     innermostIndices);

                if (bestEdgeAmount > linkageEdges) {
                    bestEdgeAmount = linkageEdges;

                    System.arraycopy(outermostIndices,
                                     0,
                                     bestOutermostIndices,
                                     0,
                                     outermostIndices.length);

                    System.arraycopy(innermostIndices,
                                     0,
                                     bestInnermostIndices,
                                     0,
                                     innermostIndices.length);
                }
            } while (innermostGenerator.inc());
        } while (outermostGenerator.inc());

        linkPartitions(positiveNodeList,
                       negativeNodeList,
                       bestOutermostIndices,
                       bestInnermostIndices,
                       graph);

        return graph;
    }

    private Graph simplifyImplNegativeOutermost(Graph graph,
                                                List<Node> positiveNodeList,
                                                List<Node> negativeNodeList) {
        int bestEdgeAmount = Integer.MAX_VALUE;
        PartitionGenerator outermostGenerator =
                new PartitionGenerator(negativeNodeList.size());

        int[] bestOutermostIndices = new int[negativeNodeList.size()];
        int[] bestInnermostIndices = new int[positiveNodeList.size()];

        do {
            int[] outermostIndices = outermostGenerator.getIndices();
            int k = outermostGenerator.getk();

            SpecialPartitionGenerator innermostGenerator =
                    new SpecialPartitionGenerator(positiveNodeList.size(), k);

            do {
                int[] innermostIndices = innermostGenerator.getIndices();
                int linkageEdges = countLinkageEdges(positiveNodeList,
                                                     negativeNodeList,
                                                     innermostIndices,
                                                     outermostIndices);

                if (bestEdgeAmount > linkageEdges) {
                    bestEdgeAmount = linkageEdges;

                    System.arraycopy(outermostIndices,
                                     0,
                                     bestOutermostIndices,
                                     0,
                                     outermostIndices.length);

                    System.arraycopy(innermostIndices,
                                     0,
                                     bestInnermostIndices,
                                     0,
                                     innermostIndices.length);
                }
            } while (innermostGenerator.inc());
        } while (outermostGenerator.inc());

        linkPartitions(positiveNodeList,
                       negativeNodeList,
                       bestInnermostIndices,
                       bestOutermostIndices,
                       graph);

        return graph;
    }
}
