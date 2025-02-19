package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.deepToString(squares) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece)
    {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position)
    {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[8][8];
        // Start with white pieces
        ChessGame.TeamColor teamColor = ChessGame.TeamColor.WHITE;
        int row = 1;
        int col = 1;

        // Rook1
        getAddPiece(row, col, teamColor, ChessPiece.PieceType.ROOK);

        // Knight1
        col = 2;
        getAddPiece(row, col, teamColor, ChessPiece.PieceType.KNIGHT);

        // Bishop1
        col = 3;
        getAddPiece(row, col, teamColor, ChessPiece.PieceType.BISHOP);

        // Queen
        col = 4;
        getAddPiece(row, col, teamColor, ChessPiece.PieceType.QUEEN);

        // King
        col = 5;
        getAddPiece(row, col, teamColor, ChessPiece.PieceType.KING);

        // Bishop2
        col = 6;
        getAddPiece(row, col, teamColor, ChessPiece.PieceType.BISHOP);

        // Knight2
        col = 7;
        getAddPiece(row, col, teamColor, ChessPiece.PieceType.KNIGHT);

        // Rook2
        col = 8;
        getAddPiece(row, col, teamColor, ChessPiece.PieceType.ROOK);

        // Pawns
        row = 2;
        for(col=1; col<9; col++)
        {
            getAddPiece(row, col, teamColor, ChessPiece.PieceType.PAWN);
        }

        // Place black pieces
        teamColor = ChessGame.TeamColor.BLACK;
        row = 8;
        col = 8;

        // Rook1
        getAddPiece(row, col, teamColor, ChessPiece.PieceType.ROOK);

        // Knight1
        col = 7;
        getAddPiece(row, col, teamColor, ChessPiece.PieceType.KNIGHT);

        // Bishop1
        col = 6;
        getAddPiece(row, col, teamColor, ChessPiece.PieceType.BISHOP);

        // King
        col = 5;
        getAddPiece(row, col, teamColor, ChessPiece.PieceType.KING);

        // Queen
        col = 4;
        getAddPiece(row, col, teamColor, ChessPiece.PieceType.QUEEN);

        // Bishop2
        col = 3;
        getAddPiece(row, col, teamColor, ChessPiece.PieceType.BISHOP);

        // Knight2
        col = 2;
        getAddPiece(row, col, teamColor, ChessPiece.PieceType.KNIGHT);

        // Rook2
        col = 1;
        getAddPiece(row, col, teamColor, ChessPiece.PieceType.ROOK);

        // Pawns
        row = 7;
        for(col=8; col>0; col--)
        {
            getAddPiece(row, col, teamColor, ChessPiece.PieceType.PAWN);
        }

    }

    @Override
    public ChessBoard clone() {
        try {
            ChessBoard clone = (ChessBoard) super.clone();
            clone = new ChessBoard();
            // Copy all instances of squares
            for (int i = 1; i <= 8; i++)
            {
                for (int j = 1; j <= 8; j++)
                {
                    // Create current position to add to new board
                    ChessPosition currentPos = new ChessPosition(i,j);
                    // Locate the new piece to transfer over
                    ChessPiece oldPiece = this.getPiece(currentPos);
                    if (oldPiece != null)
                    {
                        // Determine the team color
                        ChessGame.TeamColor teamColor = oldPiece.getTeamColor();
                        // Determine the piece type
                        ChessPiece.PieceType pieceType = oldPiece.getPieceType();
                        // Create the new piece and add it to the clone.
                        ChessPiece newPiece = new ChessPiece(teamColor, pieceType);
                        clone.addPiece(currentPos, newPiece);
                    }
                }
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    private void getAddPiece(int row, int col, ChessGame.TeamColor teamColor,
                             ChessPiece.PieceType type)
    {
        ChessPosition position = new ChessPosition(row, col);
        ChessPiece newPiece = new ChessPiece(teamColor, type);
        addPiece(position, newPiece);
    }
}
