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
            if (validCommand(args[0])) {
                ArrayList<String> command = new ArrayList<>(Arrays.asList(args));
                String cwd = System.getProperty("user.dir");
                Repo commands = null;
                File tmpDir = new File(cwd + "/.gitlet");
                if (tmpDir.exists()) {
                    File mr =  new File(".gitlet/repo");
                    if (mr.exists()) {
                        commands = Utils.readObject(mr, Repo.class);
                    }

                }
                String _command = command.remove(0);
                ArrayList<String> _operand = command;
                if (_command.equals("init")) {
                    commands = new Repo();
                    File mr = new File(".gitlet/repo");
                    Utils.writeObject(mr, commands);
                }
                switch (_command) {
                    case "add":
                        if (_operand.size() == 1) {
                            commands.add(_operand.get(0));
                            break;
                        } else {
                            System.out.println("Incorrect Argument");
                        }
                    case "log":
                        if (_operand.isEmpty()) {
                            commands.log();
                            break;
                        } else {
                            System.out.println("Incorrect Arguments");
                        }
                    case "checkout":
                        if (_operand.size() == 1) {
                            commands.checkout(_operand.get(0));
                        } else {
                            commands.checkout(_operand);
                        }
                        break;
                    case "commit":
                        if (_operand.size() == 1) {
                            commands.commit(_operand.get(0));
                        } else {
                            Utils.message("Incorrect Arguments");
                            System.exit(0);
                        }
                        break;
                }
                Utils.writeObject(new File(".gitlet/repo"), commands);

            } else {
                Util.getMessage(args[0]+" is not a valid command!");
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
