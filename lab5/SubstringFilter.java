/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        _colNum = input.colNameToIndex(colName);
        _subStr = subStr;
    }

    @Override
    protected boolean keep() {
       return _next.getValue(_colNum).contains(_subStr);
    }
    private int _colNum;
    private String _subStr;
}
