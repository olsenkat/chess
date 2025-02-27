package service;

import dataaccess.*;
import exception.ResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import requestresult.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Game Service Tests")
class GameServiceTest {
    boolean sqlDataAccess = true;
    static AuthDAO authDAO = new MemoryAuthDAO();
    static UserDAO userDAO = new MemoryUserDAO();
    static GameDAO gameDAO = new MemoryGameDAO();

    static GameService gameService;
    static UserService userService;
    static ClearService clear;
    GameServiceTest()
    {
        if (sqlDataAccess)
        {
            assertDoesNotThrow(() -> authDAO = new MySqlAuthDAO(), "AuthDAO not initialized correctly.");
            assertDoesNotThrow(() -> userDAO = new MySqlUserDAO(), "UserDAO not initialized correctly.");
            assertDoesNotThrow(() -> gameDAO = new MySqlGameDAO(), "GameDAO not initialized correctly.");
        }
        else
        {
            authDAO = new MemoryAuthDAO();
            userDAO = new MemoryUserDAO();
            gameDAO = new MemoryGameDAO();
        }
        clear = new ClearService(userDAO, authDAO, gameDAO);
        gameService = new GameService(authDAO, gameDAO);
        userService = new UserService(userDAO, authDAO);
    }

    String authToken = " ";


    @BeforeEach
    void initTests()
    {
        this.clear();
        this.registerUser();
    }


    @Nested
    @DisplayName("Create Tests")
    class CreateTests {

        @BeforeEach
        void initTests()
        {
            clear();
            registerUser();
        }

        @Test
        void createValidResponse() {
            createGame();
        }

        @Test
        void createInvalidAuth()
        {
            assertThrows(ResponseException.class,
                         () -> gameService.create(new CreateRequest("IncorrectToken",
                                                                    "new game")));
        }

        @Test
        void createNullGameName()
        {
            assertThrows(ResponseException.class,
                    () -> gameService.create(new CreateRequest(authToken,
                            null)));
        }

        @Test
        void createNullAuthToken()
        {
            assertThrows(ResponseException.class,
                    () -> gameService.create(new CreateRequest(null,
                            "Game Name")));
        }

    }

    @Nested
    @DisplayName("Join Tests")
    class JoinTests
    {

        @BeforeEach
        void initTests()
        {
            clear();
            registerUser();
        }

        @Test
        void joinValidResponseWhite() {
            Integer gameID = createGame();
            JoinResult joinResult = assertDoesNotThrow(() ->
                gameService.join(new JoinRequest(authToken, "White", gameID)),
                "No exception expected");

            assertNotNull(joinResult, "Creation Result should not be null");
        }

        @Test
        void joinValidResponseBlack() {
            Integer gameID = createGame();
            JoinResult joinResult = assertDoesNotThrow(() ->
                    gameService.join(new JoinRequest(authToken, "Black", gameID)),
                    "No exception expected");

            assertNotNull(joinResult, "Creation Result should not be null");
        }

        @Test
        void joinNullGameID() {
            createGame();
            assertThrows(ResponseException.class,
                    () -> gameService.join(new JoinRequest(authToken, "White",
                            null)));
        }

        @Test
        void joinInvalidGameID() {
            createGame();
            assertThrows(ResponseException.class,
                    () -> gameService.join(new JoinRequest(authToken, "White",
                            0)));
        }

        @Test
        void joinNullPlayerColor() {
            Integer gameID = createGame();
            assertThrows(ResponseException.class,
                    () -> gameService.join(new JoinRequest(authToken, null,
                            gameID)));
        }

        @Test
        void joinInvalidPlayerColor() {
            Integer gameID = createGame();
            assertThrows(ResponseException.class,
                    () -> gameService.join(new JoinRequest(authToken, "Pink",
                            gameID)));
        }

        @Test
        void joinNullAuthToken() {
            Integer gameID = createGame();
            assertThrows(ResponseException.class,
                    () -> gameService.join(new JoinRequest(null, "Black",
                            gameID)));
        }

    }

    @Nested
    @DisplayName("List Tests")
    class ListTests
    {

        @BeforeEach
        void initTests()
        {
            clear();
            registerUser();
        }

        @Test
        void listValidResponseFull() {
            createGame();
            ListResult listResult = assertDoesNotThrow(() ->
                    gameService.list(new ListRequest(authToken)),
                    "No exception expected");

            assertFalse(listResult.games().isEmpty(), "Game list should NOT be empty");
            assertNotNull(listResult, "List Result should not be null");
        }

        @Test
        void listValidResponseEmpty() {
            assertDoesNotThrow(() -> gameDAO.clear(), "Clear function should not throw anything");

            ListResult listResult = assertDoesNotThrow(() ->
                    gameService.list(new ListRequest(authToken)),
                    "No exception expected");


            assertTrue(listResult.games().isEmpty(), "Game list should be empty");
            assertNotNull(listResult, "List Result should not be null");
        }

        @Test
        void listIncorrectAuth() {
            createGame();

            assertThrows(ResponseException.class,
                    () -> gameService.list(new ListRequest("Incorrect")));
        }

        @Test
        void listNullAuth() {
            createGame();

            assertThrows(ResponseException.class,
                    () -> gameService.list(new ListRequest(null)));
        }

    }


    /**
     * Helper Functions
     * **/

    void clear()
    {
        assertDoesNotThrow(() ->
                        clear.clear()
                , "No exception expected");

    }

    void registerUser()
    {
        assertDoesNotThrow(() ->
            {
                // Register a user
                var user = userService.register(new RegisterRequest("username", "password", "test"));
                authToken = user.authToken();
            }, "User Registration failed");
    }

    Integer createGame()
    {
        CreateResult createResult = assertDoesNotThrow(() ->
                gameService.create(new CreateRequest(authToken, "new game")),
                "No exception expected");

        assertNotNull(createResult, "Creation Result should not be null");
        return createResult.gameID();
    }
}
