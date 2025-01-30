package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */

public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;
    private ChessPosition kingLocationWhite;
    private ChessPosition kingLocationBlack;

    public ChessGame() {
        board = new ChessBoard();
        teamTurn = TeamColor.WHITE;
        kingLocationWhite = new ChessPosition(1,5);
        kingLocationBlack = new ChessPosition(8,5);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn()
    {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team)
    {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition)
    {
        // Get initial moves from Chess piece.
        ChessPiece currentPiece = board.getPiece(startPosition);

        // If there is no piece at this location, return null.
        if (currentPiece==null)
        {
            return null;
        }
        // Initialize current valid moves and new valid moves
        Collection<ChessMove> moves = currentPiece.pieceMoves(board, startPosition);
        Collection<ChessMove> moves_new = new ArrayList<>();

        // Initialize a test board to test the new moves
        ChessBoard testBoard = board.clone();

        // Create a new Game class to move the board
        ChessGame test_game = new ChessGame();
        test_game.setBoard(testBoard);

        // Filter for check moves
        if (moves != null) {
            for (var move : moves) {
                try {
                    test_game.makeMove(move);
                } catch (InvalidMoveException e) {
                    continue;
                }

                // Have the clone board create the move

                // If the team is not in check, accept the move.
                if (!test_game.isInCheck(teamTurn)) {
                    moves_new.add(move);
                }
            }
        }
        if (moves_new.isEmpty())
        {
            return null;
        }
        return moves_new;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException
    {
        // Initialize a test board to test the new moves
        ChessBoard testBoard = board.clone();

        // Create a new Game class to move the board
        ChessGame test_game = new ChessGame();
        test_game.setBoard(testBoard);

        // Determine the start and end position of the piece, find the current piece
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece current_piece = testBoard.getPiece(startPosition);
        // If there is no piece to move, this is an error
        if (current_piece==null)
        {
            throw new InvalidMoveException();
        }
        // If the move is not in valid moves, do not allow
        if (!board.getPiece(startPosition).pieceMoves(board, startPosition).contains(move))
        {
            throw new InvalidMoveException();
        }
        // If it is not your turn, don't move
        if (board.getPiece(startPosition).getTeamColor()!=teamTurn)
        {
            throw new InvalidMoveException();
        }

        // If the promotion piece is not null, change the piece type
        if(move.getPromotionPiece()!=null)
        {
            current_piece = new ChessPiece(current_piece.getTeamColor(), move.getPromotionPiece());
        }
        // Add the new move and remove the old move
        testBoard.addPiece(startPosition, null);
        testBoard.addPiece(endPosition, current_piece);
        test_game.setBoard(testBoard); // set the test game with the new board

        // Check if the new move in the test game puts us in checkmate/check
        boolean check = test_game.isInCheck(test_game.getTeamTurn());
        boolean checkmate = test_game.isInCheckmate(test_game.getTeamTurn());
        if (check || checkmate)
        {
            throw new InvalidMoveException();
        }
        else
        {
            // If the current piece is the king, set the position
            if (current_piece.getPieceType() == ChessPiece.PieceType.KING)
            {
                // White King
                if (current_piece.getTeamColor()==TeamColor.WHITE)
                {
                    kingLocationWhite = endPosition;
                }
                // Black King
                else
                {
                    kingLocationBlack = endPosition;
                }
            }
            // If the team color is white, switch turn to black
            if (teamTurn == TeamColor.WHITE)
            {
                this.teamTurn = TeamColor.BLACK;
            }
            // If the team color is black, switch turn to white
            else
            {
                this.teamTurn = TeamColor.WHITE;
            }
            // set the official board
            this.setBoard(testBoard);
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor)
    {
        // For loop checks all locations on chessboard
        for (int i=1; i<8; i++)
        {
            for (int j=1; j<8; j++)
            {
                // Create a current position and current piece
                ChessPosition current_pos = new ChessPosition(i, j);
                ChessPiece current_piece = board.getPiece(current_pos);
                if (current_piece!=null) {
                    // Make sure that the color of the piece we are checking is an enemy
                    if (current_piece.getTeamColor() != teamColor) {
                        // If the team color is white, we need to see if the white king is in check.
                        if (teamColor == TeamColor.WHITE) {
                            ChessPosition end_pos = kingLocationWhite; // king's position
                            ChessMove test_move = new ChessMove(current_pos, end_pos, null);
                            // If the piece is a pawn, we want to add a promotion piece in there so we can check
                            if (current_piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                                test_move = new ChessMove(current_pos, end_pos, ChessPiece.PieceType.QUEEN);
                            }
                            // If the piece moves contains the test move, then we have a check!
                            if (current_piece.pieceMoves(board, current_pos).contains(test_move)) {
                                return true;
                            }

                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return false; // Need to implement
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return false; // Need to implement
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board=board;
        // For loop checks all locations on chessboard
        for (int i=1; i<8; i++) {
            for (int j = 1; j < 8; j++) {
                // Create a current position and current piece
                ChessPosition current_pos = new ChessPosition(i, j);
                ChessPiece current_piece = board.getPiece(current_pos);
                if (current_piece != null) {
                    if (current_piece.getPieceType() == ChessPiece.PieceType.KING) {
                        if (current_piece.getTeamColor() == TeamColor.WHITE) {
                            kingLocationWhite = current_pos;
                        } else {
                            kingLocationBlack = current_pos;
                        }
                    }
                }
            }
        }

    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
