package chess.pieceMoves;

import chess.*;

import java.util.Collection;
import java.util.*;


public class BishopMovesCalculator
{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        // Initialize variables used
        var moves = new ArrayList<ChessMove>();
        int CHESS_WIDTH = 8; // Width of oard
        int chess_down = myPosition.getRow()- 1; // Chess tiles below this piece
        int chess_up = CHESS_WIDTH - myPosition.getRow(); // Chess tiles above this piece
        int chess_left = myPosition.getColumn() - 1; // Chess tiles to the left of this piece
        int chess_right = CHESS_WIDTH - myPosition.getColumn(); // Chess tiles to the right of this piece
        ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor(); // Team color

        int position_determine; // Determines how many times the loop plays (how far the Bishop goes)
        ChessPosition start_pos = myPosition; // Used in new ChessMove instances

        // Determine available spaces right and up
        position_determine = Math.min(chess_up, chess_right);
        for (int i = 1; i <= position_determine; i++)
        {
            // Get the end position and move to add;
            ChessPosition end_pos = new ChessPosition(myPosition.getRow()+i,myPosition.getColumn()+i);
            ChessMove temp = new ChessMove(start_pos, end_pos, null);
            // Check if there is another piece in that location
            if (board.getPiece(end_pos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                {
                    moves.add(temp);
                    // test - remove this
                    System.out.printf("Move added: (%d, %d)", myPosition.getRow()+i, myPosition.getColumn()+i);
                }
                break;
                // Add functionality to take another piece
            }
            else
            {
                moves.add(temp);
                // remove this
                System.out.printf("Move added: (%d, %d)", myPosition.getRow()+i, myPosition.getColumn()+i);
            }
        }

        // Determine available spaces right and down
        position_determine = Math.min(chess_down, chess_right);
        for (int i = 1; i <= position_determine; i++)
        {
            // Get end position and move to add;
            ChessPosition end_pos = new ChessPosition(myPosition.getRow()-i,myPosition.getColumn()+i);
            ChessMove temp = new ChessMove(start_pos, end_pos, null);
            // Check if there is another piece in that location
            if (board.getPiece(end_pos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                {
                    moves.add(temp);
                    // remove this
                    System.out.printf("Move added: (%d, %d)", myPosition.getRow()-i, myPosition.getColumn()+i);
                }
                break;
                // Add functionality to take another piece
            }
            else
            {
                moves.add(temp);
                // remove this
                System.out.printf("Move added: (%d, %d)", myPosition.getRow()-i, myPosition.getColumn()+i);
            }
        }

        // Determine available spaces left and down
        position_determine = Math.min(chess_down, chess_left);
        for (int i = 1; i <= position_determine; i++)
        {
            // Get end position and move to add;
            ChessPosition end_pos = new ChessPosition(myPosition.getRow()-i,myPosition.getColumn()-i);
            ChessMove temp = new ChessMove(start_pos, end_pos, null);
            // Check if there is another piece in that location
            if (board.getPiece(end_pos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                {
                    moves.add(temp);
                    // remove this
                    System.out.printf("Move added: (%d, %d)", myPosition.getRow()-i, myPosition.getColumn()-i);
                }
                break;
                // Add functionality to take another piece
            }
            else
            {
                moves.add(temp);
                // remove this
                System.out.printf("Move added: (%d, %d)", myPosition.getRow()-i, myPosition.getColumn()-i);
            }
        }

        // Determine available spaces left and up
        position_determine = Math.min(chess_up, chess_left);
        for (int i = 1; i <= position_determine; i++)
        {
            // Get end position and move to add;
            ChessPosition end_pos = new ChessPosition(myPosition.getRow()+i,myPosition.getColumn()-i);
            ChessMove temp = new ChessMove(start_pos, end_pos, null);
            // Check if there is another piece in that location
            if (board.getPiece(end_pos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                {
                    moves.add(temp);
                    // remove this
                    System.out.printf("Move added: (%d, %d)", myPosition.getRow()+i, myPosition.getColumn()-i);
                }
                break;
                // Add functionality to take another piece
            }
            else
            {
                moves.add(temp);
                // remove this
                System.out.printf("Move added: (%d, %d)", myPosition.getRow()+i, myPosition.getColumn()-i);
            }
        }

        return moves;
    }
}
