import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author Ruize Xu
 */
class ECHashStringSet implements StringSet {

    ECHashStringSet() {
        for (int i = 0; i < _bucket; i++) {
            _temp[i] = new LinkedList<String>();
        }
    }

    @Override
    public void put(String s) {
        if (!this.contains(s)) {
            int _loadFactor = 5;
            if (_variable / _bucket == _loadFactor) {
                resizeHashSet();
            }
            int hashint = s.hashCode() & 0x7fffffff % _bucket;
            LinkedList<String> node = _temp[hashint];
            node.add(s);
            _variable++;
        }
    }

    @Override
    public boolean contains(String s) {
        int hash = s.hashCode() & 0x7fffffff % _bucket;
        LinkedList<String> node = _temp[hash];
        return node.contains(s);
    }

    @Override
    public List<String> asList() {
        List<String> temp = new ArrayList<String>();
        for (LinkedList<String> node : _temp) {
            temp.addAll(node);
        }
        return temp;

    }

    public void resizeHashSet() {
        _bucket = _bucket * 2;
        LinkedList<String>[] newArr = new LinkedList[_bucket];
        for (int i = 0; i < _bucket; i++) {
            newArr[i] = new LinkedList<String>();
        }
        for (LinkedList<String> node : _temp) {
            for (String tmp : node) {
                int hashint = tmp.hashCode() & 0x7fffffff % _bucket;
                LinkedList<String> newNode = newArr[hashint];
                newNode.add(tmp);
            }
        }
        _temp = newArr;
    }

    private int _bucket = 4;

    private LinkedList<String>[] _temp = new LinkedList[_bucket];

    private int _variable = 0;

}
