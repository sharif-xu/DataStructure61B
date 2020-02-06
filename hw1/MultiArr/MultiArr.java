/** Multidimensional array 
 *  @author Zoe Plaxco
 */

public class MultiArr {

    /**
    {{“hello”,"you",”world”} ,{“how”,”are”,”you”}} prints:
    Rows: 2
    Columns: 3
    
    {{1,3,4},{1},{5,6,7,8},{7,9}} prints:
    Rows: 4
    Columns: 4
    */
    public static void printRowAndCol(int[][] arr) {
        //TODO: Your code here!
        int row = arr.length;
        int col_max = 0;
        for (int i = 0; i < row; i++) {
            if (arr[i].length > col_max) {
                col_max = arr[i].length;
            }
        }
        System.out.println("Rows: " + row);
        System.out.println("Columns: " + col_max);
    } 

    /**
    @param arr: 2d array
    @return maximal value present anywhere in the 2d array
    */
    public static int maxValue(int[][] arr) {
        //TODO: Your code here!
        int max_value = -10000;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                max_value = Math.max(max_value, arr[i][j]);
            }
        }
        return max_value;
    }

    /**Return an array where each element is the sum of the 
    corresponding row of the 2d array*/
    public static int[] allRowSums(int[][] arr) {
        //TODO: Your code here!!
        int[] result = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                result[i] += arr[i][j];
            }
        }
        return result;
    }
}