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
        board.resetBoard();
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
        test_game.setTeamTurn(teamTurn);

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
        // If empty, return null
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
        test_game.setTeamTurn(teamTurn);

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
        test_game.setTeamTurn(teamTurn);

        // Check if the new move in the test game puts us in checkmate/check
        boolean check = test_game.isInCheck(test_game.getTeamTurn());
//        boolean checkmate = test_game.isInCheckmate(test_game.getTeamTurn());
        if (check)
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
        for (int i=1; i<=8; i++)
        {
            for (int j=1; j<=8; j++)
            {
                // Create a current position and current piece
                ChessPosition current_pos = new ChessPosition(i, j);
                ChessPiece current_piece = board.getPiece(current_pos);
                if (current_piece!=null) {
                    // Make sure that the color of the piece we are checking is an enemy
                    if (current_piece.getTeamColor() != teamColor) {
                        // If the team color is white, we need to see if the white king is in check.
                        if (teamColor == TeamColor.WHITE)
                        {
                            ChessMove test_move = new ChessMove(current_pos, kingLocationWhite, null);
                            // If the piece is a pawn, we want to add a promotion piece in there so we can check
                            if (current_piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                                if (test_move.getEndPosition().getRow()==1)
                                {
                                    test_move = new ChessMove(current_pos, kingLocationWhite, ChessPiece.PieceType.QUEEN);
                                }

                            }
                            // If the piece moves contains the test move, then we have a check!
                            if (current_piece.pieceMoves(board, current_pos).contains(test_move)) {
                                return true;
                            }

                        }
                        // If the team color is black, we need to see if the black king is in check.
                        if (teamColor == TeamColor.BLACK) {
                            ChessMove test_move = new ChessMove(current_pos, kingLocationBlack, null);
                            // If the piece is a pawn, we want to add a promotion piece in there so we can check
                            if (current_piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                                if (test_move.getEndPosition().getRow()==8)
                                {
                                    test_move = new ChessMove(current_pos, kingLocationBlack, ChessPiece.PieceType.QUEEN);
                                }
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
    public boolean isInCheckmate(TeamColor teamColor)
    {
        // If the piece is in check, see if there are any moves
        if (isInCheck(teamColor))
        {
            return noMoves(teamColor);
        }
        // If the piece is not in check, it cannot be in checkmate
        else
        {
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor)
    {
        // if the piece is in check, it cannot be in stalemate
        if (isInCheck(teamColor))
        {
            return false;
        }
        // If the piece is not in check, see if there are any moves
        else
        {
            return noMoves(teamColor);
        }
    }
    private boolean noMoves(TeamColor teamColor)
    {
        // Parse through al board locations
        for (int i=1; i<=8; i++)
        {
            for (int j=1; j<=8; j++)
            {
                // Get the piece at the current location
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = this.board.getPiece(position);
                // If the piece is not null, and it has the same team color, we can see if it have valid moves
                if ((piece != null) && (piece.getTeamColor()==teamColor))
                {
                    // Check to see all moves it can make
                    Collection<ChessMove> valid_moves = piece.pieceMoves(board, position);
                    // If there are moves, we can check them
                    if (valid_moves != null)
                    {
                        // Check all moves to see if they will get us out of check
                        for (var move : valid_moves) {
                            // Initialize a test board to test the new moves
                            ChessBoard testBoard = board.clone();
                            ChessGame testGame = new ChessGame();
                            testGame.setBoard(testBoard);
                            testGame.setTeamTurn(piece.getTeamColor());
                            // Try to make the move on the test board
                            try
                            {
                                testGame.makeMove(move);
                                // If the move does not place us in check, we are not in checkmate
                                if (!testGame.isInCheck(teamColor))
                                {
                                    return false;
                                }
                            }
                            // An invalid move does not affect us here, we just want to continue.
                            catch (InvalidMoveException _) {}


                        } // end for loop moves
                    } // end if statement valid moves not null
                } // end if statement piece not null, team color same
            } // end j for loop
        } // end i for loop
        // If we do not find a move we can make, there are no moves available
        return true;
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
                // Check to make sure the piece is not null
                if (current_piece != null) {
                    // Check to see if this piece is a king
                    if (current_piece.getPieceType() == ChessPiece.PieceType.KING) {
                        // If it is a king, we need to make change the king location
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
