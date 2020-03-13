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
        _usingRotors = new ArrayList<Rotor>(_numRotors);
        int flag = 0;
        for (String rotor : rotors) {
            for (Rotor temp : _allRotors) {
                if (rotor.equals(temp.name())) {
                    flag = 1;
                }
            }
            if (flag == 0) {
                throw error("Bad rotor name");
            } else {
                flag = 0;
            }
        }
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor temp: _allRotors) {
                if ((temp.name()).equals(rotors[i])) {
                    _usingRotors.add(i, temp);
                }
            }
        }
        if (!_usingRotors.get(0).reflecting()) {
            throw error("Reflector in wrong place!");
        }
        int count = 0;
        for (int i = 0; i < _numRotors; i++) {
            if (_usingRotors.get(i).rotates()) {
                count++;
            }
        }
        if (count != _pawls) {
            throw error("Wrong number of argument!");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw error("setting length error should be numRotors() - 1!");
        }
        for (int i = 1; i < _usingRotors.size(); i++) {
            Rotor temp = _usingRotors.get(i);
            temp.set(_alphabet.toInt(setting.charAt(i - 1)));
        }
    }

    /** Set default setting according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void resetRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw error("setting length error should be numRotors() - 1!");
        }
        for (int i = 1; i < _usingRotors.size(); i++) {
            Rotor temp = _usingRotors.get(i);
            temp.rset(_alphabet.toInt(setting.charAt(i - 1)));
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
        int countRotors = _usingRotors.size() - numPawls() - 1;
        boolean[] isAdvance = new boolean[_numRotors];
        for (int i = countRotors; i < _usingRotors.size(); i++) {
            if (i == _usingRotors.size() - 1) {
                _usingRotors.get(i).advance();
                continue;
            }
            if (_usingRotors.get(i).rotates()
                    && _usingRotors.get(i + 1).atNotch()) {
                isAdvance[i] = true;
                isAdvance[i + 1] = true;
                i++;
            }
        }
        for (int i = _numRotors - 1; i > 0; i--) {
            if (isAdvance[i]) {
                _usingRotors.get(i).advance();
            }
        }
        int res = _plugBoard.permute(c);
        for (int i = _usingRotors.size() - 1; i >= 0; i--) {
            res = _usingRotors.get(i).convertForward(res);
        }
        for (int i = 1; i < _usingRotors.size(); i++) {
            res = _usingRotors.get(i).convertBackward(res);
        }
        res = _plugBoard.invert(res);
        return res;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        char[] temp = msg.toCharArray();
        for (int i = 0; i < temp.length; i++) {
            int convertInt = convert(_alphabet.toInt(msg.charAt(i)));
            temp[i] = _alphabet.toChar(convertInt);
        }
        String result = new String(temp);
        return result;
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
    private ArrayList<Rotor> _usingRotors;
    /** The plugboard. */
    private Permutation _plugBoard;
}
