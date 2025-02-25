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
    boolean sqlDataAccess = true;
    static AuthDAO authDAO = new MemoryAuthDAO();
    static UserDAO userDAO = new MemoryUserDAO();
    static GameDAO gameDAO = new MemoryGameDAO();
    ClearServiceTest()
    {
        if (sqlDataAccess)
        {
            assertDoesNotThrow(() -> authDAO = new MySqlAuthDAO(), "AuthDAO not initialized correctly.");
            assertDoesNotThrow(() -> userDAO = new MySqlUserDAO(), "UserDAO not initialized correctly.");
            assertDoesNotThrow(() -> gameDAO = new MySqlGameDAO(), "GameDAO not initialized correctly.");
        }
    }
    static ClearService clear = new ClearService(userDAO, authDAO, gameDAO);
    static GameService gameService = new GameService(authDAO, gameDAO);
    static UserService userService = new UserService(userDAO, authDAO);

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
