/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        this._colNum1 = input.colNameToIndex(colName1);
        this._colNum2 = input.colNameToIndex(colName2);
    }

    @Override
    protected boolean keep() {
        return _next.getValue(_colNum1).equals(_next.getValue(_colNum2));
    }

    private int _colNum1;
    private int _colNum2;

}
