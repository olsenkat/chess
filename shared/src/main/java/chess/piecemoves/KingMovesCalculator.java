package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator
{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        // Initialize variables used
        var moves = new ArrayList<ChessMove>();
        final int CHESS_WIDTH = 8; // Width of board
        int chessDown = myPosition.getRow()- 1; // Chess tiles below this piece
        int chessUp = CHESS_WIDTH - myPosition.getRow(); // Chess tiles above this piece
        int chessLeft = myPosition.getColumn() - 1; // Chess tiles to the left of this piece
        int chessRight = CHESS_WIDTH - myPosition.getColumn(); // Chess tiles to the right of this piece

        // Move king up
        if (chessUp>=1)
        {
            // Create the end position
            ChessPosition endPos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
            // Check if there is a move, and add
            ChessMove move = determineMoves(board, myPosition, endPos);
            if (move!=null)
            {
                moves.add(move);
            }
        }
        // Move king down
        if (chessDown>=1)
        {
            // Create the end position
            ChessPosition endPos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
            // Check if there is a move, and add
            ChessMove move = determineMoves(board, myPosition, endPos);
            if (move!=null)
            {
                moves.add(move);
            }
        }
        // Move king right
        if (chessRight>=1)
        {
            // Create the end position
            ChessPosition endPos = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1);
            // Check if there is a move, and add
            ChessMove move = determineMoves(board, myPosition, endPos);
            if (move!=null)
            {
                moves.add(move);
            }
        }
        // Move king left
        if (chessLeft>=1)
        {
            // Create the end position
            ChessPosition endPos = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1);
            // Check if there is a move, and add
            ChessMove move = determineMoves(board, myPosition, endPos);
            if (move!=null)
            {
                moves.add(move);
            }
        }
        // Move king right/up diagonal
        if ((chessUp>=1) && (chessRight>=1))
        {
            // Create the end position
            ChessPosition endPos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
            // Check if there is a move, and add
            ChessMove move = determineMoves(board, myPosition, endPos);
            if (move!=null)
            {
                moves.add(move);
            }
        }
        // Move king right/down diagonal
        if ((chessDown>=1) && (chessRight>=1))
        {
            // Create the end position
            ChessPosition endPos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
            // Check if there is a move, and add
            ChessMove move = determineMoves(board, myPosition, endPos);
            if (move!=null)
            {
                moves.add(move);
            }
        }
        // Move king left/up diagonal
        if ((chessLeft>=1) && (chessUp>=1))
        {
            // Create the end position
            ChessPosition endPos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
            // Check if there is a move, and add
            ChessMove move = determineMoves(board, myPosition, endPos);
            if (move!=null)
            {
                moves.add(move);
            }
        }
        // Move king left/down diagonal
        if ((chessLeft>=1) && (chessDown>=1))
        {
            // Create the end position
            ChessPosition endPos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
            // Check if there is a move, and add
            ChessMove move = determineMoves(board, myPosition, endPos);
            if (move!=null)
            {
                moves.add(move);
            }
        }

        return moves;
    }

    public ChessMove determineMoves(ChessBoard board, ChessPosition myPosition, ChessPosition end_pos)
    {
        ChessMove move = null;
        ChessMove temp = new ChessMove(myPosition, end_pos, null);
        ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor(); // Team color
        if (board.getPiece(end_pos) != null)
        {
            // If it is yours: ignore. Else: capture and add to moves
            if (board.getPiece(end_pos).getTeamColor()!=teamColor)
            {
                move = temp;
            }
            // Add functionality to take another piece
        }
        else
        {
            move = temp;
        }
        return move;
    }
}
