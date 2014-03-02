package net.coderodde.loan.model.support;

/**
 * This class generate all partitions of a set {0, 1, ..., N - 1}.
 *
 * @author coderodde
 * @version 1.6
 */
class PartitionGenerator {

    /**
     * The total amount of elements in a set.
     */
    private int n;

    /**
     * The indices for a partition.
     */
    private int[] s;

    /**
     * Internal book-keeping.
     */
    private int[] m;

    /**
     * Constructs a new partition generator.
     *
     * @param n the size of the set to partition.
     */
    PartitionGenerator(final int n) {
        check(n);
        this.n = n;
        this.s = new int[n];
        this.m = new int[n];
    }

    /**
     * Increments to the next partition, returning <code>true</code>.
     * If there is no partitions left, returns <code>false</code>.
     *
     * @return <code>false</code> if there is no more partitions left;
     * <code>true</code> otherwise.
     */
    boolean inc() {
        int i = 0;
        ++s[i];

        while (i < n - 1 && s[i] > m[i] + 1) {
            s[i++] = 0;
            ++s[i];
        }

        if (i == n - 1) {
            return false;
        }

        int max = s[i];

        for (--i; i >= 0; --i) {
            m[i] = max;
        }

        return true;
    }

    /**
     * Returns the indices for a partition.
     *
     * @return the indices for a partition.
     */
    int[] getIndices() {
        return s;
    }

    /**
     * Checks whether the size of the set is positive.
     *
     * @param n the size of a set.
     */
    private void check(final int n) {
        if (n < 1) {
            throw new IllegalArgumentException("'n' < 1.");
        }
    }

    static final void print(int[] arr) {
        for (int i : arr) {
            System.out.print(i + " ");
        }

        System.out.println();
    }

    public static void main(String... args) {
        PartitionGenerator pg = new PartitionGenerator(4);
        int[] indices = pg.getIndices();

        do {
            print(indices);
        } while (pg.inc());
    }
}
