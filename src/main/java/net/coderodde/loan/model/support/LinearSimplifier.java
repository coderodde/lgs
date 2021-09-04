package net.coderodde.loan.model.support;

import java.util.List;
import net.coderodde.loan.model.Algorithm;
import net.coderodde.loan.model.Graph;
import net.coderodde.loan.model.Node;
import net.coderodde.loan.model.support.Utils.Triple;
import static net.coderodde.loan.model.support.Utils.fetchFromGraph;
import static net.coderodde.loan.model.support.Utils.getEquityArray;
import static net.coderodde.loan.model.support.Utils.link;
import static net.coderodde.loan.model.support.Utils.split;

/**
 * This is a linear time simplifier that treats the entire graph as a
 * single group.
 *
 * @author coderodde
 * @version 1.6
 */
public class LinearSimplifier implements Algorithm {

    @Override
    public Graph simplify(Graph g) {
        Graph resultGraph = g.copyWithoutArcs(g);

        if (g.size() < 2) {
            return resultGraph;
        }

        Triple<List<Node>, List<Node>, List<Node>> triple = split(g);

        link(fetchFromGraph(triple.first,  resultGraph),
             fetchFromGraph(triple.second, resultGraph),
             getEquityArray(triple.first),
             getEquityArray(triple.second));

        return resultGraph;
    }
}
