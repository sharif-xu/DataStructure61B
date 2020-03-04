package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Ruize Xu
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        // doneFIXME
        _cycles = cycles;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        // doneFIXME
        this._cycles.concat(cycle);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return this.alphabet().size();
    }

    /** Helping function used to return the index of certain character
     */
    int getIndex(char c) {
        int index = 0;
        for (int i = 0; i < _cycles.length(); i++) {
            if (c == _cycles.charAt(i)) {
                index = i;
                break;
            }
        }
        return index;
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        p = wrap(p);
        char ch = alphabet().toChar(p);
        char permuteCh = permute(ch);
        return alphabet().toInt(permuteCh);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        c = wrap(c);
        char ch = alphabet().toChar(c);
        char invertCh = invert(ch);
        return alphabet().toInt(invertCh);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int index = getIndex(p);
        if (index == 0) {
            return p;
        }
        if (_cycles.charAt(index + 1) == ')') {
            while (_cycles.charAt(index) != '(') {
                index--;
            }
        }
        return _cycles.charAt(index + 1);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int index = getIndex(c);
        if (index == 0) {
            return c;
        }
        if (_cycles.charAt(index - 1) == '(') {
            while (_cycles.charAt(index) != ')') {
                index++;
            }
        }
        return _cycles.charAt(index - 1);
    }


    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 1; i < _cycles.length() - 1; i++) {
            if (_cycles.charAt(i) == _cycles.charAt(i-1)
                    || _cycles.charAt(i) == _cycles.charAt(i+1)) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    // doneFIXME: ADDITIONAL FIELDS HERE, AS NEEDED
    private String _cycles;
}
