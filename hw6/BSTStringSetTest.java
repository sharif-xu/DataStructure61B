import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Ruize Xu
 */
public class BSTStringSetTest {
    // FIXME: Add your own tests for your BST StringSet

    @Test
    public void test1() {
        String[] s = new String[]{"c", "a", "d", "b", "f", "h", "g"};
        String[] s1 = new String[]{"a", "b", "c", "d", "f", "g", "h"};
        List<String> ls = new ArrayList<String>(Arrays.asList(s1));
        BSTStringSet bst = new BSTStringSet();
        for (String value : s) {
            bst.put(value);
        }
        assertTrue(bst.contains("b"));
        assertFalse(bst.contains("e"));
        assertEquals(ls, bst.asList());
        bst.put("e");
        assertTrue(bst.contains("e"));
    }
}
