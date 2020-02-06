import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testPrintRowAndCol() {
        int[][] arr1 = {{1, 3, 8}, {9, 25}, {21, 10}, {7}};
        MultiArr.printRowAndCol(arr1);
    }

    @Test
    public void testMaxValue() {
        // TODO: Your code here!
        int[][] arr1 = {{1, 9, 2}, {10, 25}, {22, 15}, {8}};
        assertEquals(MultiArr.maxValue(arr1), 25);
    }

    @Test
    public void testAllRowSums() {
        // TODO: Your code here!
        int[][] arr1 = {{5, 2, 8}, {9, 24}, {22, 10}, {9}};
        int[] res1 = {15, 33, 32, 9};
        assertArrayEquals(MultiArr.allRowSums(arr1), res1);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
