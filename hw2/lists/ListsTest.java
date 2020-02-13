package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *
 *  @author FIXME
 */

public class ListsTest {

    @Test
    public void myListsTests() {
        IntList test1 = IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11);
        int array[][] = {{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}};
        IntList test2 = IntList.list(1, 2, 3, 1, 3, 5, 1, 6, 9, 9, 9, 10);
        int array2[][] = {{1,2, 3}, {1, 3, 5}, {1, 6, 9}, {9}, {9, 10}};

        assertEquals(IntListList.list(array), Lists.naturalRuns(test1));
        assertEquals(IntListList.list(array2), Lists.naturalRuns(test2));
    }
    // It might initially seem daunting to try to set up
    // IntListList expected.
    //
    // There is an easy way to get the IntListList that you want in just
    // few lines of code! Make note of the IntListList.list method that
    // takes as input a 2D array.

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
