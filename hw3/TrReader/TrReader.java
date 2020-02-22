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
    public int read(char[] cbuf, int off, int len) throws IOException{
        int temp = _str.read(cbuf, off, len);
        for (int i = off; i < temp; i++) {
            int index = this._from.indexOf(cbuf[i]);
            if ( index >= 0) {
                cbuf[i] = _to.charAt(index);
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
