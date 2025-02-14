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
            JoinResult joinResult = null;

            try {
                joinResult = gameService.join(new JoinRequest(authToken, "White", gameID));
            } catch (ResponseException e) {
                fail("No exception expected");
            }

            assertNotNull(joinResult, "Creation Result should not be null");
        }

        @Test
        void joinValidResponseBlack() {
            Integer gameID = createGame();
            JoinResult joinResult = null;

            try {
                joinResult = gameService.join(new JoinRequest(authToken, "Black", gameID));
            } catch (ResponseException e) {
                fail("No exception expected");
            }

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

    void clear()
    {
        clear.clear();
    }

    void registerUser()
    {
        try {
            // Register a user
            var user = userService.register(new RegisterRequest("username", "password", "test"));
            authToken = user.authToken();
        }
        catch (ResponseException e)
        {
            fail("User Registration failed");
        }
    }

    Integer createGame()
    {
        CreateResult createResult = null;
        try {
            createResult = gameService.create(new CreateRequest(authToken, "new game"));
        } catch (ResponseException e) {
            fail("No exception expected");
        }
        assertNotNull(createResult, "Creation Result should not be null");
        return createResult.gameID();
    }
}
