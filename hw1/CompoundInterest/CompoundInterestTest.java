import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.

        assertEquals(0, 0); */
        assertEquals(1,CompoundInterest.numYears(2021));
        assertEquals(2,CompoundInterest.numYears(2022));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(10, CompoundInterest.futureValue(10, 11, 2020), tolerance);
        assertEquals(12.544, CompoundInterest.futureValue(10,12,2022), tolerance);

    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals( 10, CompoundInterest.futureValueReal(10, 11, 2020, 2),tolerance);
        assertEquals( 10.864,CompoundInterest.futureValueReal(10, 12, 2021, 3),  tolerance);
    }



    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals( 1000, CompoundInterest.totalSavings(1000, 2020, 10), tolerance);
        assertEquals( 10500.0, CompoundInterest.totalSavings(5000, 2021, 10), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals( 10185.0, CompoundInterest.totalSavingsReal(5000, 2021, 10, 3), tolerance);
        assertEquals( 1000, CompoundInterest.totalSavingsReal(1000, 2020, 20, 5), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
