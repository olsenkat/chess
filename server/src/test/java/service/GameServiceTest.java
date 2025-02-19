package service;

import dataaccess.*;
import exception.ResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import request_result.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Game Service Tests")
class GameServiceTest {
    // Initialize all class variables
    static final MemoryAuthDAO authDAO = new MemoryAuthDAO();
    static final MemoryUserDAO userDAO = new MemoryUserDAO();
    static final MemoryGameDAO gameDAO = new MemoryGameDAO();
    static final GameService gameService = new GameService(authDAO, gameDAO);
    static final UserService userService = new UserService(userDAO, authDAO);
    static final ClearService clear = new ClearService(userDAO, authDAO, gameDAO);
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
            gameDAO.clear();
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
        clear.clear();
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
