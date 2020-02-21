/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        _colNum = input.colNameToIndex(colName);
        this._match = match;
    }

    @Override
    protected boolean keep() {
        return _next.getValue(_colNum).contentEquals(_match);
    }

    private String _match;
    private boolean _isEqual;
    private int _colNum;
}
