package enigma;

import com.puppycrawl.tools.checkstyle.checks.sizes.FileLengthCheck;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Ruize Xu
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine mc = readConfig();
        psetting = _input.nextLine();
        if (!psetting.contains("*")) {
            throw error("It is no a vaild setting!");
        }
        while (_input.hasNext()) {
            if (psetting.contains("*")) {
                setUp(mc, psetting);
            }
            psetting = _input.nextLine();
            if (!psetting.contains("*")) {
                String msg = psetting;
                msg = msg.replace(" ","");
                msg = mc.convert(msg);
                printMessageLine(msg);
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            if (!_config.hasNext()){
                throw error("This is an empty configure file!");
            }
            String temp = _config.next();
            _alphabet = new Alphabet();
            _numRotors = _config.nextInt();
            _pawls = _config.nextInt();
            _allRotors = new ArrayList<Rotor>();
            while (_config.hasNext()) {
                mName = _config.next();
                Rotor rotor = readRotor();
                _allRotors.add(rotor);
            }
            return new Machine(_alphabet, _numRotors, _pawls, _allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
           String setUp = _config.next();
           String cycles = "";
           next = _config.next();
           while (next.contains("(")) {
               cycles = cycles.concat(next);
               if (_config.hasNext()) {
                   next = _config.next();
               } else {
                   break;
               }
           }
           Permutation p = new Permutation(cycles, _alphabet);
           char[] setUps = setUp.toCharArray();
           String notches = "";
           boolean flag1 = (setUps[0] == 'M');
           boolean flag2 = (setUps[0] == 'N');
           boolean flag3 = (setUps[0] == 'R');
           if (flag1) {
               for (int i = 1; i < setUps.length ; i++) {
                   notches = notches.concat(Character.toString(setUps[i]));
               }
               return new MovingRotor(mName, p, notches);
           }
           if (flag2) {
               return new FixedRotor(mName, p);
           }
           if (flag3) {
               return new Reflector(mName, p);
           } else {
               return null;
           }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        int i = 0;
        String[] rotors = new String[_numRotors];
        String[] temp = settings.split(" ");
        for (i = 0; i < _numRotors; i++) {
            rotors[i] = temp[i + 1];
        }
        for (int j = 0; j < rotors.length; j++) {
            for (int k = j + 1; k < rotors.length ; k++) {
                if (rotors[j].equals(rotors[k])) {
                    throw error("Rotors can only be used once!");
                }
            }
        }
        M.insertRotors(rotors);
        if (temp[i + 1].length() != _numRotors - 1) {
            throw error("Wrong input of the settings for each rotor!");
        }
        M.setRotors(temp[i + 1]);
        int j = 0;
        String cycles = "";
        for (j = i + 2; j < temp.length; j++) {
            if (temp[j].length() != 4){
                throw error("The plugboard must have two elements!");
            }
            if (temp[j].contains("(") && temp[j].contains(")")) {
                cycles = cycles.concat(temp[j]);
            } else {
                throw error("Wrong setting on the plugboard");
            }
        }
        Permutation p = new Permutation(cycles, _alphabet);
        M.setPlugboard(p);
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        msg = msg.replace(" ","");
        char[] temp = msg. toCharArray();
        int len = temp.length + temp.length / 5;
        char[] temp2 = new char[len];
        int j = 0, index = 0;
        for (char c : temp) {
            if (j < 5) {
                temp2[index++] = c;
                j++;
            }
            if (j == 5) {
                temp2[index++] = ' ';
                j = 0;
            }
        }
        String temp3 = new String(temp2);
        _output.println(temp3);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
    /** Number of rotors. */
    private int _numRotors;
    /** Number of moving rotors. */
    private int _pawls;
    /** The collection of all rotors. */
    private Collection<Rotor> _allRotors;
    /** Setting in the process() part. */
    private String psetting;
    /** Name of the machine. */
    private String mName;
    /** Next String in the _config. */
    private String next;
}
