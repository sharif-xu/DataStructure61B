import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/** HW #7, Sorting ranges.
 *  @author Ruize Xu
  */
public class Intervals {
    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals) {
        int max = -1000000, min = 0;
        for (int[] temp : intervals) {
            if (max < temp[1]) {
                max = temp[1];
            }
            if (min > temp[0]) {
                min = temp[0];
            }
        }
        int range = max - min;
        int[] count = new int[range];
        for (int[] temp : intervals) {
            for (int i = temp[0] - min; i < temp[1] - min; i++) {
                count[i] = 1;
            }
        }
        int count1 = 0;
        for (int i = 0; i < count.length; i++) {
            if (count[i] == 1) {
                count1++;
            }
        }
        return count1;
    }

    /** Test intervals. */
    static final int[][] INTERVALS = {
        {19, 30},  {8, 15}, {3, 10}, {6, 12}, {4, 5},
    };
    /** Covered length of INTERVALS. */
    static final int CORRECT = 23;

    /** Performs a basic functionality test on the coveredLength method. */
    @Test
    public void basicTest() {
        assertEquals(CORRECT, coveredLength(Arrays.asList(INTERVALS)));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
