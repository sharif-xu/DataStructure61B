package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Ruize Xu
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("Reflector has only one position");
        }
    }

    @Override
    /** Return true iff I reflect. */
    boolean reflecting() {
        return true;
    }
}
