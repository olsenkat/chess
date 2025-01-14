package chess.pieceMoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator
{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        // Initialize variables used
        var moves = new ArrayList<ChessMove>();
        int CHESS_WIDTH = 8; // Width of board
        int chess_down = myPosition.getRow()- 1; // Chess tiles below this piece
        int chess_up = CHESS_WIDTH - myPosition.getRow(); // Chess tiles above this piece
        int chess_left = myPosition.getColumn() - 1; // Chess tiles to the left of this piece
        int chess_right = CHESS_WIDTH - myPosition.getColumn(); // Chess tiles to the right of this piece
        ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor(); // Team color

        int position_determine; // Determines how many times the loop plays (how far the Bishop goes)
        ChessPosition start_pos = myPosition; // Used in new ChessMove instances

        // Determine available spaces right
        for (int i = 1; i <= chess_right; i++)
        {
            // Get the end position and move to add;
            ChessPosition end_pos = new ChessPosition(myPosition.getRow(),myPosition.getColumn()+i);
            ChessMove temp = new ChessMove(start_pos, end_pos, null);
            // Check if there is another piece in that location
            if (board.getPiece(end_pos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(end_pos).getTeamColor()!=teamColor)
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

        // Determine available spaces left
        for (int i = 1; i <= chess_left; i++)
        {
            // Get end position and move to add;
            ChessPosition end_pos = new ChessPosition(myPosition.getRow(),myPosition.getColumn()-i);
            ChessMove temp = new ChessMove(start_pos, end_pos, null);
            // Check if there is another piece in that location
            if (board.getPiece(end_pos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(end_pos).getTeamColor()!=teamColor)
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

        // Determine available spaces down
        for (int i = 1; i <= chess_down; i++)
        {
            // Get end position and move to add;
            ChessPosition end_pos = new ChessPosition(myPosition.getRow()-i,myPosition.getColumn());
            ChessMove temp = new ChessMove(start_pos, end_pos, null);
            // Check if there is another piece in that location
            if (board.getPiece(end_pos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(end_pos).getTeamColor()!=teamColor)
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



        // Determine available spaces up
        for (int i = 1; i <= chess_up; i++)
        {
            // Get end position and move to add;
            ChessPosition end_pos = new ChessPosition(myPosition.getRow()+i,myPosition.getColumn());
            ChessMove temp = new ChessMove(start_pos, end_pos, null);
            // Check if there is another piece in that location
            if (board.getPiece(end_pos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(end_pos).getTeamColor()!=teamColor)
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
