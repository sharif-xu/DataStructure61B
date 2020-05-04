package gitlet;

import com.sun.tools.corba.se.idl.Util;

import java.io.File;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Blob implements Serializable {
    /** Name of the modified file. */
    private String _name;

    /** HashID of the blob object. */
    private String _hashID;

    /** Store the content which read from file using the
     * Util.readContents() method. */
    private byte[] _contents;

    private String _contentsAsString;

    private String _timeStamp;

    public Blob(String name) {
        File file = new File(name);
        _name = name;
        _contents = Utils.readContents(file);
        _contentsAsString = Utils.readContentsAsString(file);
        ZonedDateTime now = java.time.ZonedDateTime.now();
        _timeStamp = now.format(DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss yyyy xxxx", Locale.ENGLISH));
        _hashID = createHashId();
    }

    private String createHashId() {
        String contentToString = Arrays.toString(_contents);
        String insideBlob = _name + contentToString;
        return Utils.sha1(insideBlob);
    }

    public String getName() {
        return _name;
    }

    public String getHashID() {
        return _hashID;
    }

    public byte[] getContents() {
        return _contents;
    }

    public String getContentsAsString() {
        return _contentsAsString;
    }
}
