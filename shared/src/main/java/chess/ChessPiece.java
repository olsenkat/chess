package chess;

import chess.piecemoves.BishopMovesCalculator;
import chess.piecemoves.RookMovesCalculator;
import chess.piecemoves.KnightMovesCalculator;
import chess.piecemoves.QueenMovesCalculator;
import chess.piecemoves.KingMovesCalculator;
import chess.piecemoves.PawnMovesCalculator;

import java.util.Collection;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
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

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
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
                KingMovesCalculator kingCalc = new KingMovesCalculator();
                moves = kingCalc.pieceMoves(board, myPosition);
            }
            case ChessPiece.PieceType.KNIGHT-> {
                KnightMovesCalculator knightCalc = new KnightMovesCalculator();
                moves = knightCalc.pieceMoves(board, myPosition);
            }
            case ChessPiece.PieceType.PAWN-> {
                PawnMovesCalculator pawnCalc = new PawnMovesCalculator();
                moves = pawnCalc.pieceMoves(board, myPosition);
            }
            case ChessPiece.PieceType.QUEEN-> {
                QueenMovesCalculator queenCalc = new QueenMovesCalculator();
                moves = queenCalc.pieceMoves(board, myPosition);
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
