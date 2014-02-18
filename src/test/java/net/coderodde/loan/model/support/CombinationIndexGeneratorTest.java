package net.coderodde.loan.model.support;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class tests
 * <code>net.coderodde.loan.model.support.CombinationIndexGenerator</code>.
 *
 * @author coderodde
 * @version 1.6
 */
public class CombinationIndexGeneratorTest {

    @Test
    public void testInc() {
        CombinationIndexGenerator cig = new CombinationIndexGenerator(7);
        int[] indices;
        int row = 1;

        while ((indices = cig.inc()) != null) {
            System.out.printf("%3d: ", row++);
            print(indices);
        }

        assertEquals(128, row);
    }

    private static final void print(int[] array) {
        for (int i : array) {
            System.out.print(i + " ");
        }

        System.out.println();
    }
}
