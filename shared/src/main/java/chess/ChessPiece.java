package chess;

import chess.pieceMoves.BishopMovesCalculator;
import chess.pieceMoves.RookMovesCalculator;
import chess.pieceMoves.KnightMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type)
    {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor()
    {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType()
    {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        Collection<ChessMove> moves;
        switch(board.getPiece(myPosition).getPieceType())
        {
            case ChessPiece.PieceType.BISHOP-> {
                BishopMovesCalculator bishopCalc = new BishopMovesCalculator();
                moves = bishopCalc.pieceMoves(board, myPosition);
            }
            case ChessPiece.PieceType.KING-> {
                moves=null;
            }
            case ChessPiece.PieceType.KNIGHT-> {
                KnightMovesCalculator knightCalc = new KnightMovesCalculator();
                moves = knightCalc.pieceMoves(board, myPosition);
            }
            case ChessPiece.PieceType.PAWN-> {
                moves=null;
            }
            case ChessPiece.PieceType.QUEEN-> {
                moves=null;
            }
            case ChessPiece.PieceType.ROOK-> {
                RookMovesCalculator rookCalc = new RookMovesCalculator();
                moves = rookCalc.pieceMoves(board, myPosition);
            }
            default -> moves = null;
        }
        return moves;
    }
}
