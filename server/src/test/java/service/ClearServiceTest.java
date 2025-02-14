package service;

import dataaccess.*;
import exception.ResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request_result.CreateRequest;
import request_result.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Service Tests")
public class ClearServiceTest
{
    static final MemoryAuthDAO auth = new MemoryAuthDAO();
    static final MemoryUserDAO user = new MemoryUserDAO();
    static final MemoryGameDAO game = new MemoryGameDAO();

    static final ClearService clear = new ClearService(user, auth, game);
    static final GameService gameService = new GameService(auth, game);
    static final UserService userService = new UserService(user, auth);

    static String authToken = " ";

    @BeforeEach
    void addAll()
    {
        try {
            // Register a user
            var user = userService.register(new RegisterRequest("username", "password", "test"));
            var authToken = user.authToken();

            // Create a few games to clear later
            gameService.create(new CreateRequest(authToken, "Game1"));
            gameService.create(new CreateRequest(authToken, "Game2"));
            gameService.create(new CreateRequest(authToken, "Game3"));
        }
        catch (ResponseException e)
        {
            fail();
        }
    }

    @DisplayName("Clear Test")
    @Test
    void clear()
    {
        // Clear the database
        clear.clear();

        // Ensure all the games are deleted
        assertEquals(0, game.listGames().size());

        // Ensures the user is deleted
        assertThrows(DataAccessException.class,
                () -> user.getUser("username"),
                "Expected user to be deleted");

        // Ensure the current authorization is deleted
        assertThrows(DataAccessException.class,
                () -> auth.getAuth(authToken),
                "Expected this authorization to be deleted");
    }
}
