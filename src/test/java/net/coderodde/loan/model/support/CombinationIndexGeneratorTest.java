package net.coderodde.loan.model.support;

import static org.junit.Assert.*;
import org.junit.Test;

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
            System.out.println(cig.hasNoGaps());
        }

        assertEquals(128, row);
    }

    @Test
    public void testRemove() {
        System.out.println("testRemove()");
        CombinationIndexGenerator cig = new CombinationIndexGenerator(5);
        int[] indices;
        int i = 0;

        while ((indices = cig.inc()) != null) {
            ++i;
            System.out.printf("%3d ", i);
            print(indices);
            System.out.println();

            if (indices.length == 3) {
                cig.remove();
            }
        }

        assertEquals(16, i);
    }

    @Test
    public void testRemove2() {
        System.out.println("testRemove2()");
        CombinationIndexGenerator cig = new CombinationIndexGenerator(5);
        int[] indices;
        int i = 0;

        while ((indices = cig.inc()) != null) {
            i++;
            System.out.printf("%3d: ", i);
            print(indices);
            System.out.println();

            if (i == 15) {
                cig.remove();
            }
        }

        assertEquals(16, i);
    }

    @Test
    public void testRemove3() {
        System.out.println("testRemove3()");
        CombinationIndexGenerator cig = new CombinationIndexGenerator(5);
        int[] indices;
        int i = 0;

        while ((indices = cig.inc()) != null) {
            i++;
            System.out.printf("%3d: ", i);
            print(indices);
            System.out.println();

            if (i == 13) {
                cig.remove();
            }
        }

        assertEquals(14, i);
    }

    @Test
    public void testRemove4() {
        System.out.println("testRemove4()");
        CombinationIndexGenerator cig = new CombinationIndexGenerator(5);
        int[] indices;
        int i = 0;

        while ((indices = cig.inc()) != null) {
            i++;
            System.out.printf("%3d: ", i);
            print(indices);
            System.out.println();

            if (i == 4) {
                cig.remove();
            }
        }

        assertEquals(16, i);
    }

    private static final void print(int[] array) {
        for (int i : array) {
            System.out.print(i + " ");
        }
    }
}
