package net.coderodde.loan.model.support;

import java.util.List;
import net.coderodde.loan.model.Algorithm;
import net.coderodde.loan.model.Graph;
import net.coderodde.loan.model.Node;
import net.coderodde.loan.model.support.Utils.Triple;
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
        int bestEdgeAmount = Integer.MAX_VALUE;

        List<Integer>[] bestPositivePartition = null;
        List<Integer>[] bestNegativePartition = null;

        PartitionGenerator positiveGenerator;
        PartitionGenerator negativeGenerator;

        positiveGenerator = new PartitionGenerator(triple.first.size());
        negativeGenerator = new PartitionGenerator(triple.second.size());


        return ret;
    }

}
