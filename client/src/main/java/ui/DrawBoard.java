package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class DrawBoard
{
    private final ChessPiece[][] chessPieces;
    private final String[][] stringChessPieces;
    private final ChessGame currentGame;
    DrawBoard(ChessPiece[][] chessPieces, String[][] stringChessPieces, ChessGame currentGame)
    {
        this.chessPieces = chessPieces;
        this.stringChessPieces = stringChessPieces;
        this.currentGame = currentGame;
    }
    public void drawWhiteBoard() {
        String darkSquares = SET_BG_COLOR_PURPLE;
        String lightSquares = SET_BG_COLOR_MAGENTA;
        String boardColor = SET_BG_COLOR_LIGHT_GREY;
        boardSetup();
        String top = SET_TEXT_COLOR_WHITE + boardColor + EMPTY + "a  " + " b   " + "c " + " d" + EMPTY + "e  " + "f   " + "g  " +
                "h "  + EMPTY + SET_BG_COLOR_BLACK + "\n";

        String line8 = setLineEight(boardColor,lightSquares, darkSquares);
        String line7 = setLineSeven(boardColor,lightSquares, darkSquares);
        String line6 = setLineSix(boardColor,lightSquares, darkSquares);
        String line5 = setLineFive(boardColor,lightSquares, darkSquares);
        String line4 = setLineFour(boardColor,lightSquares, darkSquares);
        String line3 = setLineThree(boardColor,lightSquares, darkSquares);
        String line2 = setLineTwo(boardColor,lightSquares, darkSquares);
        String line1 = setLineOne(boardColor,lightSquares, darkSquares);

        System.out.println(top + line8 + line7 + line6 + line5 + line4 + line3 + line2 + line1 + top);
    }

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

    public void drawBlackBoard()
    {
        String darkSquares = SET_BG_COLOR_MAGENTA;
        String lightSquares = SET_BG_COLOR_PURPLE;
        String boardColor = SET_BG_COLOR_LIGHT_GREY;
        boardSetup();
        String top = SET_TEXT_COLOR_WHITE + boardColor + EMPTY + "h  " + " g   " + "f " + " e" + EMPTY + "d  " + "c   " + "b  " +
                "a "  + EMPTY + SET_BG_COLOR_BLACK + "\n";

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

    private void getStringPieces() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = chessPieces[i][j];
                if (piece==null)
                {
                    stringChessPieces[i][j] = EMPTY;
                    continue;
                }
                ChessPiece.PieceType type = piece.getPieceType();
                ChessGame.TeamColor color = piece.getTeamColor();
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
