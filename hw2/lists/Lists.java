package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

import image.In;

/** List problem.
 *  @author
 */
class Lists {

    /* B. */
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */

    static IntListList naturalRuns(IntList L) {
        IntListList result = new IntListList();
        IntListList result_begin = result;
        IntList s_list = L;
        IntList temp = L;
        int count =  0;
        int is_ascend = 0;
        while (s_list != null) {
            temp = s_list;
            while (temp != null) {
                if (temp.tail != null) {
                    if (temp.head < temp.tail.head) {
                        is_ascend = 1;  //show that the sublist is ascending
                        count++;
                    } else {
                        is_ascend = 0;
                        count++;
                        break;
                    }
                } else {
                    if (is_ascend == 1) {
                        count++;    // the last element in the list is still ascending
                        break;
                    } else {
                        count = 1;
                        break;
                    }
                }
                temp = temp.tail;
            }
            int[] temp_arr = new int[count]; //use to store one ascending list in the loop
            for (int i = 0; i < count ; i++) {
                temp_arr[i] = s_list.head;
                s_list = s_list.tail;
            }
            count = 0; // for every loop reset count
            IntList transfer = IntList.list(temp_arr); //transfer the array to IntList, which fits in the IntListList.head
            if (s_list != null) {
                IntListList list_shell = new IntListList();
                result.head = transfer;
                result.tail = list_shell;
                result = result.tail;
            } else {
                result.head = transfer;
                result.tail = null;
            }
        }
        return result_begin;
    }
}
