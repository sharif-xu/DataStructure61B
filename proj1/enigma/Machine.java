package enigma;

import java.util.ArrayList;
import java.util.HashMap;
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
        _allRotors = allRotors;
        _pawls = pawls;
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
        for (int i = 0; i < rotors.length ; i++) {
            int flag = 0;
            for (Rotor temp: _allRotors) {
                if ((temp.name().equals(rotors[i])) && flag == 0) {
                    _usingRotors.add(i,temp);
                    flag = 1;
                }
            }
            if (flag == 0) {
                throw error("The rotor is not in the AllRotors!");
            }
        }
        if (!_usingRotors.get(0).reflecting()) {
            throw error("The first rotor must be reflector!");
        }
        for (int i = _numRotors - _pawls; i < _numRotors; i++) {
            if (!_usingRotors.get(i).rotates()) {
                throw error("The rest rotors must be moving rotors");
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        int i = 0, j = 0;
        for (Rotor temp: _usingRotors) {
            if (j == 0) {
                j = 1;
                continue; //skip operation for finding all j=1's rotor and set them
            }
            temp.set(_alphabet.toInt(setting.charAt(i)));
            i++;
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        for (int i = _usingRotors.size() - numPawls() - 1;
             i < _usingRotors.size(); i++) {
            if (i == _usingRotors.size() - 1) {
                _usingRotors.get(i).advance();
                continue;
            }
            if (_usingRotors.get(i).rotates()) {
                if (_usingRotors.get(i + 1).atNotch()) {
                    _usingRotors.get(i).advance();
                    i++;
                }
            }
        }
        int temp = _plugboard.permute(c);
        for (int i = _usingRotors.size() - 1; i >= 0 ; i--) {
            temp = _usingRotors.get(i).convertForward(temp);
        }
        for (int i = 0; i < _usingRotors.size() ; i++) {
            temp = _usingRotors.get(i).convertBackward(temp);
        }
        temp = _plugboard.permute(temp);
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
    /** Number of moving rotors. */
    private int _pawls;
    /** The collection of all rotors. */
    private Collection<Rotor> _allRotors;
    /** The Arraylsit of using rotors. */
    private ArrayList<Rotor> _usingRotors;
    /** The plugboard. */
    private Permutation _plugboard;

}
