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
