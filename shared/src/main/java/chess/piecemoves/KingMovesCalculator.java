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
        final int chessWidth = 8; // Width of board
        int chessDown = myPosition.getRow()- 1; // Chess tiles below this piece
        int chessUp = chessWidth - myPosition.getRow(); // Chess tiles above this piece
        int chessLeft = myPosition.getColumn() - 1; // Chess tiles to the left of this piece
        int chessRight = chessWidth - myPosition.getColumn(); // Chess tiles to the right of this piece

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

    public ChessMove determineMoves(ChessBoard board, ChessPosition myPosition, ChessPosition endPos)
    {
        ChessMove move = null;
        ChessMove temp = new ChessMove(myPosition, endPos, null);
        ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor(); // Team color
        if (board.getPiece(endPos) != null)
        {
            // If it is yours: ignore. Else: capture and add to moves
            if (board.getPiece(endPos).getTeamColor()!=teamColor)
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
