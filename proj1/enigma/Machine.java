package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Ruize Xu
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _useRotors = new ArrayList<Rotor>(_numRotors);
        for (String rotor : rotors) {
            for (Rotor temp : _allRotors) {
                if (!rotor.equals(temp.name())) {
                    throw error("Bad rotor name!");
                }
            }
        }
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor temp: _allRotors) {
                if ((temp.name()).equals(rotors[i])) {
                    _useRotors.add(i, temp);
                }
            }
        }
        if (!_useRotors.get(0).reflecting()) {
            throw error("Reflector in wrong place!");
        }
//        int restRotors = _numRotors - _pawls;
//        for (int i = restRotors; i < _numRotors; i++) {
//            if (!_useRotors.get(i).rotates()) {
//                throw error("The rest rotors must be moving rotors!");
//            }
//        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw error("setting length should be numRotors()-1");
        }
        int i = 0, j = 0;
        for (Rotor r: _useRotors) {
            if (j == 0) {
                j = 1;
                continue;
            }
            r.set(_alphabet.toInt(setting.charAt(i)));
            i++;
        }
    }

    /** Set default setting according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void rsetRotors(String setting) {
        int i = 0, j = 0;
        for (Rotor temp: _useRotors) {
            if (j == 0) {
                j = 1;
                continue;
            }
            temp.rset(_alphabet.toInt(setting.charAt(i)));
            i++;
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugBoard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        for (int i = _useRotors.size() - numPawls() - 1;
             i < _useRotors.size(); i++) {
            if (i == _useRotors.size() - 1) {
                _useRotors.get(i).advance();
                continue;
            }
            if (_useRotors.get(i).rotates()) {
                if (_useRotors.get(i + 1).atNotch()) {
                    _useRotors.get(i).advance();
                    _useRotors.get(i + 1).advance();
                    i++;
                }
            }
        }
        int temp = _plugBoard.permute(c);
        for (int i = _useRotors.size() - 1; i >= 0; i--) {
            temp = _useRotors.get(i).convertForward(temp);
        }
        for (int i = 1; i < _useRotors.size(); i++) {
            temp = _useRotors.get(i).convertBackward(temp);
        }
        temp = _plugBoard.permute(temp);
        return temp;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        char[] temp = msg.toCharArray();
        for (int i = 0; i < temp.length; i++) {
            int medium = convert(_alphabet.toInt(msg.charAt(i)));
            temp[i] = _alphabet.toChar(medium);
        }
        String a = new String(temp);
        return a;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** Number of rotors. */
    private int _numRotors;
    /** Number of the moving rotors. */
    private int _pawls;
    /** The collection of all the rotors. */
    private Collection<Rotor> _allRotors;
    /** The using rotors. */
    private ArrayList<Rotor> _useRotors;
    /** The plugboard. */
    private Permutation _plugBoard;
}
