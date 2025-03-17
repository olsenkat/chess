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
        Collection<ChessMove> movesNew = new ArrayList<>();

        // Filter for check moves
        if (moves != null) {
            for (var move : moves)
            {
                // Initialize a test board to test the new moves
                ChessBoard testBoard = board.clone();

                // Create a new Game class to move the board
                ChessGame testGame = new ChessGame();
                testGame.setBoard(testBoard);

                try {
                    // Set temp team turn. Valid moves does not depend upon the team who is going.
                    testGame.setTeamTurn(testBoard.getPiece(move.getStartPosition()).getTeamColor());
                    testGame.makeMove(move);
                } catch (InvalidMoveException e) {
                    continue;
                }

                // Have the clone board create the move

                // If the team is not in check, accept the move.
                if (!testGame.isInCheck(teamTurn)) {
                    movesNew.add(move);
                }
            }
        }

        return movesNew;
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
        ChessGame testGame = new ChessGame();
        testGame.setBoard(testBoard);
        testGame.setTeamTurn(teamTurn);

        // Determine the start and end position of the piece, find the current piece
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece currentPiece = testBoard.getPiece(startPosition);
        // If there is no piece to move, this is an error
        if (currentPiece==null)
        {
            throw new InvalidMoveException("There is no piece to move.");
        }
        // If the move is not in valid moves, do not allow
        if (!board.getPiece(startPosition).pieceMoves(board, startPosition).contains(move))
        {
            throw new InvalidMoveException("This is an invalid move.");
        }
        // If it is not your turn, don't move
        if (board.getPiece(startPosition).getTeamColor()!=teamTurn)
        {
            throw new InvalidMoveException("It is not your turn.");
        }

        // If the promotion piece is not null, change the piece type
        if(move.getPromotionPiece()!=null)
        {
            currentPiece = new ChessPiece(currentPiece.getTeamColor(), move.getPromotionPiece());
        }
        // Add the new move and remove the old move
        testBoard.addPiece(startPosition, null);
        testBoard.addPiece(endPosition, currentPiece);
        testGame.setBoard(testBoard); // set the test game with the new board
        testGame.setTeamTurn(teamTurn);

        // Check if the new move in the test game puts us in checkmate/check
        boolean check = testGame.isInCheck(testGame.getTeamTurn());
//        boolean checkmate = test_game.isInCheckmate(test_game.getTeamTurn());
        if (check)
        {
            throw new InvalidMoveException("This would place you in check/checkmate");
        }
        else
        {
            // If the current piece is the king, set the position
            if (currentPiece.getPieceType() == ChessPiece.PieceType.KING)
            {
                // White King
                if (currentPiece.getTeamColor()==TeamColor.WHITE)
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
//        setKings();
        // For loop checks all locations on chessboard
        for (int i=1; i<=8; i++)
        {
            for (int j=1; j<=8; j++)
            {
                // Create a current position and current piece
                ChessPosition currentPos = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPos);

                if (checkPiecePutsInCheck(currentPiece, teamColor, currentPos))
                {
                    return true;
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
                // If the piece is not null, and it has the same team color, we can see if it has valid moves
                if(!checkIfNoMoves(piece, teamColor, position))
                {
                    return false;
                }
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
        setKings();

    }

    private void setKings()
    {
        // For loop checks all locations on chessboard
        for (int i=1; i<=8; i++) {
            for (int j = 1; j <= 8; j++) {
                // Create a current position and current piece
                ChessPosition currentPos = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPos);
                // Check to make sure the piece is not null
                if (currentPiece != null) {
                    // Check to see if this piece is a king
                    if (currentPiece.getPieceType() == ChessPiece.PieceType.KING) {
                        setKingPiece(currentPiece, currentPos);
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

    private ChessMove addPromotionWhite(ChessPiece currentPiece, ChessMove testMove, ChessPosition currentPos)
    {
        // If the piece is a pawn, we want to add a promotion piece in there so we can check
        if (currentPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if (testMove.getEndPosition().getRow()==1)
            {
                return new ChessMove(currentPos, kingLocationWhite, ChessPiece.PieceType.QUEEN);
            }

        }
        return testMove;
    }

    private ChessMove addPromotionBlack(ChessPiece currentPiece, ChessMove testMove, ChessPosition currentPos)
    {
        // If the piece is a pawn, we want to add a promotion piece in there so we can check
        if (currentPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if (testMove.getEndPosition().getRow()==8)
            {
                return new ChessMove(currentPos, kingLocationBlack, ChessPiece.PieceType.QUEEN);
            }
        }
        return testMove;
    }

    private boolean whiteInCheck(ChessGame.TeamColor teamColor, ChessPosition currentPos,
                                 ChessPiece currentPiece)
    {
        // If the team color is white, we need to see if the white king is in check.
        if (teamColor == TeamColor.WHITE)
        {
            ChessMove testMove = new ChessMove(currentPos, kingLocationWhite, null);
            testMove = addPromotionWhite(currentPiece, testMove, currentPos);
            // If the piece moves contains the test move, then we have a check!
            return currentPiece.pieceMoves(board, currentPos).contains(testMove);
        }
        return false;
    }

    private boolean blackInCheck(ChessGame.TeamColor teamColor, ChessPosition currentPos,
                                 ChessPiece currentPiece)
    {
        if (teamColor == TeamColor.BLACK) {
            ChessMove testMove = new ChessMove(currentPos, kingLocationBlack, null);
            // If the piece is a pawn, we want to add a promotion piece in there so we can check
            testMove = addPromotionBlack(currentPiece, testMove, currentPos);
            // If the piece moves contains the test move, then we have a check!
            return currentPiece.pieceMoves(board, currentPos).contains(testMove);
        }
        return false;
    }

    private boolean checkPiecePutsInCheck(ChessPiece currentPiece, ChessGame.TeamColor teamColor,
                                          ChessPosition currentPos)
    {
        if (currentPiece!=null) {
            // Make sure that the color of the piece we are checking is an enemy
            if (currentPiece.getTeamColor() != teamColor) {
                if (whiteInCheck(teamColor, currentPos, currentPiece))
                {
                    return true;
                }
                // If the team color is black, we need to see if the black king is in check.
                return blackInCheck(teamColor, currentPos, currentPiece);
            }
        }
        return false;
    }

    private boolean movesGetOutOfCheck(Collection<ChessMove> validMoves, ChessPiece piece,
                                       ChessGame.TeamColor teamColor)
    {
        // Check all moves to see if they will get us out of check
        for (var move : validMoves) {
            // Initialize a test board to test the new moves
            ChessBoard testBoard = board.clone();
            ChessGame testGame = new ChessGame();
            testGame.setBoard(testBoard);
            testGame.setTeamTurn(piece.getTeamColor());
            // Try to make the move on the test board
            try {
                testGame.makeMove(move);
                // If the move does not place us in check, we are not in checkmate
                if (!testGame.isInCheck(teamColor)) {
                    return false;
                }
            }
            // An invalid move does not affect us here, we just want to continue.
            catch (InvalidMoveException e) {
                continue;
            }
        }
        return true;
    }

    private boolean checkIfNoMoves(ChessPiece piece, ChessGame.TeamColor teamColor, ChessPosition position)
    {
        if ((piece != null) && (piece.getTeamColor()==teamColor))
        {
            // Check to see all moves it can make
            Collection<ChessMove> validMoves = piece.pieceMoves(board, position);
            // If there are moves, we can check them
            if (validMoves != null)
            {
                return movesGetOutOfCheck(validMoves, piece, teamColor);
            } // end if statement valid moves not null
        } // end if statement piece not null, team color same
        return true;
    }

    private void setKingPiece(ChessPiece currentPiece, ChessPosition currentPos)
    {
        // If it is a king, we need to make change the king location
        if (currentPiece.getTeamColor() == TeamColor.WHITE) {
            kingLocationWhite = currentPos;
        } else {
            kingLocationBlack = currentPos;
        }
    }
}
