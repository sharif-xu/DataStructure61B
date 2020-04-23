package gitlet;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class Commit implements Serializable {

    private String _message;

    private String _timestamp;

    private String _branch;

    private String _uid;

    /**
     * Commit constructor called by init command
     */
    Commit(String message){
        _message = message;
        _timestamp = 
    }
}
