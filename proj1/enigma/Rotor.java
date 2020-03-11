package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Ruize Xu
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _posn = 0;
        _cposn = ' ';
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _posn;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _posn = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        _cposn = cposn;
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int temp = wrap(p + _posn - _rosn);
        temp = permutation().permute(temp);
        temp = wrap(temp - _posn + _rosn);
        return temp;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int temp = wrap(e + _posn);
        temp = permutation().invert(temp);
        temp = wrap(temp - _posn);
        return temp;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {

    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** Return the value of P modulo the size of this alphabet. */
    final int wrap(int p) {
        int r = p % alphabet().size();
        if (r < 0) {
            r += alphabet().size();
        }
        return r;
    }

    /** My name. */
    private final String _name;
    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;
    /** The starting position of the rotor. */
    private int _posn;
    /** The ring position of the rotor. */
    private int _rosn = 0;
    /** The starting position of the rotor in character. */
    private char _cposn = 0;

    /** Set setting() to ROSN. */
    void rset(int rosn) {
        this._rosn = rosn;
    }

}
