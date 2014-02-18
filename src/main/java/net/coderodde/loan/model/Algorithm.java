package net.coderodde.loan.model;

/**
 * This interface specifies the entry point of a loan graph simplifying
 * algorithm.
 *
 * @author coderodde
 * @version 1.6
 */
public interface Algorithm {

    /**
     * Computes and returns an equivalent loan graph to
     * <code>g</code>.
     *
     * @return the equivalent graph possibly with less edges.
     */
    Graph simplify(Graph g);
}
