package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Ruize Xu
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    @Override
    boolean atNotch() {
        char ch = permutation().alphabet().toChar(setting());
        for (int i = 0; i < _notches.length(); i++) {
            if (_notches.charAt(i) == ch) {
                return true;
            }
        }
        return false;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        int newPos = permutation().wrap(setting() + 1);
        set(newPos);
    }

    /** The notch oof the rotor. */
    private String _notches;
}
