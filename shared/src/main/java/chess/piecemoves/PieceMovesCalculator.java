package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A chess piece interface class that calculates the moves a chess piece can make.
 */
public abstract class PieceMovesCalculator
{
    protected ArrayList<ChessMove> moves;
    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    protected boolean addMove(ChessBoard board, ChessPosition myPosition, ChessPosition endPos, ChessGame.TeamColor teamColor)
    {
        ChessMove temp = new ChessMove(myPosition, endPos, null);
        // Check if there is another piece in that location
        if (board.getPiece(endPos) != null)
        {
            // If it is yours: ignore. Else: capture and add to moves
            if (board.getPiece(endPos).getTeamColor()!=teamColor)
            {
                moves.add(temp);
            }
            return false;
            // Add functionality to take another piece
        }
        else
        {
            moves.add(temp);
        }
        return true;
    }
}
