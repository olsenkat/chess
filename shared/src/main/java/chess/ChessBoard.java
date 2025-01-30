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
        ChessPosition position = new ChessPosition(row, col);
        ChessPiece new_piece = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
        addPiece(position, new_piece);

        // Knight1
        col = 2;
        position = new ChessPosition(row, col);
        new_piece = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
        addPiece(position, new_piece);

        // Bishop1
        col = 3;
        position = new ChessPosition(row, col);
        new_piece = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
        addPiece(position, new_piece);

        // Queen
        col = 4;
        position = new ChessPosition(row, col);
        new_piece = new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN);
        addPiece(position, new_piece);

        // King
        col = 5;
        position = new ChessPosition(row, col);
        new_piece = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        addPiece(position, new_piece);

        // Bishop2
        col = 6;
        position = new ChessPosition(row, col);
        new_piece = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
        addPiece(position, new_piece);

        // Knight2
        col = 7;
        position = new ChessPosition(row, col);
        new_piece = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
        addPiece(position, new_piece);

        // Rook2
        col = 8;
        position = new ChessPosition(row, col);
        new_piece = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
        addPiece(position, new_piece);

        // Pawns
        row = 2;
        for(col=1; col<9; col++)
        {
            position = new ChessPosition(row, col);
            new_piece = new ChessPiece(teamColor, ChessPiece.PieceType.PAWN);
            addPiece(position, new_piece);
        }


        // PLace black pieces
        teamColor = ChessGame.TeamColor.BLACK;
        row = 8;
        col = 8;

        // Rook1
        position = new ChessPosition(row, col);
        new_piece = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
        addPiece(position, new_piece);

        // Knight1
        col = 7;
        position = new ChessPosition(row, col);
        new_piece = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
        addPiece(position, new_piece);

        // Bishop1
        col = 6;
        position = new ChessPosition(row, col);
        new_piece = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
        addPiece(position, new_piece);

        // King
        col = 5;
        position = new ChessPosition(row, col);
        new_piece = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        addPiece(position, new_piece);

        // Queen
        col = 4;
        position = new ChessPosition(row, col);
        new_piece = new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN);
        addPiece(position, new_piece);

        // Bishop2
        col = 3;
        position = new ChessPosition(row, col);
        new_piece = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
        addPiece(position, new_piece);

        // Knight2
        col = 2;
        position = new ChessPosition(row, col);
        new_piece = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
        addPiece(position, new_piece);

        // Rook2
        col = 1;
        position = new ChessPosition(row, col);
        new_piece = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
        addPiece(position, new_piece);

        // Pawns
        row = 7;
        for(col=8; col>0; col--)
        {
            position = new ChessPosition(row, col);
            new_piece = new ChessPiece(teamColor, ChessPiece.PieceType.PAWN);
            addPiece(position, new_piece);
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
                    ChessPosition current_pos = new ChessPosition(i,j);
                    // Locate the new piece to transfer over
                    ChessPiece old_piece = this.getPiece(current_pos);
                    if (old_piece != null)
                    {
                        // Determine the team color
                        ChessGame.TeamColor team_color = old_piece.getTeamColor();
                        // Determine the piece type
                        ChessPiece.PieceType piece_type = old_piece.getPieceType();
                        // Create the new piece and add it to the clone.
                        ChessPiece new_piece = new ChessPiece(team_color, piece_type);
                        clone.addPiece(current_pos, new_piece);
                    }
                }
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
