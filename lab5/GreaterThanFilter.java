import java.awt.color.ICC_ProfileRGB;

/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        _colNum = input.colNameToIndex(colName);
        this._ref = ref;
    }

    @Override
    protected boolean keep() {
       return _next.getValue(_colNum).compareTo(_ref) > 0;
    }

    private int _colNum;
    private String _ref;
}
