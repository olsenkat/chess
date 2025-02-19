package chess.piecemoves;

import chess.*;

import java.util.Collection;
import java.util.*;


public class BishopMovesCalculator
{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        // Initialize variables used
        var moves = new ArrayList<ChessMove>();
        final int CHESS_WIDTH = 8; // Width of oard
        int chessDown = myPosition.getRow()- 1; // Chess tiles below this piece
        int chessUp = CHESS_WIDTH - myPosition.getRow(); // Chess tiles above this piece
        int chessLeft = myPosition.getColumn() - 1; // Chess tiles to the left of this piece
        int chessRight = CHESS_WIDTH - myPosition.getColumn(); // Chess tiles to the right of this piece
        ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor(); // Team color

        int positionDetermine; // Determines how many times the loop plays (how far the Bishop goes)

        // Determine available spaces right and up
        positionDetermine = Math.min(chessUp, chessRight);
        for (int i = 1; i <= positionDetermine; i++)
        {
            // Get the end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()+i,myPosition.getColumn()+i);
            ChessMove temp = new ChessMove(myPosition, endPos, null);
            // Check if there is another piece in that location
            if (board.getPiece(endPos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(endPos).getTeamColor()!=teamColor)
                {
                    moves.add(temp);
                }
                break;
                // Add functionality to take another piece
            }
            else
            {
                moves.add(temp);
            }
        }

        // Determine available spaces right and down
        positionDetermine = Math.min(chessDown, chessRight);
        for (int i = 1; i <= positionDetermine; i++)
        {
            // Get end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()-i,myPosition.getColumn()+i);
            ChessMove temp = new ChessMove(myPosition, endPos, null);
            // Check if there is another piece in that location
            if (board.getPiece(endPos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(endPos).getTeamColor()!=teamColor)
                {
                    moves.add(temp);
                }
                break;
                // Add functionality to take another piece
            }
            else
            {
                moves.add(temp);
            }
        }

        // Determine available spaces left and down
        positionDetermine = Math.min(chessDown, chessLeft);
        for (int i = 1; i <= positionDetermine; i++)
        {
            // Get end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()-i,myPosition.getColumn()-i);
            ChessMove temp = new ChessMove(myPosition, endPos, null);
            // Check if there is another piece in that location
            if (board.getPiece(endPos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(endPos).getTeamColor()!=teamColor)
                {
                    moves.add(temp);
                }
                break;
                // Add functionality to take another piece
            }
            else
            {
                moves.add(temp);
            }
        }

        // Determine available spaces left and up
        positionDetermine = Math.min(chessUp, chessLeft);
        for (int i = 1; i <= positionDetermine; i++)
        {
            // Get end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()+i,myPosition.getColumn()-i);
            ChessMove temp = new ChessMove(myPosition, endPos, null);
            // Check if there is another piece in that location
            if (board.getPiece(endPos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(endPos).getTeamColor()!=teamColor)
                {
                    moves.add(temp);
                }
                break;
                // Add functionality to take another piece
            }
            else
            {
                moves.add(temp);
            }
        }

        return moves;
    }
}
