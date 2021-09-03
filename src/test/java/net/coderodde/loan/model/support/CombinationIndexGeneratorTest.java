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
    @SuppressWarnings("empty-statement")
    public void testInc() {
        CombinationIndexGenerator cig = new CombinationIndexGenerator(7);
        int[] indices;
        int row = 1;

        while ((indices = cig.inc()) != null) {
            row++;
        }
        
        assertEquals(128, row);
    }

    @Test
    public void testRemove() {
        CombinationIndexGenerator cig = new CombinationIndexGenerator(5);
        int[] indices;
        int i = 0;

        while ((indices = cig.inc()) != null) {
            ++i;
            
            if (indices.length == 3) {
                cig.remove();
            }
        }

        assertEquals(16, i);
    }

    @Test
    public void testRemove2() {
        CombinationIndexGenerator cig = new CombinationIndexGenerator(5);
        int[] indices;
        int i = 0;

        while ((indices = cig.inc()) != null) {
            i++;

            if (i == 15) {
                cig.remove();
            }
        }

        assertEquals(16, i);
    }

    @Test
    public void testRemove3() {
        CombinationIndexGenerator cig = new CombinationIndexGenerator(5);
        int[] indices;
        int i = 0;

        while ((indices = cig.inc()) != null) {
            i++;

            if (i == 13) {
                cig.remove();
            }
        }

        assertEquals(14, i);
    }

    @Test
    public void testRemove4() {
        CombinationIndexGenerator cig = new CombinationIndexGenerator(5);
        int[] indices;
        int i = 0;

        while ((indices = cig.inc()) != null) {
            i++;

            if (i == 4) {
                cig.remove();
            }
        }

        assertEquals(16, i);
    }
}
