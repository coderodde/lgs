package net.coderodde.loan.model.support;

/**
 * Given a size (<tt>N</tt>) of a random access structure, this class generates
 * all possible index sets with all indices being distinct and no one of them
 * being greater or equal to <tt>N</tt>.
 *
 * Along generating new index sets, this structure support removing current
 * indices (call them <tt>S</tt>: in such a case the indices rearrange as if
 * in the addressed structure elements at indices <tt>S</tt> were removed.
 *
 * <p>Note that this structure is micro-optimized for fast operation at cost
 * of exposing implementation structures.</p>
 *
 * @author coderodde
 * @version 1.6
 */
class CombinationIndexGenerator {

    private static final int MINIMUM_SIZE = 1;

    /**
     * Denotes the upper bound on amount of indices and the maximum value of
     * each index.
     */
    private int n;

    /**
     * Denotes the current length of index sets.
     */
    private int k;

    /**
     * Stores the actual indices.
     */
    private int[] indices;

    CombinationIndexGenerator(final int n) {
        checkSize(n);
        this.n = n;
        this.k = 1;
        this.indices = new int[1];
        this.indices[0] = -1;
    }

    /**
     * Returns next index list or <code>null</code> if all possible combination
     * indices were generated.
     *
     * @return the new index list or <code>null</code> in case there is no more
     * lists to generate.
     */
    int[] inc() {
        if (k < 1 || k > n) {
            return null;
        }

        if (indices[k - 1] == n - 1) {
            int i;

            for (i = k - 2; i >= 0; i--) {
                if (indices[i] + 1 < indices[i + 1]) {
                    ++indices[i++];

                    while (i < k) {
                        indices[i] = indices[i - 1] + 1;
                        ++i;
                    }

                    return indices;
                }
            }

            k++;

            if (k > n) {
                --k;
                return null;
            }

            indices = new int[k];

            for (int j = 0; j < k; ++j) {
                indices[j] = j;
            }

            return indices;
        } else {
            ++indices[k - 1];
            return indices;
        }
    }

    boolean hasNoGaps() {
        for (int i = 0; i < k - 1; ++i) {
            if (indices[i] + 1 != indices[i + 1]) {
                return false;
            }
        }

        return true;
    }

    void reset() {
        k = 1;
        indices = new int[]{ -1 };
    }

    void remove() {
        final int oldn = n;
        n -= k;

        if (k > n) {
            k = n;
            indices = new int[k];

            for (int i = 0; i < k; ++i) {
                indices[i] = i;
            }
        } else if (n == k) {
            if (indices[0] == 0) {
                for (int i = 0; i < k - 1; ++i) {
                    indices[i] = i;
                }

                indices[k - 1] = k - 2;
            } else {
                for (int i = 0; i < k; ++i) {
                    indices[i] = i;
                }
            }
        } else {
            final int emptyRightSpots = oldn - k - indices[0];

            if (emptyRightSpots < k) {
                indices = new int[++k];

                for (int i = 1; i < k; ++i) {
                    indices[i] = i;
                }
            } else {
                for (int i = 1, j = indices[0] + 1; i < k; ++i, ++j) {
                    indices[i] = j;
                }
            }

            --indices[k - 1];
        }
    }

    private void checkSize(final int n) {
        if (n < MINIMUM_SIZE) {
            throw new IllegalArgumentException(
                    "'n' must be at least " + MINIMUM_SIZE);
        }
    }
}
