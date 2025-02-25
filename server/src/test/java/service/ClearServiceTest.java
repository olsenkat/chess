package service;

import dataaccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requestresult.CreateRequest;
import requestresult.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Service Tests")
public class ClearServiceTest
{
    static MemoryAuthDAO auth = new MemoryAuthDAO();
    static MemoryUserDAO user = new MemoryUserDAO();
    static MemoryGameDAO game = new MemoryGameDAO();

    static ClearService clear = new ClearService(user, auth, game);
    static GameService gameService = new GameService(auth, game);
    static UserService userService = new UserService(user, auth);

    static String authToken = " ";

    @BeforeEach
    void addAll()
    {
        assertDoesNotThrow(() ->
        {
            // Register a user
            var user = userService.register(new RegisterRequest("username", "password", "test"));
            var authToken = user.authToken();

            // Create a few games to clear later
            gameService.create(new CreateRequest(authToken, "Game1"));
            gameService.create(new CreateRequest(authToken, "Game2"));
            gameService.create(new CreateRequest(authToken, "Game3"));
        }, "Error: Users and Games not Added");
    }

    @DisplayName("Clear Test")
    @Test
    void clear()
    {
        // Clear the database
        assertDoesNotThrow(() ->
                        clear.clear()
                , "No exception expected");


        // Ensure all the games are deleted
        assertEquals(0, game.listGames().size());

        // Ensures the user is deleted
        assertThrows(DataAccessException.class,
                () -> user.getUser("username", "password"),
                "Expected user to be deleted");

        // Ensure the current authorization is deleted
        assertThrows(DataAccessException.class,
                () -> auth.getAuth(authToken),
                "Expected this authorization to be deleted");
    }
}
