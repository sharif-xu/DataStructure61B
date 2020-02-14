package image;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *  @author Ruize Xu
 */

public class MatrixUtilsTest {
    @Test
    public void verticalTests() {
        double[][] origin = new double[][]{
                {1000000, 1000000, 1000000, 1000000},
                {1000000, 75990, 30003, 1000000},
                {1000000, 30002, 103046, 1000000},
                {1000000, 29515, 38273, 1000000},
                {1000000, 73403, 35399, 1000000},
                {1000000, 1000000, 1000000, 1000000}};
        double[][] re = new double[][]{
                {1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923, 2124919, 2124919, 2124919}
        };

        assertArrayEquals(re, MatrixUtils.accumulateVertical(origin));
    }

    @Test
    public void accumulateTests() {
        double[][] origin = new double[][]{
                {1000000, 1000000, 1000000, 1000000},
                {1000000, 75990, 30003, 1000000},
                {1000000, 30002, 103046, 1000000},
                {1000000, 29515, 38273, 1000000},
                {1000000, 73403, 35399, 1000000},
                {1000000, 1000000, 1000000, 1000000}};
        double[][] re = new double[][]{
                {1000000, 2000000, 2075990, 2060005},
                {1000000, 1075990, 1060005, 2060005},
                {1000000, 1030002, 1132561, 2060005},
                {1000000, 1029515, 1067788, 2064914},
                {1000000, 1073403, 1064914, 2064914},
                {1000000, 2000000, 2073403, 2064914}
        };

        assertArrayEquals(re, MatrixUtils.accumulate(origin, MatrixUtils.Orientation.HORIZONTAL));
    }

    @Test
    public void seamVerticalTests() {
        double[][] origin = new double[][]{
                {1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923, 2124919, 2124919, 2124919}
        };
        int[] array = new int[]{1, 2, 1, 1, 2, 1};

        assertArrayEquals(array, MatrixUtils.findVerticalSeam(origin));
    }

    @Test
    public void seamTests() {
        double[][] origin1 = new double[][]{
                {1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923, 2124919, 2124919, 2124919}
        };

        double[][] origin2 = new double[][]{
                {1000000, 2000000, 2075990, 2060005},
                {1000000, 1075990, 1060005, 2060005},
                {1000000, 1030002, 1132561, 2060005},
                {1000000, 1029515, 1067788, 2064914},
                {1000000, 1073403, 1064914, 2064914},
                {1000000, 2000000, 2073403, 2064914}
        };
        int[] array1 = new int[]{1, 2, 1, 1, 2, 1};
        int[] arr2 = new int[]{1, 2, 1, 0};

        assertArrayEquals(array1, MatrixUtils.findSeam(origin1, MatrixUtils.Orientation.VERTICAL));
        assertArrayEquals(arr2, MatrixUtils.findSeam(origin2, MatrixUtils.Orientation.HORIZONTAL));
    }
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
