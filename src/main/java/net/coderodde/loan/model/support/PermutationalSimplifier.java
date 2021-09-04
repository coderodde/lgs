package net.coderodde.loan.model.support;

import net.coderodde.loan.model.Algorithm;
import net.coderodde.loan.model.Graph;

/**
 * This class implements permutational simplifier.
 *
 * @author coderodde
 * @version 1.6
 */
public class PermutationalSimplifier implements Algorithm {

    @Override
    public Graph simplify(Graph g) {
        Graph ret = g.copyWithoutArcs();

        if (g.size() < 2) {
            return ret;
        }

        

        return ret;
    }
}
