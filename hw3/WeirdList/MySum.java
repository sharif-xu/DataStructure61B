/** An help class to implement the Interface
 * IntUnaryFunction apply to sum up
 *  @author Ruize Xu
 */
public class MySum implements IntUnaryFunction{
    private int sum;

    public MySum() {
        sum = 0;
    }

    @Override
    public int apply(int _head) {
        sum = sum + _head;
        return _head;
    }

    public int getSum() {
        return sum;
    }
}
