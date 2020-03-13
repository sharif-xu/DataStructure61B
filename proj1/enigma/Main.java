package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

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
        String setting = _input.nextLine();

        if (!setting.contains("*")) {
            throw error("The first line is not the valid setting!");
        }
        while (_input.hasNextLine()) {
            if (setting.contains("*")) {
                setUp(mc, setting);
            }
            setting = _input.nextLine();
            if (!setting.contains("*")) {
                String msg = setting;
                msg = msg.replace(" ", "");
                msg = mc.convert(msg);
                printMessageLine(msg);
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            if (!_config.hasNext()) {
                throw error("Empty config file!");
            }
            _alphabet = new Alphabet(_config.next());
            _allRotors = new ArrayList<Rotor>();
            if (!_config.hasNext()) {
                throw error("No rotor info!");
            }
            if (!_config.hasNextInt()) {
                throw error("No rotors!");
            }
            _numRotors = _config.nextInt();
            if (!_config.hasNextInt()) {
                throw error("No moving rotor!");
            }
            _pawls = _config.nextInt();
            if (!_config.hasNext()) {
                throw error("No rotors' setting!");
            }
            next = _config.next();
            while (_config.hasNext()) {
                rname = next;
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
                if (!next.contains(")")) {
                    throw error("badconf3!");
                }
                cycles = cycles.concat(next);
                if (_config.hasNext()) {
                    next = _config.next();
                } else {
                    break;
                }
            }
            Permutation perm = new Permutation(cycles, _alphabet);
            char[] setUps = setUp.toCharArray();
            String notches = "";
            boolean flag1 = (setUps[0] == 'M');
            boolean flag2 = (setUps[0] == 'N');
            boolean flag3 = (setUps[0] == 'R');
            if (!flag1 && !flag2 && !flag3) {
                return null;
            }
            if (flag1) {
                for (int j = 1; j < setUps.length; j++) {
                    notches = notches.concat(Character.toString(setUps[j]));
                }
                return new MovingRotor(rname, perm, notches);
            } else if (flag2) {
                return new FixedRotor(rname, perm);
            } else if (flag3) {
                return new Reflector(rname, perm);
            } else {
                throw error("Type of the rotors is wrong!");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] rotors = new String[_numRotors];
        String[] temp = settings.split(" ");
        if (temp.length - 1 < M.numRotors()) {
            throw error("lack of rotors!");
        }
        for (int i = 0; i < _numRotors; i++) {
            rotors[i] = temp[i + 1];
        }
        for (int i = 0; i < rotors.length; i++) {
            for (int j = i + 1; j < rotors.length; j++) {
                if (rotors[i].equals(rotors[j])) {
                    throw error("repeated rotor!");
                }
            }
        }

        String cycles = "";
        for (int j = _numRotors + 2; j < temp.length; j++) {
            if (temp[j].matches("^\\([A-Z]{2}\\)$")) {
                cycles = cycles.concat(temp[j]);
            } else {
                throw error("Wrong setting on the plugboard");
            }
        }
        M.insertRotors(rotors);
        M.setRotors(temp[_numRotors + 1]);
        Permutation perm = new Permutation(cycles, _alphabet);
        M.setPlugboard(perm);
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        msg = msg.replace(" ", "");
        char[] inputCharArr = msg.toCharArray();
        int len = inputCharArr.length + inputCharArr.length / 5;
        char[] temp2 = new char[len];
        int count = 0;
        int index = 0;
        for (char c : inputCharArr) {
            temp2[index++] = c;
            count++;
            if (count == 5) {
                temp2[index++] = ' ';
                count = 0;
            }
        }
        String finalStr = new String(temp2);
        _output.println(finalStr);
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

    /** Number of the moving rotors. */
    private int _pawls;

    /** The collection of all the rotors. */
    private Collection<Rotor> _allRotors;

    /** The next String in the _config. */
    private String next;

    /** The name of the machine. */
    private String rname;

}
