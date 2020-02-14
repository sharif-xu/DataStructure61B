package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        int len = A.length + B.length;
        int[] C = new int[len];
        for (int i = 0; i < A.length ; i++) {
            C[i] = A[i];
        }
        for (int i = 0; i < B.length; i++) {
            C[i+A.length] = B[i];
        }
        return C;
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        int arrLen = A.length-len;
        int[] removedArr = new int[arrLen];
        int[] arrayBefore = new int[start];
        int[] arrAfter = new int[A.length-start-len];
        System.arraycopy(A, 0, arrayBefore, 0, start);
        for (int i = 0; i < arrAfter.length ; i++) {
            arrAfter[i] = A[i+start+len];
        }
        removedArr = catenate(arrayBefore,arrAfter);
        return removedArr;
    }

    /* C3. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        int i = 0;
        int count = 1;
        int seg=0;
        int[] countArr = new int[10]; // max to ten different ascending lists, can be change to any possible value.

        while (i < A.length-1) {
            if (i != A.length - 2) {
                if (A[i] < A[i + 1]) {
                    count++;
                } else {
                    countArr[seg] = count;
                    count = 1;
                    seg++;
                }
            } else {
                if (A[i] < A[i + 1]) {
                    count++;
                    countArr[seg] = count;
                } else {
                    countArr[seg] = count;
                    seg++;
                    countArr[seg] = 1;
                    seg++;
                }
            }
            i++;
        }

        int start = 0;
        int[][] result = new int[seg][];
        for (int j = 0; j < seg; j++) {
            result[j] = new int[countArr[j]];
            for (int k = 0; k < countArr[j]; k++) {
                result[j][k] = A[start];
                start++;
            }
        }

        return result;
    }
}
