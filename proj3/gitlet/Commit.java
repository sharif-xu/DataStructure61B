package gitlet;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Commit implements Serializable {

    private String _message;

    private String _timestamp;

    private String _branch;

    private String _uid;

    /**
     * A list of strings of hashes of Blobs that are being tracked.
     * Key is Blob's hashID, and value is the corresponding Blob object. 
     */

    private HashMap<String, Blob> _blobs = new HashMap<>();

    /** An array of Hashes of parents. */
    private String[] _parentid;

    public String getMessage() {
        return _message;
    }

    public String getTimestamp() {
        return _timestamp;
    }

    public String getBranchName() {
        return _branch;
    }

    public String getUid() {
        return _uid;
    }

    public String[] getParentid() {
        return _parentid;
    }

    public HashMap<String, Blob> getBlobs() {
        return _blobs;
    }

    public Commit() {

    }

    /**
     * Commit constructor called by init command
     */
    public Commit(String message){
        _message = message;
        _timestamp = "Thu Jan 1 00:00:00 1970 +0000";
        _uid = generateHash();
        _branch = "master";
        _blobs = null;
        _parentid = null;

    }

    public Commit(String message, String[] parentid, String branch, HashMap<String, Blob> blobs) {
        _message = message;
        _branch = branch;
        _parentid = parentid;
        _blobs = blobs;
        ZonedDateTime now = ZonedDateTime.now();
        _timestamp = now.format(DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss yyyy xxxx", Locale.ENGLISH));
        _uid = generateHash();
    }

    /**
     * hash generator returns of HASH.
     **/
    private String generateHash() {
        String blobToString;
        String parentToString = Arrays.toString(_parentid);
        if (_blobs == null){
            blobToString = "";
        } else {
            blobToString = _blobs.toString();
        }
        String contentOfHash = _message + _timestamp + _branch + parentToString + blobToString;

        return Utils.sha1(contentOfHash);
    }

    public String getParentID() {
        if (_parentid != null) {
            return _parentid[0];
        }
        return null;
    }

    public String[] getAllParentID() {
        return _parentid;
    }
}
