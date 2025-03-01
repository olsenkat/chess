package chess.piecemoves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator
{
    ArrayList<ChessMove> moves;
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        // Initialize variables used
        moves = new ArrayList<>();
        final int chessWidth = 8; // Width of board
        int chessDown = myPosition.getRow()- 1; // Chess tiles below this piece
        int chessUp = chessWidth - myPosition.getRow(); // Chess tiles above this piece
        int chessLeft = myPosition.getColumn() - 1; // Chess tiles to the left of this piece
        int chessRight = chessWidth - myPosition.getColumn(); // Chess tiles to the right of this piece
        ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor(); // Team Color

        if (teamColor == ChessGame.TeamColor.WHITE)
        {
            // Move pawn up
            if (chessUp>=1)
            {
                ChessPosition endPos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                if ((endPos.getRow()==8))
                {
                    addPromotionForward(board, myPosition, endPos);
                }
                else
                {
                    addMoveIfSpaceEmpty(board, endPos, myPosition);
                }
            }
            // Pawn can move 2 when in starting position
            if ((myPosition.getRow()==2))
            {
                ChessPosition tempEndPos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                ChessPosition endPos = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn());
                addMoveIfSpaceFull(board, myPosition, endPos, tempEndPos);
            }

            // Move pawn right/up diagonal
            if ((chessUp>=1) && (chessRight>=1)) {
                ChessPosition endPos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
                if ((endPos.getRow()==8)) {
                    addPromotionDiagonal(board, myPosition, endPos, teamColor);
                }
                else {
                    getMoveDiagonal(board, endPos, myPosition, teamColor);
                }
            }
            // Move pawn left/up diagonal
            if ((chessLeft>=1) && (chessUp>=1)) {
                ChessPosition endPos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
                if ((endPos.getRow()==8)) {
                    addPromotionDiagonal(board, myPosition, endPos, teamColor);
                }
                else {
                    getMoveDiagonal(board, endPos, myPosition, teamColor);
                }
            }
        }
        if (teamColor== ChessGame.TeamColor.BLACK) {
            // Move pawn down
            if (chessDown>=1) {
                ChessPosition endPos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                if ((endPos.getRow()==1)) {
                    addPromotionForward(board, myPosition, endPos);
                }
                else {
                    addMoveIfSpaceEmpty(board, endPos, myPosition);
                }

            }
            // Pawn can move 2 when in starting position
            if ((myPosition.getRow()==7)) {
                ChessPosition tempEndPos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                ChessPosition endPos = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn());
                addMoveIfSpaceFull(board, myPosition, endPos, tempEndPos);
            }

            // Move pawn right/down diagonal
            if ((chessDown>=1) && (chessRight>=1)) {
                ChessPosition endPos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
                if ((endPos.getRow()==1)) {
                    addPromotionDiagonal(board, myPosition, endPos, teamColor);
                }
                else {
                    getMoveDiagonal(board, endPos, myPosition, teamColor);
                }
            }

            // Move pawn left/down diagonal
            if ((chessLeft>=1) && (chessDown>=1)) {
                ChessPosition endPos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
                if ((endPos.getRow()==1)) {
                    addPromotionDiagonal(board, myPosition, endPos, teamColor);
                }
                else {
                    getMoveDiagonal(board, endPos, myPosition, teamColor);
                }
            }
        }
        return moves;
    }
    public ChessMove determineMoves(ChessBoard board, ChessPosition myPosition, ChessPosition endPos)
    {
        if (board.getPiece(endPos) != null)
        {
            return null;
        }

        // Initialize moves
        ChessMove move;

        // The pawn can't attack straight ahead.
        move = new ChessMove(myPosition, endPos, null);
        return move;
    }

    private void addPromotionForward(ChessBoard board, ChessPosition myPosition, ChessPosition endPos)
    {
        // Change promotion type
        for(var piece: ChessPiece.PieceType.values())
        {
            ChessMove temp = null;
            if ((piece!= ChessPiece.PieceType.PAWN) && (piece!= ChessPiece.PieceType.KING))
            {
                temp = new ChessMove(myPosition, endPos, piece);
            }
            if ((board.getPiece(endPos) == null) && (temp!=null))
            {
                moves.add(temp);
            }
        }
    }

    private void addPromotionDiagonal(ChessBoard board, ChessPosition myPosition,
                                      ChessPosition endPos, ChessGame.TeamColor teamColor)
    {
        // Change promotion type
        for(var piece: ChessPiece.PieceType.values())
        {
            ChessMove temp = null;
            if ((piece!= ChessPiece.PieceType.PAWN) && (piece!= ChessPiece.PieceType.KING))
            {
                temp = new ChessMove(myPosition, endPos, piece);
            }
            if ((board.getPiece(endPos) != null) && (temp!=null))
            {
                // If it is yours: ignore. Else: capture and add to moves
                if (board.getPiece(endPos).getTeamColor()!=teamColor)
                {
                    moves.add(temp);
                }
            }
        }
    }

    private void addMoveIfSpaceEmpty(ChessBoard board, ChessPosition endPos, ChessPosition myPosition)
    {
        ChessMove temp = new ChessMove(myPosition, endPos, null);
        if (board.getPiece(endPos) == null)
        {
            moves.add(temp);
        }
    }

    private void addMoveIfSpaceFull(ChessBoard board, ChessPosition myPosition,
                                    ChessPosition endPos, ChessPosition tempEndPos)
    {
        // Check if there is a move, and add
        ChessMove move = determineMoves(board, myPosition, endPos);
        if ((move!=null) && ((board.getPiece(tempEndPos) == null)))
        {
            moves.add(move);
        }
    }

    private void getMoveDiagonal(ChessBoard board, ChessPosition endPos,
                                 ChessPosition myPosition, ChessGame.TeamColor teamColor)
    {
        ChessMove temp = new ChessMove(myPosition, endPos, null);
        if (board.getPiece(endPos) != null)
        {
            // If it is yours: ignore. Else: capture and add to moves
            if (board.getPiece(endPos).getTeamColor()!=teamColor)
            {
                moves.add(temp);
            }
        }
    }
}
