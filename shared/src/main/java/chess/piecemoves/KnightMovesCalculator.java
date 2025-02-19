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
        final int CHESS_WIDTH = 8; // Width of board
        int chessDown = myPosition.getRow()- 1; // Chess tiles below this piece
        int chessUp = CHESS_WIDTH - myPosition.getRow(); // Chess tiles above this piece
        int chessLeft = myPosition.getColumn() - 1; // Chess tiles to the left of this piece
        int chessRight = CHESS_WIDTH - myPosition.getColumn(); // Chess tiles to the right of this piece
        ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor(); // Team color

        // Determine available spaces right (then up)
        if ((chessRight >=2) && (chessUp>=1))
        {
            // Get the end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()+2);
            ChessMove temp = addMove(board, myPosition, teamColor, endPos);
            if (temp!=null)
            {
                moves.add(temp);
            }
        }
        // Determine available spaces right (then down)
        if ((chessRight >=2) && (chessDown>=1))
        {
            // Get the end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()+2);
            ChessMove temp = addMove(board, myPosition, teamColor, endPos);
            if (temp!=null)
            {
                moves.add(temp);
            }
        }


        // Determine available spaces left (then up)
        if ((chessLeft >=2) && (chessUp>=1))
        {
            // Get the end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()-2);
            ChessMove temp = addMove(board, myPosition, teamColor, endPos);
            if (temp!=null)
            {
                moves.add(temp);
            }
        }
        // Determine available spaces left (then down)
        if ((chessLeft >=2) && (chessDown>=1))
        {
            // Get the end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()-2);
            ChessMove temp = addMove(board, myPosition, teamColor, endPos);
            if (temp!=null)
            {
                moves.add(temp);
            }
        }

        // Determine available spaces up (then right)
        if ((chessUp >=2) && (chessRight>=1))
        {
            // Get the end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()+2,myPosition.getColumn()+1);
            ChessMove temp = addMove(board, myPosition, teamColor, endPos);
            if (temp!=null)
            {
                moves.add(temp);
            }
        }
        // Determine available spaces up (then left)
        if ((chessUp >=2) && (chessLeft>=1))
        {
            // Get the end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()+2,myPosition.getColumn()-1);
            ChessMove temp = addMove(board, myPosition, teamColor, endPos);
            if (temp!=null)
            {
                moves.add(temp);
            }
        }

        // Determine available spaces down (then right)
        if ((chessDown >=2) && (chessRight>=1))
        {
            // Get the end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()-2,myPosition.getColumn()+1);
            ChessMove temp = addMove(board, myPosition, teamColor, endPos);
            if (temp!=null)
            {
                moves.add(temp);
            }
        }
        // Determine available spaces down (then left)
        if ((chessDown >=2) && (chessLeft>=1))
        {
            // Get the end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()-2,myPosition.getColumn()-1);
            ChessMove temp = addMove(board, myPosition, teamColor, endPos);
            if (temp!=null)
            {
                moves.add(temp);
            }
        }

        return moves;
    }

    // Add move checks to see if the move is a valid move. If not, it returns null
    private ChessMove addMove(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor teamColor, ChessPosition endPos)
    {
        // Get the end position and move to add;
        ChessMove temp = new ChessMove(myPosition, endPos, null);
        if (board.getPiece(endPos) != null)
        {
            // If it is yours: ignore. Else: capture and add to moves
            if (board.getPiece(endPos).getTeamColor()!=teamColor)
            {
                return temp;
            }
            // Add functionality to take another piece
        }
        else
        {
            return temp;
        }
        return null;
    }
}
