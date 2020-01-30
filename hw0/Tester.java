import org.junit.Test;
import static org.junit.Assert.*;

import ucb.junit.textui;

/** Tests for hw0. 
 *  @author Ruize Xu
 */
public class Tester {

    /* Feel free to add your own tests.  For now, you can just follow
     * the pattern you see here.  We'll look into the details of JUnit
     * testing later.
     *
     * To actually run the tests, just use
     *      java Tester 
     * (after first compiling your files).
     *
     * DON'T put your HW0 solutions here!  Put them in a separate
     * class and figure out how to call them from here.  You'll have
     * to modify the calls to max, threeSum, and threeSumDistinct to
     * get them to work, but it's all good practice! */

    @Test
    public void maxTest() {
        // Change call to max to make this call yours.
        int[] arr1 = {1, 2, -2, -1, 3, 8, 24};
        assertEquals(hw0.max(arr1), 24);
        int[] arr2 = {1, 1, 1};
        assertEquals(hw0.max(arr2), 1);
        // REPLACE THIS WITH MORE TESTS.
    }

    @Test
    public void threeSumTest() {
        // Change call to threeSum to make this call yours.
        int[] arr1 = {1, 2, 3, 0};
        assertTrue(hw0.Three_SUM(arr1));
        int[] arr2 = {0, 0, 0, 0};
        assertTrue(hw0.Three_SUM(arr2));
        // REPLACE THIS WITH MORE TESTS.
    }

    @Test
    public void threeSumDistinctTest() {
        // Change call to threeSumDistinct to make this call yours.
        int[] arr1 = {-1, 1, 0};
        assertTrue(hw0.Three_SUM_DISTINCT(arr1));
        int[] arr2 = {0, 0, 0, 0};
        assertTrue(hw0.Three_SUM_DISTINCT(arr2));
        // REPLACE THIS WITH MORE TESTS.
    }

    public static void main(String[] unused) {
        textui.runClasses(Tester.class);
    }

}
