package gitlet;

import org.checkerframework.checker.units.qual.C;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Repo implements Serializable {

    /** The structure is used to store the head of each branch,
     *  key for branchName, and corresponding value is uid of the
     *  exact commit of this branch.
     */
    private HashMap<String, String> _branches;

    /** The head pointer that corresponds to the branch that actually will be
     * pointing at the commit that we want . */
    private String _head;

    /** Staging area used to store the blob object, key is file name,
     *  value is the Blob object, help us identify whether the file is
     *  changed.
     */
    private HashMap<String, Blob> _stagingArea;

    /** Untracked files are like the opposite of the Staging Area,
     * these are files that WERE tracked before, and now, for the
     * next commit, they're not going to be added. */
    private ArrayList<String> _untrackedFiles;

    /** Overseer of entire tree structure, each branch has a name (String)
     * and a hash ID of its current position so that we can find the commit
     * that it's pointing to.*/
    private LinkedHashMap<String, Commit> _commit;

    public HashMap<Commit, Integer> branch1Commits = new HashMap<Commit, Integer>();
    public HashMap<Commit, Integer> branch2Commits = new HashMap<Commit, Integer>();

    private final File pwd = new File(System.getProperty("user.dir"));

    private final String pwdString = System.getProperty("user.dir");




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



    public Repo() {
        if (!Files.exists(Paths.get(".gitlet"))) {
            Commit initial = new Commit("initial commit");
            File gitlet = new File(".gitlet");
            gitlet.mkdir();
            File commits = new File(".gitlet/commits");
            commits.mkdir();
            File staging = new File(".gitlet/staging");
            staging.mkdir();
            String id = initial.getUid();
            File initialFile = new File(".gitlet/commits/" + id);
            Utils.writeContents(initialFile, Utils.serialize(initial));
            _head = "master";
            _branches = new HashMap<String, String>();
            _branches.put("master", initial.getUid());
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
        _untrackedFiles.remove(filename);
        Blob blob = new Blob(filename);
        String blobHashID = blob.getHashID();
        Commit lastCommit = uidToCommit(getHead());
        HashMap<String, Blob> files = lastCommit.getBlobs();
        File stagingblob = new File(".gitlet/staging/" + blobHashID);
        boolean alreadyAdded = false;
        boolean emptyBlobs = false;
        if (files == null) {
            emptyBlobs = true;
        } else {
            for (Blob temp : files.values()) {
                if (temp.getHashID().equals(blobHashID)) {
                    alreadyAdded = true;
                    break;
                }
            }
        }
        if (!alreadyAdded || emptyBlobs) {
            _stagingArea.put(filename, blob);
            String contents = Utils.readContentsAsString(new File(filename));
            Utils.writeContents(stagingblob, contents);
        } else {
           if (stagingblob.exists()) {
               _stagingArea.remove(filename);
           }
       }
    }

    public void commit(String msg) {
        if (msg.trim().equals("")) {
            Utils.message("Please enter a commit message.");
            throw new GitletException();
        }
        Commit lastCommit = uidToCommit(getHead());
        HashMap<String, Blob> trackedFiles = lastCommit.getBlobs();
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
        String[] parent = new String[]{lastCommit.getUid()};
        String branch = _head;
        Commit newCommit = new Commit(msg, parent, branch, trackedFiles);
        String s = newCommit.getUid();
        File newCommFile = new File(".gitlet/commits/" + s);
        Utils.writeObject(newCommFile, newCommit);

        _stagingArea = new HashMap<String, Blob>();
        _untrackedFiles = new ArrayList<String>();
        _branches.put(_head, newCommit.getUid());
    }

    public void commit(String msg, String[] parents) {
        if (msg.trim().equals("")) {
            Utils.message("Please enter a commit message.");
            throw new GitletException();
        }
        Commit lastCommit = uidToCommit(getHead());
        HashMap<String, Blob> trackedFiles = lastCommit.getBlobs();
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
        String branch = lastCommit.getBranchName();
        Commit newCommit = new Commit(msg, parents, branch, trackedFiles);
        String s = newCommit.getUid();
        File newCommFile = new File(".gitlet/commits/" + s);
        Utils.writeObject(newCommFile, newCommit);

        _stagingArea = new HashMap<String, Blob>();
        _untrackedFiles = new ArrayList<String>();
        _branches.put(_head, newCommit.getUid());
    }


    public void log() {
        String head = getHead();
        while (head != null) {
            Commit first = uidToCommit(head);
            if (first.getParentid() != null && first.getParentid().length > 1) {
                System.out.println("===");
                System.out.println("commit " + head);
                String short1 = first.getParentid()[0].substring(0, 7);
                String short2 = first.getParentid()[1].substring(0, 7);
                System.out.println("Merge: " + short1 + " " + short2);
                System.out.println("Date: " + first.getTimestamp());
                System.out.println(first.getMessage());
                System.out.println();
            } else {
                System.out.println("===");
                System.out.println("commit " + head);
                System.out.println("Date: " + first.getTimestamp());
                System.out.println(first.getMessage());
                System.out.println();
            }
            head = first.getParentID();
        }
    }

    public void globalLog() {
        File commitDir = new File(".gitlet/commits");
        for (File commitFile : Objects.requireNonNull(commitDir.listFiles())) {
            String uid = commitFile.getName();
            Commit temp = uidToCommit(uid);
            if (temp.getParentid() != null && temp.getParentid().length > 1) {
                System.out.println("===");
                System.out.println("commit " + uid);
                String short1 = temp.getParentid()[0].substring(0, 7);
                String short2 = temp.getParentid()[1].substring(0, 7);
                System.out.println("Merge: " + short1 + " " + short2);
                System.out.println("Date: " + temp.getTimestamp());
                System.out.println(temp.getMessage());
                System.out.println();
            } else {
                System.out.println("===");
                System.out.println("commit " + uid);
                System.out.println("Date: " + temp.getTimestamp());
                System.out.println(temp.getMessage());
                System.out.println();
            }
        }

    }

    public void status() {
        System.out.println("=== Branches ===");
        Object[] keys = _branches.keySet().toArray();
        Arrays.sort(keys);
        for (Object branch : keys) {
            if (branch.equals(_head)) {
                System.out.println("*" + branch);
            } else {
                System.out.println(branch);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        Object[] stages = _stagingArea.keySet().toArray();
        Arrays.sort(stages);
        for (Object staged : stages) {
            System.out.println(staged);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        Object[] untracks = _untrackedFiles.toArray();
        Arrays.sort(untracks);
        for (Object removed : untracks) {
            System.out.println(removed);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
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
        commID = shortToLong(commID);
        Commit comm = uidToCommit(commID);
        HashMap<String, Blob> trackedFiles = comm.getBlobs();
        boolean find = false;
        for (Blob blob : trackedFiles.values())
            if (blob.getName().equals(fileName)){
                File f = new File(fileName);
                String p = ".gitlet/staging/";
                String blobFileName = p + blob.getHashID();
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
        HashMap<String, Blob> blobs = comm.getBlobs();
        checkForUntracked(pwd);

        for (File file : Objects.requireNonNull(pwd.listFiles())) {
            if (file.getName().equals(".DS_Store") || file.getName().equals(".gitignore") || file.getName().equals("proj3.iml") || file.getName().equals("Makefile")) {
                continue; // FIXME: Delete
            }
            if (file.isDirectory()) {
                continue;
            }
            if (!file.getName().equals(".gitlet")) {
                if (!Utils.restrictedDelete(file)) {
                    Utils.message("Can not delete file" + file.getName());
                    throw new GitletException();
                }
            }
        }
        if (blobs != null) {
            for (Blob blob : blobs.values()) {
                String fileDir = blob.getName();
                String blobhash = blob.getHashID();
                File blobfile = new File(".gitlet/staging/" + blobhash);
                File file = new File(fileDir);
                String contents = Utils.readContentsAsString(blobfile);
                Utils.writeContents(file, contents);
            }
        }
        _stagingArea = new HashMap<String, Blob>();
        _untrackedFiles = new ArrayList<String>();
        _head = branchName;
    }

    public void rm(String fileName) {
        File file = new File(fileName);
        Commit lastCommit = uidToCommit(getHead());
        HashMap<String, Blob> trackedFiles = lastCommit.getBlobs();
        boolean flag = false;
        if (trackedFiles != null) {
            for (String blobHash : trackedFiles.keySet()) {
                Blob temp = trackedFiles.get(blobHash);
                String blobname = temp.getName();
                if (blobname.equals(fileName)) {
                    flag = true;
                    break;
                }
            }
        }
        if (!file.exists() && !flag) {
            Utils.message("File does not exist.");
            throw new GitletException();
        }
        boolean changed = false;
        if (_stagingArea.containsKey(fileName)) {
            _stagingArea.remove(fileName);
            changed = true;
        }
        if (flag) {
            _untrackedFiles.add(fileName);
            File toRemove = new File(fileName);
            Utils.restrictedDelete(toRemove);
            changed = true;
        }
        if (!changed) {
            Utils.message("No reason to remove the file.");
            throw new GitletException();
        }
    }

    public void branch(String branchName) {
        if (!_branches.containsKey(branchName)) {
            _branches.put(branchName, getHead());
        } else {
            Utils.message("A branch with that name already exists.");
            throw new GitletException();
        }
    }

    public void rmbranch(String branchName) {
        if (!_branches.containsKey(branchName)) {
            Utils.message("A branch with that name does not exist.");
            throw new GitletException();
        }
        if (_head.equals(branchName)) {
            Utils.message("Cannot remove the current branch.");
            throw new GitletException();
        }
        else _branches.remove(branchName);
    }

    public void reset(String commitUid) {
        commitUid = shortToLong(commitUid);
        Commit c = uidToCommit(commitUid);
        HashMap<String, Blob> blobs = c.getBlobs();
        checkForUntracked(pwd);
        for (File file : Objects.requireNonNull(pwd.listFiles())) {
            if (!file.isDirectory()) {
                if (file.getName().equals(".gitignore") || file.getName().equals("proj3.iml") || file.getName().equals("Makefile")) {
                    continue; // FIXME: Delete
                }
                String fileName = file.getName();
                boolean find = false;
                for (Blob b : blobs.values()) {
                    if (fileName.equals(b.getName())) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    if (!Utils.restrictedDelete(file)) {
                        Utils.message("Can not delete file" + file.getName());
                        throw new GitletException();
                    }
                }
            }
        }
        for (Blob blob : blobs.values()) {
            String blobhash = blob.getHashID();
            File blobfile = new File(".gitlet/staging/" + blobhash);
            File file = new File(blob.getName());
            String contents = Utils.readContentsAsString(blobfile);
            Utils.writeContents(file, contents);
        }
        _stagingArea = new HashMap<String, Blob>();
        _branches.put(_head, commitUid);
    }

    public void find(String message) {
        File commitDir = new File(".gitlet/commits");
        boolean flag = false;
        for (File commitFile :
                Objects.requireNonNull(commitDir.listFiles())) {
            String fileName = commitFile.getName();
            Commit temp = uidToCommit(fileName);
            if (temp.getMessage().equals(message)) {
                System.out.println(fileName);
                flag = true;
            }
        }
        if (!flag) {
            Utils.message("Found no commit with that message.");
            throw new GitletException();
        }
    }

    public void merge(String branchName) {
        if (!_branches.containsKey(branchName)) {
            Utils.message("A branch with that name does not exist.");
            throw new GitletException();
        }
        if (_stagingArea.size() != 0 || _untrackedFiles.size() != 0) {
            Utils.message("You have uncommitted changes.");
            throw new GitletException();
        }
        if (branchName.equals(_head)) {
            Utils.message("Cannot merge a branch with itself.");
            throw new GitletException();
        }
        String splitCommitHash = splitPoint(_head, branchName);
        if (splitCommitHash.equals(_branches.get(branchName))) {
            Utils.message("Given branch is an ancestor of the current branch.");
            throw new GitletException();
        }
        if (splitCommitHash.equals(_branches.get(_head))) {
            checkout(branchName);
            _branches.put(_head, _branches.get(branchName));
            Utils.message("Current branch fast-forwarded.");
            throw new GitletException();
        }
        checkForUntracked(pwd);
        Commit splitCommit = uidToCommit(splitCommitHash);
        Commit currentHead = uidToCommit(getHead());
        Commit givenHead = uidToCommit(_branches.get(branchName));
        HashMap<String, Blob> splitBlobs = splitCommit.getBlobs();
        HashMap<String, Blob> currentBlobs = currentHead.getBlobs();
        HashMap<String, Blob> givenBlobs = givenHead.getBlobs();

        if (splitBlobs != null) {
            for (Blob blob : splitBlobs.values()) {
                String blobName = blob.getName();
                boolean isModifiedInCurrent = isModified(blobName, splitBlobs, currentBlobs);
                boolean isModifiedInGiven = isModified(blobName, splitBlobs, givenBlobs);
                boolean isModifiedBetween = isModified(blobName, currentBlobs, givenBlobs);
                boolean isInCurrent = false, isInGiven = false;
                for (Blob temp : currentBlobs.values()) {
                    if (temp.getName().equals(blobName)) {
                        isInCurrent = true;
                        break;
                    }
                }
                for (Blob temp : givenBlobs.values()) {
                    if (temp.getName().equals(blobName)) {
                        isInGiven = true;
                        break;
                    }
                }
                if (isInCurrent && !isInGiven) {
                    if (isModifiedInCurrent) {
                        mergeConflict(branchName, blobName);
                    } else {
                        rm(blobName);
                        Utils.restrictedDelete(pwdString + blobName);
                    }
                }
                if (isInCurrent && isInGiven) {
                    if (isModifiedBetween && isModifiedInCurrent && isModifiedInGiven) {
                        mergeConflict(branchName, blobName);
                        break;
                    }
                    if (isModifiedInGiven) {
                        checkoutFileInGiven(branchName, givenBlobs, blobName);
                    }
                }
                if (!isInCurrent && isInGiven) {
                    if (isModifiedInGiven) {
                        mergeConflict(branchName, blobName);
                        break;
                    }
                }

            }
        }

        if (givenBlobs != null) {
            for (Blob blob : givenBlobs.values()) {
                String blobName = blob.getName();
                String blobHash = blob.getHashID();
                boolean isInSplit = false, isInCurrent = false;
                boolean isModifiedBetween = isModified(blobName, currentBlobs, givenBlobs);
                for (Blob temp : currentBlobs.values()) {
                    if (temp.getName().equals(blobName)) {
                        isInCurrent = true;
                        break;
                    }
                }
                if (splitBlobs != null) {
                    for (Blob temp : splitBlobs.values()) {
                        if (temp.getName().equals(blobName)) {
                            isInSplit = true;
                            break;
                        }
                    }
                }
                if (!isInCurrent) {
                    if(!isInSplit) {
                        checkoutFileInGiven(branchName, givenBlobs, blobName);
                    }
                } else {
                    if (!isInSplit) {
                        if (isModifiedBetween) {
                            mergeConflict(branchName, blobName);
                            break;
                        }
                    }
                }

            }
        }
        String[] parents = new String[]{getHead(), _branches.get(branchName)};
        commit("Merged " + branchName + " into " + _head + ".", parents);
    }

    private void checkoutFileInGiven(String branchName, HashMap<String, Blob> givenBlobs, String blobName) {
        ArrayList<String> args = new ArrayList<>();
        args.add(_branches.get(branchName));
        args.add("--");
        args.add(blobName);
        checkout(args);
        _stagingArea.put(blobName, givenBlobs.get(blobName));
    }

    /** Splitting up the merge. Need a BRANCHNAME. */
    private void mainMerge(String branchName) {
        String split = splitPoint(branchName, _head);
        Commit splitCommit = uidToCommit(split);
        Commit currentHead = uidToCommit(getHead());
        Commit givenHead = uidToCommit(_branches.get(branchName));
        HashMap<String, Blob> splitBlobs = splitCommit.getBlobs();
        HashMap<String, Blob> currentBlobs = currentHead.getBlobs();
        HashMap<String, Blob> givenBlobs = givenHead.getBlobs();

        checkForUntracked(pwd);

        for (Blob blob : splitBlobs.values()) {
            String blobName = blob.getName();
            boolean presentInGiven = false;
            for (Blob temp : givenBlobs.values()) {
                if (temp.getName().equals(blobName)) {
                    presentInGiven = true;
                    break;
                }
            }
            boolean isModifiedInCurrent = isModified(blobName, splitBlobs, currentBlobs);
            boolean isModifiedInGiven = isModified(blobName, splitBlobs, givenBlobs);
            if (!isModifiedInCurrent) {
                if (!presentInGiven) {
                    Utils.restrictedDelete(new File(blobName));
                    rm(blobName);
                    continue;
                }
                if (isModifiedInGiven) {
                    ArrayList<String> args = new ArrayList<>();
                    args.add(_branches.get(branchName));
                    args.add("--");
                    args.add(blobName);
                    checkout(args);
                    add(blobName);
                }
            }
            if (isModifiedInCurrent && isModifiedInGiven) {
                if (isModified(blobName, givenBlobs, currentBlobs)) {
                    mergeConflict(branchName, blobName);
                }
            }
        }
    }

    /** This has a BRANCHNAME and a FILENAME. */
    private void mergeConflict(String branchName, String fileName) {
        Commit splitCommit = uidToCommit(splitPoint(branchName, _head));
        Commit currentHead = uidToCommit(getHead());
        Commit givenHead = uidToCommit(_branches.get(branchName));
        HashMap<String, Blob> splitBlobs = splitCommit.getBlobs();
        HashMap<String, Blob> currentBlobs = currentHead.getBlobs();
        HashMap<String, Blob> givenBlobs = givenHead.getBlobs();
        String cContents = "";
        for (Blob blob : currentBlobs.values()) {
            if (blob.getName().equals(fileName)) {
                cContents = blob.getContentsAsString();
                break;
            }
        }
        String gContents = "";
        for (Blob blob : givenBlobs.values()) {
            if (blob.getName().equals(fileName)) {
                gContents = blob.getContentsAsString();
                break;
            }
        }
        String contents = "<<<<<<< HEAD\n";
        contents += cContents + "\n";
        contents += "=======\n" + gContents;
        contents += "\n>>>>>>>\n";
        Utils.writeContents(new File(fileName), contents);
        add(fileName);
        Utils.message("Encountered a merge conflict.");
    }

    /**
     * Helper Function for check whether the exact file has been changed
     * from commit H to commit I.
     * @param fileName String the name of file you want to check
     * @param h HashMap<String, Blob> tracked blobs in commit H
     * @param i HashMap<String, Blob> tracked blobs in commit i
     * @return a boolean if the file with name F has been modified from
     * commit H to commit I.
     */
    boolean isModified(String fileName, HashMap<String, Blob> h, HashMap<String, Blob> i) {
        String b1 = "", b2 = "";
        for (Blob blob : h.values()) {
            if (blob.getName().equals(fileName)) {
                b1 = blob.getHashID();
            }
        }
        for (Blob blob : i.values()) {
            if (blob.getName().equals(fileName)) {
                b2 = blob.getHashID();
            }
        }
        return !b1.equals(b2);

    }

    /** Takes in two branch names, BRANCH1 and BRANCH2. Returns the
     * SHA ID of the common ancestor commit. */
    private String splitPoint(String currentBranch, String givenBranch) {
        String head1hash = _branches.get(currentBranch);
        String head2hash = _branches.get(givenBranch);
        Commit head1 = uidToCommit(head1hash);
        Commit head2 = uidToCommit(head2hash);

        branch1Commits.put(head1, 0);
        branch2Commits.put(head2, 0);
        Commit splitCommit = new Commit();

        DFS(head1, 0, "branch1");
        DFS(head2, 0, "branch2");
        int dist = 100;

        for (Commit current : branch1Commits.keySet()) {
            for (Commit given : branch2Commits.keySet()) {
                if (current.getUid().equals(given.getUid())) {
                    if (branch1Commits.get(current) < dist) {
                        splitCommit = current;
                        dist = branch1Commits.get(current);
                    }
                }
            }
        }
        branch1Commits.clear();
        branch2Commits.clear();
        return splitCommit.getUid();

    }

    public void DFS(Commit head, int dist, String branch) {
        if (head == null) {
            return;
        }
        if (head.getAllParentID() == null) {
            return;
        }
        String[] headParent = head.getAllParentID();
        if (branch.equals("branch1")) {
            for (String s : headParent) {
                Commit temp = uidToCommit(s);
                dist += 1;
                branch1Commits.put(temp, dist);
                DFS(temp, dist, "branch1");
            }
        } else {
            for (String s : headParent) {
                Commit temp = uidToCommit(s);
                dist += 1;
                branch2Commits.put(temp, dist);
                DFS(temp, dist, "branch2");
            }
        }
    }


    /** This function takes in the present working directory
     * PWD and will determine if there are untracked files
     * that mean that this checkout or Merge operation can't
     * continue. */
    private void checkForUntracked(File pwd) {
        Commit lastCommit = uidToCommit(getHead());
        HashMap<String, Blob> trackedFiles = lastCommit.getBlobs();
        String s = "There is an untracked file in the way; delete it, " +
                "or add and commit it first.";
        for (File f : Objects.requireNonNull(pwd.listFiles())) {
            if (f.isDirectory()) {
               continue;
            }
            if (f.getName().equals(".DS_Store") || f.getName().equals(".gitignore") || f.getName().equals("proj3.iml") || f.getName().equals("Makefile")) {
                continue; // FIXME: Delete
            }
            if (trackedFiles == null) {
                if (Objects.requireNonNull(pwd.listFiles()).length > 1) {
                    Utils.message(s);
                    throw new GitletException();
                }
            } else {
                boolean notTracked = !trackedFiles.containsKey(f.getName());
                boolean notStaging = !_stagingArea.containsKey(f.getName());
                boolean notRoot = !f.getName().equals(".gitlet");
                if (notRoot && notTracked && notStaging) {
                    Utils.message(s);
                    throw new GitletException();
                }
            }
        }
    }

    /**
     * This method is used to find the corresponding Commit object
     * according to the unique uid it contains.
     * @param uid String the uid for each Commit object
     * @return Commit object read from file
     */
    public Commit uidToCommit(String uid) {
        File f = new File(".gitlet/commits/" + uid);
        if (f.exists()) {
            return Utils.readObject(f, Commit.class);
        } else {
            Utils.message("No commit with that id exists.");
            throw new GitletException();
        }
    }

    /** Takes in a shortened String ID and returns a String
     * of the full length ID. */
    private String shortToLong(String id) {
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
}
