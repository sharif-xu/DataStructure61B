/** An help class to implement the Interface
 * IntUnaryFunction apply to do the adding operation
 *  @author Ruize Xu
 */
public class MyAdd implements IntUnaryFunction {
    private int _input;

    public MyAdd(int input) {
        _input = input;
    }

    @Override
    public int apply(int _head) {
        _head = _head + _input;
        return _head;
    }

}
