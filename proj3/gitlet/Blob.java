package gitlet;

import com.sun.tools.corba.se.idl.Util;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Blob implements Serializable {
    /** Name of the modified file. */
    private String _name;

    /** HashID of the blob object. */
    private String _hashID;

    /** Store the content which read from file using the
     * Util.readContents() method. */
    private byte[] _contents;

    public Blob(String name) {
        File file = new File(name);
        _name = name;
        _contents = Utils.readContents(file);
        _hashID = createHashId();
    }

    private String createHashId() {
        String contentToString = Arrays.toString(_contents);
        String insideBlob = _name + contentToString;
        return Utils.sha1(contentToString);
    }

    public String get_name() {
        return _name;
    }

    public String get_hashID() {
        return _hashID;
    }

    public byte[] get_contents() {
        return _contents;
    }
}
