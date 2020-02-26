
public class IsEmpty extends WeirdList{

    public IsEmpty() {
        super(0, null);
    }
    @Override
    public int length() {
        return 0;
    }
    @Override
    public String toString() {
        return "";
    }
    @Override
    public WeirdList map(IntUnaryFunction func) {
        return this;
    }

}
