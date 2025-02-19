package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator
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

        // Determine available spaces right (then up)
        if ((chess_right >=2) && (chess_up>=1))
        {
            // Get the end position and move to add;
            ChessPosition end_pos = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()+2);
            ChessMove temp = new ChessMove(start_pos, end_pos, null);
            if (board.getPiece(end_pos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                {
                    moves.add(temp);
                }
                // Add functionality to take another piece
            }
            else
            {
                moves.add(temp);
            }
        }
        // Determine available spaces right (then down)
        if ((chess_right >=2) && (chess_down>=1))
        {
            // Get the end position and move to add;
            ChessPosition end_pos = new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()+2);
            ChessMove temp = new ChessMove(start_pos, end_pos, null);
            if (board.getPiece(end_pos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                {
                    moves.add(temp);
                }
                // Add functionality to take another piece
            }
            else
            {
                moves.add(temp);
            }
        }


        // Determine available spaces left (then up)
        if ((chess_left >=2) && (chess_up>=1))
        {
            // Get the end position and move to add;
            ChessPosition end_pos = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()-2);
            ChessMove temp = new ChessMove(start_pos, end_pos, null);
            if (board.getPiece(end_pos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                {
                    moves.add(temp);
                }
                // Add functionality to take another piece
            }
            else
            {
                moves.add(temp);
            }
        }
        // Determine available spaces left (then down)
        if ((chess_left >=2) && (chess_down>=1))
        {
            // Get the end position and move to add;
            ChessPosition end_pos = new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()-2);
            ChessMove temp = new ChessMove(start_pos, end_pos, null);
            if (board.getPiece(end_pos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                {
                    moves.add(temp);
                }
                // Add functionality to take another piece
            }
            else
            {
                moves.add(temp);
            }
        }

        // Determine available spaces up (then right)
        if ((chess_up >=2) && (chess_right>=1))
        {
            // Get the end position and move to add;
            ChessPosition end_pos = new ChessPosition(myPosition.getRow()+2,myPosition.getColumn()+1);
            ChessMove temp = new ChessMove(start_pos, end_pos, null);
            if (board.getPiece(end_pos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                {
                    moves.add(temp);
                }
                // Add functionality to take another piece
            }
            else
            {
                moves.add(temp);
            }
        }
        // Determine available spaces up (then left)
        if ((chess_up >=2) && (chess_left>=1))
        {
            // Get the end position and move to add;
            ChessPosition end_pos = new ChessPosition(myPosition.getRow()+2,myPosition.getColumn()-1);
            ChessMove temp = new ChessMove(start_pos, end_pos, null);
            if (board.getPiece(end_pos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                {
                    moves.add(temp);
                }
                // Add functionality to take another piece
            }
            else
            {
                moves.add(temp);
            }
        }

        // Determine available spaces down (then right)
        if ((chess_down >=2) && (chess_right>=1))
        {
            // Get the end position and move to add;
            ChessPosition end_pos = new ChessPosition(myPosition.getRow()-2,myPosition.getColumn()+1);
            ChessMove temp = new ChessMove(start_pos, end_pos, null);
            if (board.getPiece(end_pos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                {
                    moves.add(temp);
                }
                // Add functionality to take another piece
            }
            else
            {
                moves.add(temp);
            }
        }
        // Determine available spaces down (then left)
        if ((chess_down >=2) && (chess_left>=1))
        {
            // Get the end position and move to add;
            ChessPosition end_pos = new ChessPosition(myPosition.getRow()-2,myPosition.getColumn()-1);
            ChessMove temp = new ChessMove(start_pos, end_pos, null);
            if (board.getPiece(end_pos) != null)
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                {
                    moves.add(temp);
                }
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
