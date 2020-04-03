/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import net.sf.saxon.expr.instruct.ITemplateCall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Square.*;

/** Represents the state of a game of Lines of Action.
 *  @author Ruize Xu
 */
class Board {

    /** Default number of moves for each side that results in a draw. */
    static final int DEFAULT_MOVE_LIMIT = 60;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /** A Board whose initial contents are taken from INITIALCONTENTS
     *  and in which the player playing TURN is to move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row][col]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     *
     *  CAUTION: The natural written notation for arrays initializers puts
     *  the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /** A new board in the standard initial position. */
    Board() {
        this(INITIAL_PIECES, BP);
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        this();
        copyFrom(board);
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        int count = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j <  BOARD_SIZE; j++) {
                _board[count++] = contents[i][j];
            }
        }
        _turn = side;
        _moveLimit = DEFAULT_MOVE_LIMIT;
    }

    /** Set me to the initial configuration. */
    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        } else {
            System.arraycopy(board._board, 0, this._board, 0, BOARD_SIZE * BOARD_SIZE);
            this._turn = board._turn;
            this._moveLimit = DEFAULT_MOVE_LIMIT;
        }
    }

    /** Return the contents of the square at SQ. */
    Piece get(Square sq) {
        return _board[sq.index()];
    }

    /** Set the square at SQ to V and set the side that is to move next
     *  to NEXT, if NEXT is not null. */
    void set(Square sq, Piece v, Piece next) {
        _board[sq.index()] = v;
        _turn = next;
    }

    /** Set the square at SQ to V, without modifying the side that
     *  moves next. */
    void set(Square sq, Piece v) {
        set(sq, v, null);
    }

    /** Set limit on number of moves by each side that results in a tie to
     *  LIMIT, where 2 * LIMIT > movesMade(). */
    void setMoveLimit(int limit) {
        if (2 * limit <= movesMade()) {
            throw new IllegalArgumentException("move limit too small");
        }
        _moveLimit = 2 * limit;
    }

    /** Assuming isLegal(MOVE), make MOVE. This function assumes that
     *  MOVE.isCapture() will return false.  If it saves the move for
     *  later retraction, makeMove itself uses MOVE.captureMove() to produce
     *  the capturing move. */
    void makeMove(Move move) {
        assert isLegal(move);
        Square from = move.getFrom();
        Square to = move.getTo();
        Piece fp = get(from);
        Piece tp = get(to);
        set(from, EMP);
        set(to, fp, fp.opposite());
        if (fp == tp.opposite()) {
            move = Move.mv(from, to).captureMove();
        }
        _moves.add(move);
        _subsetsInitialized = false;
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        Move move = _moves.remove(_moves.size() - 1);
        Square from = move.getFrom();
        Square to = move.getTo();
        Piece currentTo = get(to);
        if (move.isCapture()) {
            set(to, currentTo.opposite());
            set(from, currentTo, currentTo);
        } else {
            set(to, EMP);
            set(from, currentTo, currentTo);
        }
    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff FROM - TO is a legal move for the player currently on
     *  move. */
    boolean isLegal(Square from, Square to) {
        Piece fromP = get(from);
        Piece toP = get(to);
        int distance = from.distance(to);
        int direction = from.direction(to);
        if (!from.isValidMove(to)) {
            return false;
        }
        if (fromP == toP) {
            return false;
        }
        if (blocked(from, to)) {
            return false;
        }
        return distance == calcNumPiece(direction, from);
    }

    /**
     * Helper function for calculate the numeber of !EMP pieces in
     * certain direction.
     * @param direction
     * @param from
     * @return
     */
    int calcNumPiece(int direction, Square from) {
        int opDir = 0;
        if (direction >= 4) {
            opDir = direction - 4;
        } else {
            opDir = direction + 4;
        }
        int count = 1;
        for (int i = 0; i < BOARD_SIZE; i++) {
            Square temp1 = from.moveDest(direction, i);
            Square temp2 = from.moveDest(opDir,i);
            if (temp1 != null) {
                if (get(temp1) != EMP) {
                    count++;
                }
            }
            if (temp2 != null) {
                if (get(temp2) != EMP) {
                    count++;
                }
            }
        }
        return count;
    }

    /** Return true iff MOVE is legal for the player currently on move.
     *  The isCapture() property is ignored. */
    boolean isLegal(Move move) {
        return isLegal(move.getFrom(), move.getTo());
    }

    /** Return a sequence of all legal moves from this position. */
    List<Move> legalMoves() {
        List<Move> allLegalMoves = new ArrayList<Move>();
        for (int i = 0; i < _board.length; i++) {
            Square currentSq = ALL_SQUARES[i];
            Piece currentP = _board[i];
            if (currentP == _turn) {
                for (int j = 0; j < _board.length; j++) {
                   Square nextSq = ALL_SQUARES[j];
                   Piece nextP = _board[nextSq.index()];
                    if (currentP != nextP) {
                        if (isLegal(currentSq, nextSq)) {
                            allLegalMoves.add(Move.mv(currentSq, nextSq));
                        }
                    }
                }
            }
        }
        return allLegalMoves;
    }

    /** Return true iff the game is over (either player has all his
     *  pieces continguous or there is a tie). */
    boolean gameOver() {
        return winner() != null;
    }

    /** Return true iff SIDE's pieces are continguous. */
    boolean piecesContiguous(Piece side) {
        return getRegionSizes(side).size() == 1;
    }

    /** Return the winning side, if any.  If the game is not over, result is
     *  null.  If the game has ended in a tie, returns EMP. */
    Piece winner() {
        if (!_winnerKnown) {
            if (_moves.size() > _moveLimit) {
                return EMP;
            }
            if (!piecesContiguous(_turn) && !piecesContiguous(_turn.opposite())) {
                return null;
            }
            if (piecesContiguous(_turn)) {
                _winner = _turn;
            } else {
                _winner = _turn.opposite();
            }
            _winnerKnown = true;
        }
        return _winner;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return Arrays.deepEquals(_board, b._board) && _turn == b._turn;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_board) * 2 + _turn.hashCode();
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = BOARD_SIZE - 1; r >= 0; r -= 1) {
            out.format("    ");
            for (int c = 0; c < BOARD_SIZE; c += 1) {
                out.format("%s ", get(sq(c, r)).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /** Return true if a move from FROM to TO is blocked by an opposing
     *  piece or by a friendly piece on the target square. */
    private boolean blocked(Square from, Square to) {
        int dir = from.direction(to);
        int distance = from.distance(to);
        for (int i = 0; i < distance; i++) {
            Square temp = from.moveDest(dir, i);
            if(temp != null) {
                if (get(temp) == get(from).opposite()) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Return the size of the as-yet unvisited cluster of squares
     *  containing P at and adjacent to SQ.  VISITED indicates squares that
     *  have already been processed or are in different clusters.  Update
     *  VISITED to reflect squares counted. */
    private int numContig(Square sq, boolean[][] visited, Piece p) {
        Piece currentP = get(sq);
        if (p == EMP) {
            return 0;
        }
        if (currentP != p) {
            return 0;
        }
        if (visited[sq.row()][sq.col()]) {
            return 0;
        } else {
            int count = 1;
            visited[sq.row()][sq.col()] = true;
            for (int i = 0; i < 8; i++) {
                Square next = sq.moveDest(i, 1);
                if (next != null) {
                    count += numContig(next, visited, p);
                }
            }
            return count;
        }
    }

    /** Set the values of _whiteRegionSizes and _blackRegionSizes. */
    private void computeRegions() {
        if (_subsetsInitialized) {
            return;
        }
        _whiteRegionSizes.clear();
        _blackRegionSizes.clear();
        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                visited[i][j] = false;
            }
        }
        for (int i = 0; i < _board.length; i++) {
            int row = 0;
            int col = i;
            if (i > 7) {
                col = i % BOARD_SIZE;
                row = i / BOARD_SIZE;
            }
            if (_board[i] == BP) {
                if (!visited[row][col]) {
                    _blackRegionSizes.add(numContig(ALL_SQUARES[i], visited, BP));
                }
            }
            if (_board[i] == WP) {
                if (!visited[row][col]) {
                    _whiteRegionSizes.add(numContig(ALL_SQUARES[i], visited, WP));
                }
            }
        }
        Collections.sort(_whiteRegionSizes, Collections.reverseOrder());
        Collections.sort(_blackRegionSizes, Collections.reverseOrder());
        _subsetsInitialized = true;
    }

    /** Return the sizes of all the regions in the current union-find
     *  structure for side S. */
    List<Integer> getRegionSizes(Piece s) {
        computeRegions();
        if (s == WP) {
            return _whiteRegionSizes;
        } else {
            return _blackRegionSizes;
        }
    }

    /** The standard initial configuration for Lines of Action (bottom row
     *  first). */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** Current contents of the board.  Square S is at _board[S.index()]. */
    private final Piece[] _board = new Piece[BOARD_SIZE  * BOARD_SIZE];

    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /** Current side on move. */
    private Piece _turn;
    /** Limit on number of moves before tie is declared.  */
    private int _moveLimit;
    /** True iff the value of _winner is known to be valid. */
    private boolean _winnerKnown;
    /** Cached value of the winner (BP, WP, EMP (for tie), or null (game still
     *  in progress).  Use only if _winnerKnown. */
    private Piece _winner;

    /** True iff subsets computation is up-to-date. */
    private boolean _subsetsInitialized;

    /** List of the sizes of continguous clusters of pieces, by color. */
    private final ArrayList<Integer>
        _whiteRegionSizes = new ArrayList<>(),
        _blackRegionSizes = new ArrayList<>();
}
