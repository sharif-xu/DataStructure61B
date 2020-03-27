import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Ruize Xu
 */
public class ECHashStringSetTest  {
    // FIXME: Add your own tests for your ECHashStringSetTest

    @Test
    public void test() {
        String[] s = new String[]{"ejzb", "ofbf", "byps", "pckk", "pckk", "jjvf", "gsvm", "fzlb", "diiw", "xzdd", "dymb"};
        String[] s1 = new String[]{"jjvf", "ejzb", "byps", "fzlb", "xzdd", "dymb", "ofbf", "pckk", "gsvm", "diiw"};
        ECHashStringSet echash = new ECHashStringSet();
        List<String> expected = new ArrayList<String>(Arrays.asList(s1));
        for (String value : s) {
            echash.put(value);
        }
        assertTrue(echash.contains("ejzb"));
        assertFalse(echash.contains("aaaa"));
        assertEquals(expected, echash.asList());
    }

}
