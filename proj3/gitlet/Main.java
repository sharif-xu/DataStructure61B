package gitlet;

import com.sun.tools.corba.se.idl.Util;
import com.sun.xml.internal.xsom.impl.scd.Iterators;

import java.io.File;
import java.io.Serializable;
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
                ArrayList<String> operands = new ArrayList<>(Arrays.asList(args));
                Command commands = new Command();
                commands.main(operands);
                Utils.writeContents(new File(".gitlet/gitlet"),
                        Utils.serialize(commands));

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
