package service;

import dataaccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requestresult.CreateRequest;
import requestresult.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Clear Service Tests")
public class ClearServiceTest extends ServiceTests {
    boolean sqlDataAccess = true;

    // Create auth data access
    AuthDAO authDAO;

    // Create user data access
    UserDAO userDAO;

    // Create game data access
    GameDAO gameDAO;

    // Create game service
    GameService gameService;

    // Create user Service
    UserService userService;

    // Create clear service
    ClearService clear;

    ClearServiceTest()
    {
        // Update all class DAO variables
        authDAO = updateAuthDAO(sqlDataAccess);
        userDAO = updateUserDAO(sqlDataAccess);
        gameDAO = updateGameDAO(sqlDataAccess);

        // Update all class Service variables
        gameService = updateGameService();
        userService = updateUserService();
        clear = updateClearService();
    }

    static String authToken = " ";

    @BeforeEach
    void addAll()
    {
        assertDoesNotThrow(() ->
        {
            // Register a user
            var user = userService.register(new RegisterRequest("username", "password", "test"));
            var authToken = user.authToken();

            assertDoesNotThrow(() ->
            {
                // Create a few games to clear later
                gameService.create(new CreateRequest(authToken, "Game1"));
                gameService.create(new CreateRequest(authToken, "Game2"));
                gameService.create(new CreateRequest(authToken, "Game3"));
            }, "Error: Games not Added");
        }, "Error: User/Games not Added");
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
        assertDoesNotThrow(() -> assertEquals(0, gameDAO.listGames().size()));

        // Ensures the user is deleted
        assertThrows(DataAccessException.class,
                () -> userDAO.getUser("username"),
                "Expected user to be deleted");

        // Ensure the current authorization is deleted
        assertThrows(DataAccessException.class,
                () -> authDAO.getAuth(authToken),
                "Expected this authorization to be deleted");
    }
}
