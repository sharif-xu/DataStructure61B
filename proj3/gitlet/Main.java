package gitlet;

import com.sun.tools.corba.se.idl.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Ruize Xu
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        try {
            if (args.length == 0) {
                Utils.message("Please enter a command.");
                throw new GitletException();
            }
            ArrayList<String> command = new ArrayList<>(Arrays.asList(args));
            String cwd = System.getProperty("user.dir");
            Repo repo = null;
            File tmpDir = new File(cwd + "/.gitlet");
            if (tmpDir.exists()) {
                File mr =  new File(".gitlet/repo");
                if (mr.exists()) {
                    repo = Utils.readObject(mr, Repo.class);
                }

            }
            String _command = command.remove(0);
            ArrayList<String> _operand = command;
            if (_command.equals("init")) {
                if (!tmpDir.exists()){
                    repo = new Repo();
                    File mr = new File(".gitlet/repo");
                    Utils.writeObject(mr, repo);
                } else {
                    System.out.println("A Gitlet version-control "
                            + "system already exists in the "
                            + "current directory");
                    System.exit(0);
                }
            }
            if (validCommand(args[0])) {
                if (!tmpDir.exists()) {
                    Utils.message("Not in an initialized Gitlet directory.");
                    System.exit(0);
                }
                switch (_command) {
                    case "add":
                        if (_operand.size() == 1) {
                            repo.add(_operand.get(0));
                            break;
                        } else {
                            System.out.println("Incorrect Argument");
                        }
                    case "log":
                        if (_operand.isEmpty()) {
                            repo.log();
                            break;
                        } else {
                            System.out.println("Incorrect Arguments");
                        }
                    case "global-log":
                        if (_operand.isEmpty()) {
                            repo.globalLog();
                            break;
                        } else {
                            System.out.println("Incorrect Arguments");
                        }
                    case "checkout":
                        if (_operand.size() == 1) {
                            repo.checkout(_operand.get(0));
                        } else {
                            repo.checkout(_operand);
                        }
                        break;
                    case "commit":
                        if (_operand.size() == 1) {
                            repo.commit(_operand.get(0));
                        } else {
                            Utils.message("Incorrect Arguments");
                            System.exit(0);
                        }
                        break;
                    case "status":
                        if (_operand.size() == 0) {
                            repo.status();
                        } else {
                            Utils.message("Incorrect Arguments");
                            System.exit(0);
                        }
                        break;
                    case "rm":
                        if (_operand.size() == 1) {
                            repo.rm(_operand.get(0));
                        } else {
                            Utils.message("Incorrect Arguments");
                            System.exit(0);
                        }
                        break;
                    case  "branch":
                        if (_operand.size() == 1) {
                            repo.branch(_operand.get(0));
                        } else {
                            Utils.message("Incorrect Arguments");
                            System.exit(0);
                        }
                        break;
                    case "rm-branch":
                        if (_operand.size() == 1) {
                            repo.rmbranch(_operand.get(0));
                        } else {
                            Utils.message("Incorrect Arguments");
                            System.exit(0);
                        }
                        break;
                    case "reset":
                        if (_operand.size() == 1) {
                            repo.reset(_operand.get(0));
                        } else {
                            Utils.message("Incorrect Arguments");
                            System.exit(0);
                        }
                        break;
                    case "find":
                        if (_operand.size() == 1) {
                            repo.find(_operand.get(0));
                        } else {
                            Utils.message("Incorrect Arguments");
                            System.exit(0);
                        }
                        break;
                    case "merge":
                        if (_operand.size() == 1) {
                            repo.merge(_operand.get(0));
                        } else {
                            Utils.message("Incorrect Arguments");
                            System.exit(0);
                        }
                        break;
                    case "default":
                        Utils.message(" No command with that name exists.");
                        System.exit(0);
                }
                Utils.writeObject(new File(".gitlet/repo"), repo);
            } else {
                Utils.message(" No command with that name exists.");
                throw new GitletException();
            }
        } catch (GitletException e) {
            System.exit(0);
        }
    }


    /** Takes in a string ARG word, will return whether or not
     * it is a valid command. */
    private static boolean validCommand(String arg) {
        for (String command: _vaildCommands) {
            if (arg.equals(command)) {
                return true;
            }
        }
        return false;
    }

    /** Array of possible valid commands. */
    private static String[] _vaildCommands = new String[] {"init", "add",
            "commit", "rm", "log", "global-log",
            "find", "status", "checkout",
            "branch", "rm-branch", "reset", "merge"};

}
