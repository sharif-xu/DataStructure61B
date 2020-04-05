/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import javax.swing.text.MutableAttributeSet;
import java.awt.*;

import static loa.Piece.*;

/** An automated Player.
 *  @author Ruize Xu
 */
class MachinePlayer extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new MachinePlayer with no piece or controller (intended to produce
     *  a template). */
    MachinePlayer() {
        this(null, null);
    }

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
    }

    @Override
    String getMove() {
        Move choice;

        assert side() == getGame().getBoard().turn();
        int depth;
        choice = searchForMove();
        getGame().reportMove(choice);
        return choice.toString();
    }

    @Override
    Player create(Piece piece, Game game) {
        return new MachinePlayer(piece, game);
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private Move searchForMove() {
        Board work = new Board(getBoard());
        int value;
        assert side() == work.turn();
        _foundMove = null;
        if (side() == WP) {
            value = findMove(work, chooseDepth(), true, 1, -INFTY, INFTY);
        } else {
            value = findMove(work, chooseDepth(), true, -1, -INFTY, INFTY);
        }
        return _foundMove;
    }

     /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        /**
         * if depth == 0 ...:
         *  Do something to get value of that node.
         * bestscore = 0;
         * for move in moves:
         *  score = findMove(update_b, depth - 1, ...)
         *  if score better than the best score:
         *      bestscore = score;
         *  if maximizing:
         *      alpha = max(score, alpha);
         *  else:
         *      beta = min(score, beta);
         *  if alpha >= beta:
         *      prune;
         * return bestscore;
         */
        // FIXME
        if (board.piecesContiguous(WP)) {
            return WINNING_VALUE;
        }
        if (board.piecesContiguous(BP)) {
            return -WINNING_VALUE;
        }
        if (depth == 0) {
            return heuristic(board);
        }
        int score, bestscore = 0;
        Move temp = null;
        for (Move move : board.legalMoves()) {
            if (sense == 1) { //maximize
                board.makeMove(move);
                score = findMove(board, depth - 1, false, -1, alpha, beta);
                board.retract();
                bestscore = -INFTY;
                if (bestscore < score) {
                    bestscore = score;
                    temp = move;
                }
                if (bestscore > beta) {
                    break;
                }
                alpha = Math.max(alpha, bestscore);
            } else { //minimize
                board.makeMove(move);
                score = findMove(board, depth - 1, false, 1, alpha, beta);
                board.retract();
                bestscore = INFTY;
                if (bestscore > score) {
                    bestscore = score;
                    temp = move;
                }
                if (bestscore < alpha) {
                    break;
                }
                beta = Math.min(beta, score);
                if (alpha >= beta) {
                    break;
                }
            }
            if (alpha >= beta) {
                break;
            }
        }
        if (saveMove) {
            _foundMove = temp;
        }

//        int score, bestscore = 0;
//        Move temp = null;
//        if (sense == 1){
//            int maxscore = -INFTY;
//            for (Move move : board.legalMoves()) {
//                board.makeMove(move);
//                score = findMove(board, depth - 1, false, -1, alpha, beta);
//                board.retract();
//                if (maxscore < score) {
//                    maxscore = score;
//                    temp = move;
//                    bestscore = maxscore;
//                }
//                if (maxscore > beta) {
//                    break;
//                }
//                alpha = Math.max(alpha, maxscore);
//                if (alpha >= beta) {
//                    break;
//                }
//            }
//            if (saveMove) {
//                _foundMove = temp;
//            }
//        } else {
//            int minscore = INFTY;
//            for (Move move : board.legalMoves()) {
//                board.makeMove(move);
//                score = findMove(board, depth - 1, false, 1, alpha, beta);
//                board.retract();
//                if (minscore > score) {
//                    minscore = score;
//                    temp = move;
//                    bestscore = minscore;
//                }
//                if (minscore < alpha) {
//                    break;
//                }
//                beta = Math.min(beta, score);
//                if (alpha >= beta) {
//                    break;
//                }
//            }
//            if (saveMove) {
//                _foundMove = temp;
//            }
//        }
        return bestscore;
    }

    /** Return a search depth for the current position. */
    private int chooseDepth() {
        return 3;
    }

    private int heuristic(Board board) {
        int max = board.getRegionSizes(WP).size();
        int min = board.getRegionSizes(BP).size();
        return min - max;
    }

    // FIXME: Other methods, variables here.

    /** Used to convey moves discovered by findMove. */
    private Move _foundMove;

}
