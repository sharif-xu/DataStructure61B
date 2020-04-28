package gitlet;

import com.sun.tools.corba.se.idl.Util;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Repo implements Serializable {

    private String _currentbranch;

    private String _currentpath;

    private String _command;

    private String _parentid;

    private ArrayList<String> _commitkey;

    private ArrayList<String> _operand;

    /** Overseer of entire tree structure, each branch has a name (String)
     * and a hash ID of its current position so that we can find the commit
     * that it's pointing to.*/
    private HashMap<String, String> _branches;

    /** The head pointer that corresponds to the branch that actually will be
     * pointing at the commit that we want . */
    private String _head;

    /** Staging Area, maps the name of the file, useful for figuring out
     * whether we need to swap it out for existing file in commit, or add
     * it entirely new. */
    private HashMap<String, Blob> _stagingArea;

    /** Untracked files are like the opposite of the Staging Area,
     * these are files that WERE tracked before, and now, for the
     * next commit, they're not going to be added. */
    private ArrayList<String> _untrackedFiles;

    /** Overseer of entire tree structure, each branch has a name (String)
     * and a hash ID of its current position so that we can find the commit
     * that it's pointing to.*/
    private LinkedHashMap<String, Commit> _commit;

    private LinkedHashMap<String, Blob> _staging;

    public String get_currentbranch() {
        return _currentbranch;
    }

    public String get_currentpath() {
        return _currentpath;
    }

    public String get_command() {
        return _command;
    }

    public String get_parentid() {
        return _parentid;
    }

    public ArrayList<String> get_commitkey() {
        return _commitkey;
    }

    public ArrayList<String> get_operand() {
        return _operand;
    }

    public HashMap<String, String> get_branches() {
        return _branches;
    }

    public String getHead() {
        return _branches.get(_head);
    }

    public HashMap<String, Blob> get_stagingArea() {
        return _stagingArea;
    }

    public ArrayList<String> get_untrackedFiles() {
        return _untrackedFiles;
    }

    public LinkedHashMap<String, Commit> get_commit() {
        return _commit;
    }

    public LinkedHashMap<String, Blob> get_staging() {
        return _staging;
    }

    public Repo() {
        if (!Files.exists(Paths.get(".gitlet"))) {
            Commit initial = new Commit("initial commit");
            File gitlet = new File(".gitlet");
            gitlet.mkdir();
            File commits = new File(".gitlet/commits");
            commits.mkdir();
            File staging = new File(".gitlet/staging");
            staging.mkdir();
            String id = initial.get_uid();
            File initialFile = new File(".gitlet/commits/" + id);
            Utils.writeContents(initialFile, Utils.serialize(initial));
            _head = "master";
            _branches = new HashMap<String, String>();
            _branches.put("master", initial.get_uid());
            _stagingArea = new HashMap<String, Blob>();
            _untrackedFiles = new ArrayList<String>();
        } else {
            System.out.println("A Gitlet version-control "
                    + "system already exists in the "
                    + "current directory");
        }
    }

    public void init() throws IOException {
        if (!Files.exists(Paths.get(".gitlet"))) {
            Commit initial = new Commit("initial commit");
            File gitlet = new File(".gitlet");
            gitlet.mkdir();
            File commits = new File(".gitlet/commits");
            commits.mkdir();
            File staging = new File(".gitlet/staging");
            staging.mkdir();
            String id = initial.get_uid();
            File initialFile = new File(".gitlet/commits/" + id);
            Utils.writeContents(initialFile, Utils.serialize(initial));
            _head = "master";
            _branches = new HashMap<String, String>();
            _branches.put("master", initial.get_uid());
            _stagingArea = new HashMap<String, Blob>();
            _untrackedFiles = new ArrayList<String>();
        } else {
            System.out.println("A Gitlet version-control "
                    + "system already exists in the "
                    + "current directory");
        }
    }

    public void add(String filename) {
        if (!new File(filename).exists()) {
            Utils.message("File does not exist.");
            throw new GitletException();
        }
        Blob blob = new Blob(filename);
        String blobHashID = blob.get_hashID();
        Commit mostRecent = uidToCommit(getHead());
        HashMap<String, Blob> files = mostRecent.get_blobs();
        File stagingblob = new File(".gitlet/staging/" + blobHashID);
        if (stagingblob.exists()) {
            _stagingArea.remove(filename);
        } else {
            _stagingArea.put(blobHashID, blob);
            String contents = Utils.readContentsAsString(new File(filename));
            Utils.writeContents(stagingblob, contents);
        }
    }

    public void commit(String msg) {
        if (msg.trim().equals("")) {
            Utils.message("Please enter a commit message.");
            throw new GitletException();
        }
        Commit mostRecent = uidToCommit(getHead());
        HashMap<String, Blob> trackedFiles = mostRecent.get_blobs();
        if (trackedFiles == null) {
            trackedFiles = new HashMap<String, Blob>();
        }
        if (_stagingArea.size() != 0 || _untrackedFiles.size() != 0) {
            for (String fileName : _stagingArea.keySet()) {

                trackedFiles.put(fileName, _stagingArea.get(fileName));
            }
            for (String fileName : _untrackedFiles) {
                trackedFiles.remove(fileName);
            }
        } else {
            Utils.message("No changes added to the commit.");
            throw new GitletException();
        }
        String[] parent = new String[]{mostRecent.get_uid()};
        String branch = mostRecent.get_branch();
        Commit newCommit = new Commit(msg, parent, branch, trackedFiles);
        String s = newCommit.get_uid();
        File newCommFile = new File(".gitlet/commits/" + s);
        Utils.writeObject(newCommFile, newCommit);

        _stagingArea = new HashMap<String, Blob>();
        _untrackedFiles = new ArrayList<String>();
        _branches.put(_head, newCommit.get_uid());
    }


    public void log() {
        String head = getHead();
        while (head != null) {
            Commit first = uidToCommit(head);
            if (first.get_parentid() != null && first.get_parentid().length > 1) {
                System.out.println("===");
                System.out.println("commit " + head);
                String short1 = first.get_parentid()[0].substring(0, 7);
                String short2 = first.get_parentid()[1].substring(0, 7);
                System.out.println("Merge: " + short1 + " " + short2);
                System.out.println("Date: " + first.get_timestamp());
                System.out.println(first.get_message());
                System.out.println();
            } else {
                System.out.println("===");
                System.out.println("commit " + head);
                System.out.println("Date: " + first.get_timestamp());
                System.out.println(first.get_message());
                System.out.println();
            }
            head = first.getParentID();
        }
    }

    public void status() {

    }

    /*********************** CHECKOUT ****************************/

    /** Takes in a Arraylist<String> ARGS.
     */
    public void checkout(ArrayList<String> args) {
        String commID;
        String fileName;
        if (args.size() == 2 && args.get(0).equals("--")) {
            fileName = args.get(1);
            commID = getHead();
        } else if (args.size() == 3 && args.get(1).equals("--")) {
            commID = args.get(0);
            fileName = args.get(2);
        } else {
            Utils.message("Incorrect operands");
            throw new GitletException();
        }
        commID = convertShortenedID(commID);
        Commit comm = uidToCommit(commID);
        HashMap<String, Blob> trackedFiles = comm.get_blobs();
        boolean find = false;
        for (Blob blob : trackedFiles.values())
            if (blob.get_name().equals(fileName)){
                File f = new File(fileName);
                String p = ".gitlet/staging/";
                String blobFileName = p + blob.get_hashID();
                File g = new File(blobFileName);
                String contents = Utils.readContentsAsString(g);
                Utils.writeContents(f, contents);
                find = true;
            }
        if (!find) {
            Utils.message("File does not exist in that commit.");
            throw new GitletException();
        }
    }


    /** Takes in a shortened String ID and returns a String
     * of the full length ID. */
    private String convertShortenedID(String id) {
        if (id.length() == Utils.UID_LENGTH) {
            return id;
        }
        File commitFolder = new File(".gitlet/commits");
        File[] commits = commitFolder.listFiles();

        for (File file : commits) {
            if (file.getName().contains(id)) {
                return file.getName();
            }
        }
        Utils.message("No commit with that id exists.");
        throw new GitletException();
    }

    /** This is the third use case for checkout.
     * It takes in a BRANCHNAME. */
    public void checkout(String branchName) {
        if (!_branches.containsKey(branchName)) {
            Utils.message("No such branch exists.");
            throw new GitletException();
        }
        if (_head.equals(branchName)) {
            String s = "No need to checkout the current branch.";
            Utils.message(s);
            throw new GitletException();
        }
        String commID = _branches.get(branchName);
        Commit comm = uidToCommit(commID);
        HashMap<String, Blob> files = comm.get_blobs();
        String pwdString = System.getProperty("user.dir");
        File pwd = new File(pwdString);
        checkForUntracked(pwd);
        for (File file : pwd.listFiles()) {
            if (files == null) {
                Utils.restrictedDelete(file);
            } else {
                boolean b = !files.containsKey(file.getName());
                if (b && !file.getName().equals(".gitlet")) {
                    Utils.restrictedDelete(file);
                }
            }
        }
        if (files != null) {
            for (String file : files.keySet()) {
                String g = ".gitlet/staging/" + files.get(file);
                File f = new File(g);
                String contents = Utils.readContentsAsString(f);
                Utils.writeContents(new File(file), contents);
            }
        }
        _stagingArea = new HashMap<String, Blob>();
        _untrackedFiles = new ArrayList<String>();
        _head = branchName;

    }



    /** This function takes in the present working directory
     * PWD and will determine if there are untracked files
     * that mean that this checkout or Merge operation can't
     * continue. */
    private void checkForUntracked(File pwd) {
        String s;
        s = "There is an untracked file in the way; ";
        s += "delete it or add it first.";
        Commit mostRecent = uidToCommit(getHead());
        HashMap<String, Blob> trackedFiles = mostRecent.get_blobs();
        for (File file : pwd.listFiles()) {
            if (trackedFiles == null) {
                if (pwd.listFiles().length > 1) {
                    Utils.message(s);
                    throw new GitletException();
                }
            } else {
                boolean b = !trackedFiles.containsKey(file.getName());
                boolean c = !_stagingArea.containsKey(file.getName());
                if (b && !file.getName().equals(".gitlet") && c) {
                    Utils.message(s);
                    throw new GitletException();
                }
            }
        }
    }

    /** This is how we are going to be capable of returns back and forth
     * in between each hash and the corresponding commit. Takes in a
     * String UID, and returns the commit object that corresponds
     * to that UID. */
    public Commit  uidToCommit(String uid) {
        File f = new File(".gitlet/commits/" + uid);
        if (f.exists()) {
            return Utils.readObject(f, Commit.class);
        } else {
            Utils.message("No commit with that id exists.");
            throw new GitletException();
        }
    }
}
