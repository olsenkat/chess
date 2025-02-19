package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator
{
    ArrayList<ChessMove> moves;
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        // Initialize variables used
        moves = new ArrayList<ChessMove>();
        final int chessWidth = 8; // Width of board
        int chessDown = myPosition.getRow()- 1; // Chess tiles below this piece
        int chessUp = chessWidth - myPosition.getRow(); // Chess tiles above this piece
        int chessLeft = myPosition.getColumn() - 1; // Chess tiles to the left of this piece
        int chessRight = chessWidth - myPosition.getColumn(); // Chess tiles to the right of this piece
        ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor(); // Team color

        // Determine available spaces right
        for (int i = 1; i <= chessRight; i++)
        {
            // Get the end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow(),myPosition.getColumn()+i);
            if (!getSpace(board, myPosition, endPos, teamColor))
            {
                break;
            }
        }

        // Determine available spaces left
        for (int i = 1; i <= chessLeft; i++)
        {
            // Get end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow(),myPosition.getColumn()-i);
            if (!getSpace(board, myPosition, endPos, teamColor))
            {
                break;
            }
        }

        // Determine available spaces down
        for (int i = 1; i <= chessDown; i++)
        {
            // Get end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()-i,myPosition.getColumn());
            if (!getSpace(board, myPosition, endPos, teamColor))
            {
                break;
            }
        }

        // Determine available spaces up
        for (int i = 1; i <= chessUp; i++)
        {
            // Get end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()+i,myPosition.getColumn());
            if (!getSpace(board, myPosition, endPos, teamColor))
            {
                break;
            }
        }

        return moves;
    }

    private boolean getSpace(ChessBoard board, ChessPosition myPosition, ChessPosition endPos, ChessGame.TeamColor teamColor)
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
        }
        else
        {
            moves.add(temp);
        }
        return true;
    }
}
