package net.coderodde.loan;

import java.util.Random;
import net.coderodde.loan.model.Algorithm;
import net.coderodde.loan.model.Graph;
import net.coderodde.loan.model.Node;
import net.coderodde.loan.model.support.GreedyCombinatorialSimplifier;
import net.coderodde.loan.model.support.LinearSimplifier;

/**
 * This class is the entry for performance demo.
 *
 * @author coderodde
 * @version 1.6
 */
public class Demo {

    public static void main(String... args) {
        title("Profiling the simplification algorithms");

        final long SEED = System.currentTimeMillis(); //1393260761409L; //System.currentTimeMillis();
        final int N = 12;
        final Random r = new Random(SEED);

        System.out.println("Seed: " + SEED);

        Graph input = createGraph(N, 0.5f, 30L, r);

        System.out.println(
                "Amount of edges in the input graph: " + input.getEdgeAmount());

        System.out.println(
                "The total flow of the input graph:  " + input.getTotalFlow());

        profile(new LinearSimplifier(), input);
        profile(new GreedyCombinatorialSimplifier(), input);
        bar();
        System.out.println("Bye, bye!");
    }

    private static final void profile(Algorithm algorithm, Graph graph) {
        title2(algorithm.getClass().getSimpleName());

        long ta = System.currentTimeMillis();
        Graph result = algorithm.simplify(graph);
        long tb = System.currentTimeMillis();

        System.out.println("Time: " + (tb - ta) + " ms.");
        System.out.println("Edges left: " + result.getEdgeAmount());
        System.out.println("Total flow: " + result.getTotalFlow());
        System.out.println("Equivalent: " + graph.isEquivalentTo(result));
    }

    private static final Graph createGraph(final int size,
                                           final float loadFactor,
                                           final long maxLoan,
                                           final Random r) {
        Graph ret = new Graph();

        for (int i = 0; i < size; ++i) {
            ret.add(new Node("" + i));
        }

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (i != j) {
                    if (r.nextFloat() < loadFactor) {
                        ret.get(i)
                           .connectTo(ret.get(j),
                                      Math.abs(r.nextLong()) % maxLoan + 1L);
                    }
                }
            }
        }

        return ret;
    }

    private static final int BAR_LENGTH = 80;

    private static final void bar() {
        StringBuilder sb = new StringBuilder(BAR_LENGTH);

        for (int i = 0; i < BAR_LENGTH; ++i) {
            sb.append('-');
        }

        System.out.println(sb.toString());
    }

    private static final void titleImpl(String s, char c) {
        StringBuilder sb = new StringBuilder(BAR_LENGTH);
        final int left = (BAR_LENGTH - s.length() - 2) >> 1;
        final int right = BAR_LENGTH - s.length() - 2 - left;

        for (int i = 0; i < left; ++i) {
            sb.append(c);
        }

        sb.append(' ')
          .append(s)
          .append(' ');

        for (int i = 0; i < right; ++i) {
            sb.append(c);
        }

        System.out.println(sb.toString());

    }

    private static final void title(String s) {
        titleImpl(s, '*');
    }

    private static final void title2(String s) {
        titleImpl(s, '-');
    }
}
