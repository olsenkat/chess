package ui;

import chess.*;

import java.util.Collection;

import static ui.EscapeSequences.*;

public class DrawBoard
{
    // Set class variables
    private final ChessPiece[][] chessPieces;
    private final String[][] stringChessPieces;
    private final ChessGame currentGame;
    private String darkSquares = SET_BG_COLOR_PURPLE;
    private String lightSquares = SET_BG_COLOR_MAGENTA;
    private final String boardColor = SET_BG_COLOR_LIGHT_GREY;


    // Pass in constructor values
    DrawBoard(ChessPiece[][] chessPieces, String[][] stringChessPieces, ChessGame currentGame)
    {
        this.chessPieces = chessPieces;
        this.stringChessPieces = stringChessPieces;
        this.currentGame = currentGame;
    }

    public void drawWhiteHighlighted(ChessPosition startPos, Collection<ChessMove> validMoves)
    {
        boardSetup();
        String top = SET_TEXT_COLOR_WHITE + boardColor + EMPTY + "a  " + " b   " + "c " + " d" + EMPTY + "e  " + "f   " + "g  " +
                "h "  + EMPTY + SET_BG_COLOR_BLACK + "\n";

        StringBuilder board = new StringBuilder();
        board.append(top);

        for (int i = 8; i >= 1; i--)
        {
            board.append(boardColor).append(" ").append(i).append(" ");
            for (int j = 1; j <= 8; j++)
            {
                board = drawJArray(j, i, startPos, validMoves, board);
            }
            board.append(boardColor).append(SET_TEXT_COLOR_WHITE).append(" ");
            board.append(i).append(" ").append(SET_BG_COLOR_BLACK + "\n");
        }
        board.append(top);
        System.out.println(board);
    }

    public void drawBlackHighlighted(ChessPosition startPos, Collection<ChessMove> validMoves)
    {
        // Set up the board
        boardSetup();
        // Set top String
        String top = SET_TEXT_COLOR_WHITE + boardColor + EMPTY + "h  " + " g   " + "f " + " e" + EMPTY + "d  " + "c   " + "b  " +
                "a "  + EMPTY + SET_BG_COLOR_BLACK + "\n";

        // Set up variables to use
        StringBuilder board = new StringBuilder();
        board.append(top);

        // Loop to add all rows/columns
        for (int i = 1; i <= 8; i++)
        {
            // Append the beginning of row
            board.append(boardColor).append(" ").append(i).append(" ");
            // Add each individual column in row
            for (int j = 8; j >= 1; j--)
            {
                // Draw the column square
                board = drawJArray(j, i, startPos, validMoves, board);
            }
            // Set the text color to white.
            board.append(boardColor).append(SET_TEXT_COLOR_WHITE).append(" ");
            // Print i and set the background color to black
            board.append(i).append(" ").append(SET_BG_COLOR_BLACK + "\n");
        }
        // Append the column letters
        board.append(top);

        // Print the board
        System.out.println(board);
    }

    public StringBuilder drawJArray (int j, int i,
                                     ChessPosition startPos, Collection<ChessMove> validMoves,
                                     StringBuilder board)
    {
        String darkHighlight = SET_BG_COLOR_DARK_GREEN;
        String lightHighlight = SET_BG_COLOR_GREEN;

        boolean oddRow =  ((i % 2) != 0); // Check if the row is odd
        int iArray = i-1; // set if the i is
        String dark;
        String light;
        boolean oddCol =  ((j % 2) != 0);
        int jArray = j-1;
        ChessPosition currentPos = new ChessPosition(i, j);

        if (((startPos.getColumn() == j) && (startPos.getRow() == i)) || ((validMoves!=null) && (
                validMoves.contains(new ChessMove(startPos, currentPos, null)) ||
                        validMoves.contains(new ChessMove(startPos, currentPos, ChessPiece.PieceType.QUEEN))
        )))
        {
            dark = darkHighlight;
            light = lightHighlight;
        }
        else
        {
            dark = darkSquares;
            light = lightSquares;
        }

        if ((oddRow && oddCol) || (!oddRow && !oddCol))
        {
            board.append(dark);
            board.append(stringChessPieces[iArray][jArray]);
        }
        else
        {
            board.append(light);
            board.append(stringChessPieces[iArray][jArray]);
        }
        return board;
    }

    // Function to draw the ChessBoard with White side in front
    public void drawWhiteBoard() {
        // Set light and dark square colors, and board color
        String darkSquares = SET_BG_COLOR_PURPLE;
        String lightSquares = SET_BG_COLOR_MAGENTA;
        String boardColor = SET_BG_COLOR_LIGHT_GREY;

        // Set up board and set top and bottom strings
        boardSetup();
        String top = SET_TEXT_COLOR_WHITE + boardColor + EMPTY + "a  " + " b   " + "c " + " d" + EMPTY + "e  " + "f   " + "g  " +
                "h "  + EMPTY + SET_BG_COLOR_BLACK + "\n";

        // Set lines (White)
        String line8 = setLineEight(boardColor,lightSquares, darkSquares);
        String line7 = setLineSeven(boardColor,lightSquares, darkSquares);
        String line6 = setLineSix(boardColor,lightSquares, darkSquares);
        String line5 = setLineFive(boardColor,lightSquares, darkSquares);
        String line4 = setLineFour(boardColor,lightSquares, darkSquares);
        String line3 = setLineThree(boardColor,lightSquares, darkSquares);
        String line2 = setLineTwo(boardColor,lightSquares, darkSquares);
        String line1 = setLineOne(boardColor,lightSquares, darkSquares);

        // Print Board normally
        System.out.println(top + line8 + line7 + line6 + line5 + line4 + line3 + line2 + line1 + top);
    }

    // Sets up the board arrays
    private void boardSetup()
    {
        getPieces();
        getStringPieces();
    }

    private String setLineEight(String boardColor, String lightSquares, String darkSquares)
    {
        return boardColor + " 8 " + lightSquares +
                stringChessPieces[7][0] + darkSquares + stringChessPieces[7][1] +  lightSquares +
                stringChessPieces[7][2] + darkSquares + stringChessPieces[7][3] + lightSquares +
                stringChessPieces[7][4] + darkSquares + stringChessPieces[7][5] + lightSquares +
                stringChessPieces[7][6] + darkSquares + stringChessPieces[7][7] + boardColor +
                SET_TEXT_COLOR_WHITE + " 8 " + SET_BG_COLOR_BLACK + "\n";
    }

    private String setLineSeven(String boardColor, String lightSquares, String darkSquares)
    {
        return boardColor + " 7 " + darkSquares +
                stringChessPieces[6][0] + lightSquares + stringChessPieces[6][1] +  darkSquares +
                stringChessPieces[6][2] + lightSquares + stringChessPieces[6][3] + darkSquares +
                stringChessPieces[6][4] + lightSquares + stringChessPieces[6][5] + darkSquares +
                stringChessPieces[6][6] + lightSquares + stringChessPieces[6][7] + boardColor +
                SET_TEXT_COLOR_WHITE + " 7 " + SET_BG_COLOR_BLACK + "\n";
    }

    private String setLineSix(String boardColor, String lightSquares, String darkSquares)
    {
        return boardColor + " 6 " + lightSquares +
                stringChessPieces[5][0] + darkSquares + stringChessPieces[5][1] +  lightSquares +
                stringChessPieces[5][2] + darkSquares + stringChessPieces[5][3] + lightSquares +
                stringChessPieces[5][4] + darkSquares + stringChessPieces[5][5] + lightSquares +
                stringChessPieces[5][6] + darkSquares + stringChessPieces[5][7] + boardColor +
                SET_TEXT_COLOR_WHITE + " 6 " + SET_BG_COLOR_BLACK + "\n";
    }

    private String setLineFive(String boardColor, String lightSquares, String darkSquares)
    {
        return boardColor + " 5 " + darkSquares +
                stringChessPieces[4][0] + lightSquares + stringChessPieces[4][1] +  darkSquares +
                stringChessPieces[4][2] + lightSquares + stringChessPieces[4][3] + darkSquares +
                stringChessPieces[4][4] + lightSquares + stringChessPieces[4][5] + darkSquares +
                stringChessPieces[4][6] + lightSquares + stringChessPieces[4][7] + boardColor +
                SET_TEXT_COLOR_WHITE + " 5 " + SET_BG_COLOR_BLACK + "\n";
    }

    private String setLineFour(String boardColor, String lightSquares, String darkSquares)
    {
        return boardColor + " 4 " + lightSquares +
                stringChessPieces[3][0] + darkSquares + stringChessPieces[3][1] +  lightSquares +
                stringChessPieces[3][2] + darkSquares + stringChessPieces[3][3] + lightSquares +
                stringChessPieces[3][4] + darkSquares + stringChessPieces[3][5] + lightSquares +
                stringChessPieces[3][6] + darkSquares + stringChessPieces[3][7] + boardColor +
                SET_TEXT_COLOR_WHITE + " 4 " + SET_BG_COLOR_BLACK + "\n";
    }

    private String setLineThree(String boardColor, String lightSquares, String darkSquares)
    {
        return boardColor + " 3 " + darkSquares +
                stringChessPieces[2][0] + lightSquares + stringChessPieces[2][1] +  darkSquares +
                stringChessPieces[2][2] + lightSquares + stringChessPieces[2][3] + darkSquares +
                stringChessPieces[2][4] + lightSquares + stringChessPieces[2][5] + darkSquares +
                stringChessPieces[2][6] + lightSquares + stringChessPieces[2][7] + boardColor +
                SET_TEXT_COLOR_WHITE + " 3 " + SET_BG_COLOR_BLACK + "\n";
    }

    private String setLineTwo(String boardColor, String lightSquares, String darkSquares)
    {
        return boardColor + " 2 " + lightSquares +
                stringChessPieces[1][0] + darkSquares + stringChessPieces[1][1] +  lightSquares +
                stringChessPieces[1][2] + darkSquares + stringChessPieces[1][3] + lightSquares +
                stringChessPieces[1][4] + darkSquares + stringChessPieces[1][5] + lightSquares +
                stringChessPieces[1][6] + darkSquares + stringChessPieces[1][7] + boardColor +
                SET_TEXT_COLOR_WHITE + " 2 " + SET_BG_COLOR_BLACK + "\n";
    }

    private String setLineOne(String boardColor, String lightSquares, String darkSquares)
    {
        return boardColor + " 1 " + darkSquares +
                stringChessPieces[0][0] + lightSquares + stringChessPieces[0][1] +  darkSquares +
                stringChessPieces[0][2] + lightSquares + stringChessPieces[0][3] + darkSquares +
                stringChessPieces[0][4] + lightSquares + stringChessPieces[0][5] + darkSquares +
                stringChessPieces[0][6] + lightSquares + stringChessPieces[0][7] + boardColor +
                SET_TEXT_COLOR_WHITE + " 1 " + SET_BG_COLOR_BLACK + "\n";
    }

    // Draws the Chess Board from the black side
    public void drawBlackBoard()
    {
        // Set board color variables
        String darkSquares = SET_BG_COLOR_MAGENTA;
        String lightSquares = SET_BG_COLOR_PURPLE;
        String boardColor = SET_BG_COLOR_LIGHT_GREY;

        // Set up board and create top/bottom string
        boardSetup();
        String top = SET_TEXT_COLOR_WHITE + boardColor + EMPTY + "h  " + " g   " + "f " + " e" + EMPTY + "d  " + "c   " + "b  " +
                "a "  + EMPTY + SET_BG_COLOR_BLACK + "\n";

        // Print lines backwards
        String line8 = setLineEightBlack(boardColor,lightSquares, darkSquares);
        String line7 = setLineSevenBlack(boardColor,lightSquares, darkSquares);
        String line6 = setLineSixBlack(boardColor,lightSquares, darkSquares);
        String line5 = setLineFiveBlack(boardColor,lightSquares, darkSquares);
        String line4 = setLineFourBlack(boardColor,lightSquares, darkSquares);
        String line3 = setLineThreeBlack(boardColor,lightSquares, darkSquares);
        String line2 = setLineTwoBlack(boardColor,lightSquares, darkSquares);
        String line1 = setLineOneBlack(boardColor,lightSquares, darkSquares);

        System.out.println(top + line1 + line2 + line3 + line4 + line5 + line6 + line7 + line8 + top);
    }

    private String setLineEightBlack(String boardColor, String lightSquares, String darkSquares)
    {
        return boardColor + " 8 " + lightSquares +
                stringChessPieces[7][7] + darkSquares + stringChessPieces[7][6] +  lightSquares +
                stringChessPieces[7][5] + darkSquares + stringChessPieces[7][4] + lightSquares +
                stringChessPieces[7][3] + darkSquares + stringChessPieces[7][2] + lightSquares +
                stringChessPieces[7][1] + darkSquares + stringChessPieces[7][0] + boardColor +
                SET_TEXT_COLOR_WHITE + " 8 " + SET_BG_COLOR_BLACK + "\n";
    }

    private String setLineSevenBlack(String boardColor, String lightSquares, String darkSquares)
    {
        return boardColor + " 7 " + darkSquares +
                stringChessPieces[6][7] + lightSquares + stringChessPieces[6][6] +  darkSquares +
                stringChessPieces[6][5] + lightSquares + stringChessPieces[6][4] + darkSquares +
                stringChessPieces[6][3] + lightSquares + stringChessPieces[6][2] + darkSquares +
                stringChessPieces[6][1] + lightSquares + stringChessPieces[6][0] + boardColor +
                SET_TEXT_COLOR_WHITE + " 7 " + SET_BG_COLOR_BLACK + "\n";
    }

    private String setLineSixBlack(String boardColor, String lightSquares, String darkSquares)
    {
        return boardColor + " 6 " + lightSquares +
                stringChessPieces[5][7] + darkSquares + stringChessPieces[5][6] +  lightSquares +
                stringChessPieces[5][5] + darkSquares + stringChessPieces[5][4] + lightSquares +
                stringChessPieces[5][3] + darkSquares + stringChessPieces[5][2] + lightSquares +
                stringChessPieces[5][1] + darkSquares + stringChessPieces[5][0] + boardColor +
                SET_TEXT_COLOR_WHITE + " 6 " + SET_BG_COLOR_BLACK + "\n";
    }

    private String setLineFiveBlack(String boardColor, String lightSquares, String darkSquares)
    {
        return boardColor + " 5 " + darkSquares +
                stringChessPieces[4][7] + lightSquares + stringChessPieces[4][6] +  darkSquares +
                stringChessPieces[4][5] + lightSquares + stringChessPieces[4][4] + darkSquares +
                stringChessPieces[4][3] + lightSquares + stringChessPieces[4][2] + darkSquares +
                stringChessPieces[4][1] + lightSquares + stringChessPieces[4][0] + boardColor +
                SET_TEXT_COLOR_WHITE + " 5 " + SET_BG_COLOR_BLACK + "\n";
    }

    private String setLineFourBlack(String boardColor, String lightSquares, String darkSquares)
    {
        return boardColor + " 4 " + lightSquares +
                stringChessPieces[3][7] + darkSquares + stringChessPieces[3][6] +  lightSquares +
                stringChessPieces[3][5] + darkSquares + stringChessPieces[3][4] + lightSquares +
                stringChessPieces[3][3] + darkSquares + stringChessPieces[3][2] + lightSquares +
                stringChessPieces[3][1] + darkSquares + stringChessPieces[3][0] + boardColor +
                SET_TEXT_COLOR_WHITE + " 4 " + SET_BG_COLOR_BLACK + "\n";
    }

    private String setLineThreeBlack(String boardColor, String lightSquares, String darkSquares)
    {
        return boardColor + " 3 " + darkSquares +
                stringChessPieces[2][7] + lightSquares + stringChessPieces[2][6] +  darkSquares +
                stringChessPieces[2][5] + lightSquares + stringChessPieces[2][4] + darkSquares +
                stringChessPieces[2][3] + lightSquares + stringChessPieces[2][2] + darkSquares +
                stringChessPieces[2][1] + lightSquares + stringChessPieces[2][0] + boardColor +
                SET_TEXT_COLOR_WHITE + " 3 " + SET_BG_COLOR_BLACK + "\n";
    }

    private String setLineTwoBlack(String boardColor, String lightSquares, String darkSquares)
    {
        return boardColor + " 2 " + lightSquares +
                stringChessPieces[1][7] + darkSquares + stringChessPieces[1][6] +  lightSquares +
                stringChessPieces[1][5] + darkSquares + stringChessPieces[1][4] + lightSquares +
                stringChessPieces[1][3] + darkSquares + stringChessPieces[1][2] + lightSquares +
                stringChessPieces[1][1] + darkSquares + stringChessPieces[1][0] + boardColor +
                SET_TEXT_COLOR_WHITE + " 2 " + SET_BG_COLOR_BLACK + "\n";
    }

    private String setLineOneBlack(String boardColor, String lightSquares, String darkSquares)
    {
        return boardColor + " 1 " + darkSquares +
                stringChessPieces[0][7] + lightSquares + stringChessPieces[0][6] +  darkSquares +
                stringChessPieces[0][5] + lightSquares + stringChessPieces[0][4] + darkSquares +
                stringChessPieces[0][3] + lightSquares + stringChessPieces[0][2] + darkSquares +
                stringChessPieces[0][1] + lightSquares + stringChessPieces[0][0] + boardColor +
                SET_TEXT_COLOR_WHITE + " 1 " + SET_BG_COLOR_BLACK + "\n";
    }

    // Creates an array with all the chess pieces
    private void getPieces() {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece currentPiece = currentGame.getBoard().getPiece(new ChessPosition(i, j));
                if (currentPiece != null) {
                    chessPieces[i-1][j-1] = currentPiece;
                }
            }
        }
    }

    // Updates the chess pieces to strings
    private void getStringPieces() {
        // Loop through entire board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Get piece
                ChessPiece piece = chessPieces[i][j];
                // If the piece is null, the space is empty
                if (piece==null)
                {
                    stringChessPieces[i][j] = EMPTY;
                    continue;
                }
                // Get the type and color
                ChessPiece.PieceType type = piece.getPieceType();
                ChessGame.TeamColor color = piece.getTeamColor();
                // Add type and color to String array
                if (color== ChessGame.TeamColor.WHITE) {
                    if (type == ChessPiece.PieceType.PAWN)
                    {
                        stringChessPieces[i][j] = SET_TEXT_COLOR_WHITE + WHITE_PAWN;
                    }
                    else if (type == ChessPiece.PieceType.KING)
                    {
                        stringChessPieces[i][j] = SET_TEXT_COLOR_WHITE + WHITE_KING;
                    }
                    else if (type == ChessPiece.PieceType.QUEEN)
                    {
                        stringChessPieces[i][j] = SET_TEXT_COLOR_WHITE + WHITE_QUEEN;
                    }
                    else if (type == ChessPiece.PieceType.KNIGHT)
                    {
                        stringChessPieces[i][j] = SET_TEXT_COLOR_WHITE + WHITE_KNIGHT;
                    }
                    else if (type == ChessPiece.PieceType.BISHOP)
                    {
                        stringChessPieces[i][j] = SET_TEXT_COLOR_WHITE + WHITE_BISHOP;
                    }
                    else if (type == ChessPiece.PieceType.ROOK)
                    {
                        stringChessPieces[i][j] = SET_TEXT_COLOR_WHITE + WHITE_ROOK;
                    }
                }
                if (color== ChessGame.TeamColor.BLACK) {
                    if (type == ChessPiece.PieceType.PAWN)
                    {
                        stringChessPieces[i][j] = SET_TEXT_COLOR_BLACK + BLACK_PAWN;
                    }
                    else if (type == ChessPiece.PieceType.KING)
                    {
                        stringChessPieces[i][j] = SET_TEXT_COLOR_BLACK + BLACK_KING;
                    }
                    else if (type == ChessPiece.PieceType.QUEEN)
                    {
                        stringChessPieces[i][j] = SET_TEXT_COLOR_BLACK + BLACK_QUEEN;
                    }
                    else if (type == ChessPiece.PieceType.KNIGHT)
                    {
                        stringChessPieces[i][j] = SET_TEXT_COLOR_BLACK + BLACK_KNIGHT;
                    }
                    else if (type == ChessPiece.PieceType.BISHOP)
                    {
                        stringChessPieces[i][j] = SET_TEXT_COLOR_BLACK + BLACK_BISHOP;
                    }
                    else if (type == ChessPiece.PieceType.ROOK)
                    {
                        stringChessPieces[i][j] = SET_TEXT_COLOR_BLACK + BLACK_ROOK;
                    }
                }
            }
        }
    }
}
