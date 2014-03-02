package net.coderodde.loan.model.support;

/**
 * This class generates all possible partition of a set into <tt>k</tt>
 * blocks. The value of <tt>k</tt> is given at construction.
 *
 * @author coderodde
 * @version 1.6
 */
class SpecialPartitionGenerator {

    /**
     * The total amount of elements in a set.
     */
    private final int n;

    /**
     * The total amount of blocks.
     */
    private final int k;

    /**
     * The indices for a partition.
     */
    private final int[] s;

    /**
     * Internal book-keeping.
     */
    private final int[] m;

    /**
     * Constructs a new partition generator.
     *
     * @param n the size of the set to partition.
     */
    SpecialPartitionGenerator(final int n, final int k) {
        check(n, k);
        this.n = n;
        this.k = k;
        this.s = new int[n];
        this.m = new int[n];

        for (int i = 0; i < n - k + 1; ++i) {
            s[i] = m[i] = 0;
        }

        for (int i = n - k + 1; i < n; ++i) {
            s[i] = m[i] = i - n + k;
        }
    }

    /**
     * Increments to the next partition, returning <code>true</code>.
     * If there is no partitions left, returns <code>false</code>.
     *
     * @return <code>false</code> if there is no more partitions left;
     * <code>true</code> otherwise.
     */
    boolean inc() {
        for (int i = n - 1; i > 0; --i) {
            if (s[i] < k - 1 && s[i] <= m[i - 1]) {
                s[i]++;
                m[i] = Math.max(m[i], s[i]);

                for (int j = i + 1; j < n - k + m[i] + 1; ++j) {
                    s[j] = 0;
                    m[j] = m[i];
                }

                for (int j = n - k + m[i] + 1; j < n; ++j) {
                    s[j] = m[j] = k - n + j;
                }

                return true;
            }
        }

        return false;
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
    private void check(final int n, final int k) {
        if (n < 1) {
            throw new IllegalArgumentException("'n' < 1.");
        }

        if (k < 1) {
            throw new IllegalArgumentException("'k' < 1.");
        }

        if (k > n) {
            throw new IllegalArgumentException("'k' > 'n'.");
        }
    }

    static final void print(int[] arr) {
        for (int i : arr) {
            System.out.print(i + " ");
        }

        System.out.println();
    }

    public static void main(String... args) {
        SpecialPartitionGenerator pg = new SpecialPartitionGenerator(4, 2);
        int[] indices = pg.getIndices();

        do {
            print(indices);
        } while (pg.inc());
    }
}

