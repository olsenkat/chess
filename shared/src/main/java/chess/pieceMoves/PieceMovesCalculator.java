package chess.pieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

/**
 * A chess piece interface class that calculates the moves a chess piece can make.
 */
public abstract class PieceMovesCalculator
{
    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

}
