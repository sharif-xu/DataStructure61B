import java.util.Arrays;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 1; i < k; i++) {
                for (int j = i; j > 0; j--) {
                    if (array[j] < array[j - 1]) {
                        swap(array, j, j - 1);
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 0; i < k; i++) {
                int currentMin = array[i];
                int currentIndex = i;
                for (int j = i; j < k; j++) {
                    if (array[j] < currentMin) {
                        currentMin = array[j];
                        currentIndex = j;
                    }
                }
                swap(array, i, currentIndex);
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (array == null || array.length < 2) {
                return;
            }
            process(array, 0, k - 1);
        }

        public static void process(int[] arr, int left, int right) {
            if (left == right) {
                return;
            }
            int mid = left +((right - left) >> 1);
            process(arr, left ,mid);
            process(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }

        public static void merge(int[] arr, int left, int mid, int right) {
            int[] help = new int[right - left + 1];
            int p1 = left;
            int p2 = mid + 1;
            int i = 0;
            while (p1 <= mid && p2 <= right) {
                help[i++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];
            }
            while (p1 <= mid) {
                help[i++] = arr[p1++];
            }
            while (p2 <= right) {
                help[i++] = arr[p2++];
            }
            if (help.length >= 0) System.arraycopy(help, 0, arr, left + 0, help.length);
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            int[] b = new int[k];
            System.arraycopy(a, 0, b, 0, k);
            int max = b[0];
            for (int i = 1; i < k; i++) {
                if (max < b[i])
                    max = b[i];
            }
            double digit = Math.pow(10, String.valueOf(max).length());
            int currentDigit = 1;
            int[][] bucket = new int[10][k];
            int[] num = new int[10];
            while (currentDigit < digit) {
                for (int i : b) {
                    int m = (i / currentDigit) % 10;
                    bucket[m][num[m]] = i;
                    num[m]++;
                }
                int c = 0;
                for (int i = 0; i < 10; i++) {
                    if (num[i] != 0) {
                        for (int j = 0; j < num[i]; j++) {
                            b[c++] = bucket[i][j];
                        }
                    }
                    num[i] = 0;
                }
                currentDigit = currentDigit * 10;
            }
            System.arraycopy(b, 0, a, 0, k);
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
