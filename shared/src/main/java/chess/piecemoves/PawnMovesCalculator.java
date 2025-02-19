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
        int CHESS_WIDTH = 8; // Width of board
        int chess_down = myPosition.getRow()- 1; // Chess tiles below this piece
        int chess_up = CHESS_WIDTH - myPosition.getRow(); // Chess tiles above this piece
        int chess_left = myPosition.getColumn() - 1; // Chess tiles to the left of this piece
        int chess_right = CHESS_WIDTH - myPosition.getColumn(); // Chess tiles to the right of this piece
        ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor(); // Team Color

        if (teamColor == ChessGame.TeamColor.WHITE)
        {
            // Move pawn up
            if (chess_up>=1)
            {
                // Create the end position
                ChessPosition end_pos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());

                // Determine if the piece will receive a promotion
                if ((end_pos.getRow()==8))
                {
                    // Change promotion type
                    for(var piece: ChessPiece.PieceType.values())
                    {
                        ChessMove temp = null;
                        if ((piece!= ChessPiece.PieceType.PAWN) && (piece!= ChessPiece.PieceType.KING))
                        {
                            temp = new ChessMove(myPosition, end_pos, piece);
                        }
                        if ((board.getPiece(end_pos) == null) && (temp!=null))
                        {
                            moves.add(temp);
                        }
                    }
                }
                else
                {
                    ChessMove temp = new ChessMove(myPosition, end_pos, null);
                    if (board.getPiece(end_pos) == null)
                    {
                        moves.add(temp);
                    }
                }
            }
            // Pawn can move 2 when in starting position
            if ((myPosition.getRow()==2))
            {
                ChessPosition temp_end_pos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                ChessPosition end_pos = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn());
                // Check if there is a move, and add
                ChessMove move = determineMoves(board, myPosition, end_pos);
                if ((move!=null) && ((board.getPiece(temp_end_pos) == null)))
                {
                    moves.add(move);
                }
            }


            // Pawn can go right/up or left/up diagonal if there is an enemy nearby
            // Move pawn right/up diagonal
            if ((chess_up>=1) && (chess_right>=1))
            {
                // Create the end position
                ChessPosition end_pos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
                // Check if there is a move, and add
                // Determine if the piece will receive a promotion
                if ((end_pos.getRow()==8))
                {
                    // Change promotion type
                    for(var piece: ChessPiece.PieceType.values())
                    {
                        ChessMove temp = null;
                        if ((piece!= ChessPiece.PieceType.PAWN) && (piece!= ChessPiece.PieceType.KING))
                        {
                            temp = new ChessMove(myPosition, end_pos, piece);
                        }
                        if ((board.getPiece(end_pos) != null) && (temp!=null))
                        {
                            // If it is yours: ignore. Else: capture and add to moves
                            if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                            {
                                moves.add(temp);
                            }
                        }
                    }
                }
                else
                {
                    ChessMove temp = new ChessMove(myPosition, end_pos, null);
                    if (board.getPiece(end_pos) != null)
                    {
                        // If it is yours: ignore. Else: capture and add to moves
                        if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                        {
                            moves.add(temp);
                        }
                    }
                }
            }
            // Move pawn left/up diagonal
            if ((chess_left>=1) && (chess_up>=1))
            {
                // Create the end position
                ChessPosition end_pos = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
                // Check if there is a move, and add
                // Determine if the piece will receive a promotion
                if ((end_pos.getRow()==8))
                {
                    // Change promotion type
                    for(var piece: ChessPiece.PieceType.values())
                    {
                        ChessMove temp = null;
                        if ((piece!= ChessPiece.PieceType.PAWN) && (piece!= ChessPiece.PieceType.KING))
                        {
                            temp = new ChessMove(myPosition, end_pos, piece);
                        }
                        if ((board.getPiece(end_pos) != null) && (temp!=null))
                        {
                            // If it is yours: ignore. Else: capture and add to moves
                            if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                            {
                                moves.add(temp);
                            }
                        }
                    }
                }
                else
                {
                    ChessMove temp = new ChessMove(myPosition, end_pos, null);
                    if (board.getPiece(end_pos) != null)
                    {
                        // If it is yours: ignore. Else: capture and add to moves
                        if (board.getPiece(end_pos).getTeamColor()!=teamColor)
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
            if (chess_down>=1)
            {
                ChessPosition end_pos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                // Check if there is a move, and add

                // Determine if the piece will receive a promotion
                if ((end_pos.getRow()==1))
                {
                    // Change promotion type
                    for(var piece: ChessPiece.PieceType.values())
                    {
                        ChessMove temp = null;
                        if ((piece!= ChessPiece.PieceType.PAWN) && (piece!= ChessPiece.PieceType.KING))
                        {
                            temp = new ChessMove(myPosition, end_pos, piece);
                        }
                        if ((board.getPiece(end_pos) == null) && (temp!=null))
                        {
                            moves.add(temp);
                        }
                    }
                }
                else
                {
                    ChessMove temp = new ChessMove(myPosition, end_pos, null);
                    if (board.getPiece(end_pos) == null)
                    {
                        moves.add(temp);
                    }
                }

            }
            // Pawn can move 2 when in starting position
            if ((myPosition.getRow()==7))
            {
                ChessPosition temp_end_pos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                ChessPosition end_pos = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn());
                // Check if there is a move, and add
                ChessMove move = determineMoves(board, myPosition, end_pos);
                if ((move!=null) && ((board.getPiece(temp_end_pos) == null)))
                {
                    moves.add(move);
                }
            }


            // Pawn can go right/up or left/up diagonal if there is an enemy nearby
            // Move pawn right/down diagonal
            if ((chess_down>=1) && (chess_right>=1))
            {
                // Create the end position
                ChessPosition end_pos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
                // Check if there is a move, and add
                if ((end_pos.getRow()==1))
                {
                    // Change promotion type
                    for(var piece: ChessPiece.PieceType.values())
                    {
                        ChessMove temp = null;
                        if ((piece!= ChessPiece.PieceType.PAWN) && (piece!= ChessPiece.PieceType.KING))
                        {
                            temp = new ChessMove(myPosition, end_pos, piece);
                        }
                        if ((board.getPiece(end_pos) != null) && (temp!=null))
                        {
                            // If it is yours: ignore. Else: capture and add to moves
                            if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                            {
                                moves.add(temp);
                            }
                        }
                    }
                }
                else
                {
                    ChessMove temp = new ChessMove(myPosition, end_pos, null);
                    if (board.getPiece(end_pos) != null)
                    {
                        // If it is yours: ignore. Else: capture and add to moves
                        if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                        {
                            moves.add(temp);
                        }
                    }
                }
            }
            // Move pawn left/down diagonal
            if ((chess_left>=1) && (chess_down>=1))
            {
                // Create the end position
                ChessPosition end_pos = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
                // Check if there is a move, and add
                if ((end_pos.getRow()==1))
                {
                    // Change promotion type
                    for(var piece: ChessPiece.PieceType.values())
                    {
                        ChessMove temp = null;
                        if ((piece!= ChessPiece.PieceType.PAWN) && (piece!= ChessPiece.PieceType.KING))
                        {
                            temp = new ChessMove(myPosition, end_pos, piece);
                        }
                        if ((board.getPiece(end_pos) != null) && (temp!=null))
                        {
                            // If it is yours: ignore. Else: capture and add to moves
                            if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                            {
                                moves.add(temp);
                            }
                        }
                    }
                }
                else
                {
                    ChessMove temp = new ChessMove(myPosition, end_pos, null);
                    if (board.getPiece(end_pos) != null)
                    {
                        // If it is yours: ignore. Else: capture and add to moves
                        if (board.getPiece(end_pos).getTeamColor()!=teamColor)
                        {
                            moves.add(temp);
                        }
                    }
                }
            }
        }

        return moves;

    }
    public ChessMove determineMoves(ChessBoard board, ChessPosition myPosition, ChessPosition end_pos)
    {
        if (board.getPiece(end_pos) != null)
        {
            return null;
        }

        // Initialize move.s
        ChessMove move = null;
        ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor(); // Team color
        // Determine the promotion piece
        ChessPiece.PieceType promotionPiece = null;
        // Determine if the piece will receive a promotion

        // Create the Chess move
        ChessMove temp = new ChessMove(myPosition, end_pos, promotionPiece);
        // The pawn can't attack straight ahead.
        move = temp;
        return move;
    }
}
