import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Ruize Xu
 */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */
    public TrReader(Reader str, String from, String to) {
        _str = str;
        _from = from;
        _to = to;
    }

    @Override
    public int read(char buf[]) throws IOException {
        return read(buf, 0, buf.length);
    }

    @Override
    public int read(char[] buf, int offset, int len) throws IOException {
        int temp = _str.read(buf, offset, len);
        for(int i = 0;i<buf.length;i++){
            if(_from.indexOf(buf[i])!=-1){
                buf[i] = _to.charAt(_from.indexOf(buf[i]));
            }
        }
        return temp;
    }

    @Override
    public void close() throws IOException {
        _str.close();
    }

    private Reader _str;
    private String _from;
    private String _to;
}
