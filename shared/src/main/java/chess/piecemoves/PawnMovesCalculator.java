package chess.piecemoves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator
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
        ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor(); // Team Color

        if (teamColor == ChessGame.TeamColor.WHITE)
        {
            // Move pawn up
            if (chessUp>=1)
            {
                // Create the end position
                ChessPosition endPos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());

                // Determine if the piece will receive a promotion
                if ((endPos.getRow()==8))
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
                else
                {
                    ChessMove temp = new ChessMove(myPosition, endPos, null);
                    if (board.getPiece(endPos) == null)
                    {
                        moves.add(temp);
                    }
                }
            }
            // Pawn can move 2 when in starting position
            if ((myPosition.getRow()==2))
            {
                ChessPosition tempEndPos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                ChessPosition endPos = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn());
                // Check if there is a move, and add
                ChessMove move = determineMoves(board, myPosition, endPos);
                if ((move!=null) && ((board.getPiece(tempEndPos) == null)))
                {
                    moves.add(move);
                }
            }


            // Pawn can go right/up or left/up diagonal if there is an enemy nearby
            // Move pawn right/up diagonal
            if ((chessUp>=1) && (chessRight>=1))
            {
                // Create the end position
                ChessPosition endPos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
                // Check if there is a move, and add
                // Determine if the piece will receive a promotion
                if ((endPos.getRow()==8))
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
                else
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
            // Move pawn left/up diagonal
            if ((chessLeft>=1) && (chessUp>=1))
            {
                // Create the end position
                ChessPosition endPos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
                // Check if there is a move, and add
                // Determine if the piece will receive a promotion
                if ((endPos.getRow()==8))
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
                else
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
        }
        if (teamColor== ChessGame.TeamColor.BLACK)
        {
            // Move pawn down
            if (chessDown>=1)
            {
                ChessPosition endPos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                // Check if there is a move, and add

                // Determine if the piece will receive a promotion
                if ((endPos.getRow()==1))
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
                else
                {
                    ChessMove temp = new ChessMove(myPosition, endPos, null);
                    if (board.getPiece(endPos) == null)
                    {
                        moves.add(temp);
                    }
                }

            }
            // Pawn can move 2 when in starting position
            if ((myPosition.getRow()==7))
            {
                ChessPosition tempEndPos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                ChessPosition endPos = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn());
                // Check if there is a move, and add
                ChessMove move = determineMoves(board, myPosition, endPos);
                if ((move!=null) && ((board.getPiece(tempEndPos) == null)))
                {
                    moves.add(move);
                }
            }


            // Pawn can go right/up or left/up diagonal if there is an enemy nearby
            // Move pawn right/down diagonal
            if ((chessDown>=1) && (chessRight>=1))
            {
                // Create the end position
                ChessPosition endPos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
                // Check if there is a move, and add
                if ((endPos.getRow()==1))
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
                else
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
            // Move pawn left/down diagonal
            if ((chessLeft>=1) && (chessDown>=1))
            {
                // Create the end position
                ChessPosition endPos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
                // Check if there is a move, and add
                if ((endPos.getRow()==1))
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
                else
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
}
