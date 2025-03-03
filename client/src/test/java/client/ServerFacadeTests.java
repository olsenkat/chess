package client;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import requestresult.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    RegisterRequest testUser = new RegisterRequest("username", "password", "email");

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        String url = "http://localhost:8080";
        facade = new ServerFacade(url);
    }

    @BeforeEach
    public void clearAll()
    {
        assertDoesNotThrow(() -> facade.clear(),
                "clear should not throw an error");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerUserValid()
    {
        register(testUser);
    }

    @Test
    public void registerUserInvalid()
    {
        RegisterRequest user = new RegisterRequest(null, "password", "email");
        assertThrows(ResponseException.class, () -> facade.registerUser(user),
                "Incorrect registerUser should throw an error");
    }

    @Test
    public void logoutUserValid()
    {
        logout(testUser);
    }

    @Test
    public void logoutUserInvalid()
    {
        register(testUser);
        LogoutRequest logout = new LogoutRequest("IncorrectToken");
        assertThrows(ResponseException.class, () -> facade.logoutUser(logout),
                "Incorrect logoutUser should throw an error");
    }

    @Test
    public void loginUserValid()
    {
        logout(testUser);
        assertDoesNotThrow(() -> facade.loginUser(new LoginRequest(testUser.username(), testUser.password())),
                "loginUser should not throw an error");

    }

    @Test
    public void loginUserInvalid()
    {
        new RegisterRequest("username", "password", "email");
        assertThrows(ResponseException.class,
                () -> facade.loginUser(new LoginRequest(null, testUser.password())),
                "Incorrect loginUser should throw an error");
    }

    @Test
    public void createGameValid()
    {
        String authToken = register(testUser);
        create(new CreateRequest(authToken, "GameName"));
    }

    @Test
    public void createGameInvalid()
    {
        register(testUser);
        assertThrows(ResponseException.class,
                () -> facade.createGame(new CreateRequest("InvalidToken", "GameName")),
                "Incorrect createGame should throw an error");
    }

    @Test
    public void listGamesValid()
    {
        String authToken = register(testUser);
        create(new CreateRequest(authToken, "GameName1"));
        create(new CreateRequest(authToken, "GameName2"));
        create(new CreateRequest(authToken, "GameName3"));
        ListResult result = assertDoesNotThrow(() -> facade.listGames(new ListRequest(authToken)),
                "listGames should not throw an error");
        assert(result.games().size()==3);
    }

    @Test
    public void listGamesInvalid()
    {
        String authToken = register(testUser);
        create(new CreateRequest(authToken, "GameName1"));
        create(new CreateRequest(authToken, "GameName2"));
        create(new CreateRequest(authToken, "GameName3"));
        assertThrows(ResponseException.class,
                () -> facade.listGames(new ListRequest("InvalidToken")),
                "Incorrect listGames should throw an error");
    }

    @Test
    public void joinGameValid()
    {
        String authToken = register(testUser);
        int id = create(new CreateRequest(authToken, "GameName"));
        assertDoesNotThrow(
                () -> facade.joinGame(new JoinRequest(authToken, "WHITE", id)),
                "joinGame should not throw an error");
    }

    @Test
    public void joinGameInvalid()
    {
        String authToken = register(testUser);
        int id = create(new CreateRequest(authToken, "GameName"));
        assertThrows(ResponseException.class,
                () -> facade.joinGame(new JoinRequest(authToken, "PINK", id)),
                "Incorrect joinGame should throw an error");
    }

    @Test
    public void clearValid()
    {
        String authToken = register(testUser);
        create(new CreateRequest(authToken, "GameName1"));
        create(new CreateRequest(authToken, "GameName2"));
        create(new CreateRequest(authToken, "GameName3"));
        assertDoesNotThrow(() -> facade.clear(),
                "clear should not throw an error");
    }

    @Test
    public void clearInvalid()
    {
        String authToken = register(testUser);
        create(new CreateRequest(authToken, "GameName1"));
        create(new CreateRequest(authToken, "GameName2"));
        create(new CreateRequest(authToken, "GameName3"));
        assertDoesNotThrow(() -> facade.clear(),
                "clear should not throw an error");
        assertThrows(ResponseException.class,
                () -> facade.loginUser(new LoginRequest(testUser.username(), testUser.password())),
                "AfterCleared, we shouldn't be able to access the old users");
        assertThrows(ResponseException.class,
                () -> facade.listGames(new ListRequest(authToken)),
                "AfterCleared, we shouldn't be able to access the auth should throw an error");
    }

    private String register(RegisterRequest user)
    {
        RegisterResult result = assertDoesNotThrow(() -> facade.registerUser(user),
                "registerUser should not throw an error");
        return result.authToken();
    }

    private void logout(RegisterRequest user)
    {
        String authToken = register(user);
        assertDoesNotThrow(() -> facade.logoutUser(new LogoutRequest(authToken)),
                "logoutUser should not throw an error");
    }

    private int create(CreateRequest game)
    {
        CreateResult result = assertDoesNotThrow(() -> facade.createGame(game),
                "createGame should not throw an error");
        return result.gameID();
    }

}
