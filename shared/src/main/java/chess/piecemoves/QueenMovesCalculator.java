package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator
{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        // Initialize variables used
        moves = new ArrayList<>();
        final int chessWidth = 8; // Width of board
        int chessDown = myPosition.getRow()- 1; // Chess tiles below this piece
        int chessUp = chessWidth - myPosition.getRow(); // Chess tiles above this piece
        int chessLeft = myPosition.getColumn() - 1; // Chess tiles to the left of this piece
        int chessRight = chessWidth - myPosition.getColumn(); // Chess tiles to the right of this piece
        ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor(); // Team color

        int positionDetermine; // Determines how many times the loop plays (how far the Bishop goes)

        // Determine available spaces right
        for (int i = 1; i <= chessRight; i++)
        {
            // Get the end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow(),myPosition.getColumn()+i);
            if (!addMove(board, myPosition, endPos, teamColor))
            {
                break;
            }
        }

        // Determine available spaces left
        for (int i = 1; i <= chessLeft; i++)
        {
            // Get end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow(),myPosition.getColumn()-i);
            if (!addMove(board, myPosition, endPos, teamColor))
            {
                break;
            }
        }

        // Determine available spaces down
        for (int i = 1; i <= chessDown; i++)
        {
            // Get end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()-i,myPosition.getColumn());
            if (!addMove(board, myPosition, endPos, teamColor))
            {
                break;
            }
        }

        // Determine available spaces up
        for (int i = 1; i <= chessUp; i++)
        {
            // Get end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()+i,myPosition.getColumn());
            if (!addMove(board, myPosition, endPos, teamColor))
            {
                break;
            }
        }

        // Determine available spaces right and up
        positionDetermine = Math.min(chessUp, chessRight);
        for (int i = 1; i <= positionDetermine; i++)
        {
            // Get the end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()+i,myPosition.getColumn()+i);
            if (!addMove(board, myPosition, endPos, teamColor))
            {
                break;
            }
        }

        // Determine available spaces right and down
        positionDetermine = Math.min(chessDown, chessRight);
        for (int i = 1; i <= positionDetermine; i++)
        {
            // Get end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()-i,myPosition.getColumn()+i);
            if (!addMove(board, myPosition, endPos, teamColor))
            {
                break;
            }
        }

        // Determine available spaces left and down
        positionDetermine = Math.min(chessDown, chessLeft);
        for (int i = 1; i <= positionDetermine; i++)
        {
            // Get end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()-i,myPosition.getColumn()-i);
            if (!addMove(board, myPosition, endPos, teamColor))
            {
                break;
            }
        }

        // Determine available spaces left and up
        positionDetermine = Math.min(chessUp, chessLeft);
        for (int i = 1; i <= positionDetermine; i++)
        {
            // Get end position and move to add;
            ChessPosition endPos = new ChessPosition(myPosition.getRow()+i,myPosition.getColumn()-i);
            if (!addMove(board, myPosition, endPos, teamColor))
            {
                break;
            }
        }

        return moves;
    }
}
