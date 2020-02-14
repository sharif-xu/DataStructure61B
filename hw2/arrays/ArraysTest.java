package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *  @author Ruize Xu
 */

public class ArraysTest {
    @Test
    public void catenateTest() {
        int[] array0 = new int[]{};
        int[] array1 = new int[]{1, 2, 3, 4};
        int[] array2 = new int[]{5, 6};
        int[] array3 = new int[]{1, 2, 3, 4, 5, 6};
        assertArrayEquals(array1, Arrays.catenate(array0,array1));
        assertArrayEquals(array3, Arrays.catenate(array1,array2));
    }

    @Test
    public void removeTest() {
        int[] array1 = new int[]{3, 4, 5};
        int[] array2 = new int[]{1, 4, 5};
        int[] array3 = new int[]{1, 2, 3, 4, 5};
        int[] array4 = new int[]{1, 2, 5};
        assertArrayEquals(array2, Arrays.remove(array3, 1, 2));
        assertArrayEquals(array1, Arrays.remove(array3, 0, 2));
    }

    @Test
    public void natualRunTests() {
        int[] array = new int[]{1, 3, 7, 5, 4, 6, 9, 10, 5};
        int[][] array1 = new int[][]{{1, 3, 7}, {5}, {4, 6, 9, 10}, {5},};

        assertArrayEquals(array1, Arrays.naturalRuns(array));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
