package gitlet;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Commit implements Serializable {

    private String _message;

    private String _timestamp;

    private String _branch;

    private String _uid;

    /** An array of Hashes of parents. */
    private String[] _parentid;


    /**
     * A list of strings of hashes of Blobs that are being tracked.
     */

    private LinkedHashMap<String, Blob> _blobs = new LinkedHashMap<>();

    /**
     * Commit constructor called by init command
     */
    public Commit(String message){
        _message = message;
        _timestamp = "00:00:00 UTC, Thursday, 1 January 1970";
        _uid = generateHash();
    }

    public Commit(String message, String[] parentid, String branch, LinkedHashMap<String, Blob> blobs) {
        _message = message;
        _branch = branch;
        _parentid = parentid;
        _blobs = blobs;
        ZonedDateTime now = ZonedDateTime.now();
        _timestamp = now.format(DateTimeFormatter.ofPattern("HH:mm:ss 'UTC' xxxx, EE, dd MMMM yyyy"));
        _uid = generateHash();
    }


    /**
     * hash generator returns of initial HASH.
     **/
    private String generateHash() {
        List<Object> initial = new ArrayList<>();
        initial.add(_message);
        initial.add(_timestamp);
        initial.add(_branch);
        initial.add(_blobs);
        return Utils.sha1(initial);
    }
}
