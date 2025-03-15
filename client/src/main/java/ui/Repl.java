package ui;

import websocket.NotificationHandler;
import websocket.messages.NotificationMessage;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.println(SET_TEXT_COLOR_MAGENTA + BLACK_KNIGHT + "Welcome to Katie's Chess Server." + BLACK_KNIGHT +
                "\n" + WHITE_KING + "Type 'help' to get started!" + WHITE_KING + SET_TEXT_COLOR_WHITE);

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_MAGENTA + result + SET_TEXT_COLOR_WHITE);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void notify(NotificationMessage notification) {
        System.out.println(SET_TEXT_COLOR_RED + notification.getMessage());
        printPrompt();
    }

    private void printPrompt() {
        System.out.print("\n" + EMPTY + ">>> " + SET_TEXT_COLOR_GREEN);
    }

}