package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testInvertChar() {
        String input = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        Permutation p = new Permutation(input, new Alphabet());
        assertEquals('U', p.invert('A'));
        assertEquals('W', p.invert('B'));
        assertEquals('Y', p.invert('C'));

    }
    @Test
    public void testInvertInt() {
        String input = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        Permutation p = new Permutation(input, new Alphabet());
        assertEquals(20, p.invert(0));
        assertEquals(22, p.invert(1));
        assertEquals(24, p.invert(2));
    }

    @Test
    public void testPermuteInt() {
        String input = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        Permutation p = new Permutation(input, new Alphabet());
        assertEquals(4, p.permute(0));
        assertEquals(10, p.permute(1));
        assertEquals(9, p.permute(25));
        assertEquals(3, p.permute(6));
    }

    @Test
    public void testPermuteChar() {
        String input = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        Permutation p = new Permutation(input, new Alphabet());
        assertEquals('E', p.permute('A'));
        assertEquals('K', p.permute('B'));
        assertEquals('C', p.permute('Y'));
        assertEquals('D', p.permute('G'));
    }
    @Test
    public void testModuleOverBound() {
        String input = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        Permutation p = new Permutation(input, new Alphabet());
        assertEquals(4, p.permute(26));
        assertEquals(22, p.invert(27));
    }
    @Test
    public void testOnlyOne() {
        Permutation p = new Permutation("(A)", new Alphabet());
        assertEquals('A', p.invert('A'));
        assertEquals(0, p.invert(0));
        assertEquals('A', p.permute('A'));
        assertEquals(0, p.permute(0));
    }
    @Test
    public void testExtra1() {
        Permutation p = new Permutation("(DAG) (CS) (O)",
                new Alphabet("ACSGOD"));
        assertEquals('A', p.invert('G'));
        assertEquals(1, p.invert(2));
    }
    @Test
    public  void testExtra2() {
        Permutation p = new Permutation("(BACD)", new Alphabet("DBCA"));
        assertEquals(1, p.permute(0));
        assertEquals(3, p.permute(9));
    }
}
