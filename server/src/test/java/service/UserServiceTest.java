package service;

import dataaccess.*;
import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
import requestresult.LoginRequest;
import requestresult.LogoutRequest;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Service Tests")
class UserServiceTest {
    boolean sqlDataAccess = true;
    static AuthDAO authDAO;
    static UserDAO userDAO;
    static GameDAO gameDAO;

    static UserService userService;
    static ClearService clear;
    UserServiceTest()
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
        userService = new UserService(userDAO, authDAO);
    }


    @BeforeEach
    void initTests()
    {
        this.clear();
    }
    @Nested
    @DisplayName("Register Tests")
    class RegisterTests {

        @BeforeEach
        void initTests()
        {
            clear();
        }

        @Test
        void registerValidResponse() {
            registerAndCheckUser();
        }

        @Test
        void registerNullUsername()
        {
            assertThrows(ResponseException.class,
                    () -> userService.register(new RegisterRequest(null,
                                                          "password",
                                                             "email")));
        }

        @Test
        void registerNullPassword()
        {
            assertThrows(ResponseException.class,
                    () -> userService.register(new RegisterRequest("username",
                            null,
                            "email")));
        }

        @Test
        void registerNullEmail()
        {
            assertThrows(ResponseException.class,
                    () -> userService.register(new RegisterRequest("username",
                            "password",
                            null)));
        }

        @Test
        void registerDuplicateUser()
        {
            registerAndCheckUser();
            assertThrows(ResponseException.class,
                    () -> userService.register(new RegisterRequest("username",
                            "password",
                            "email@email.com")));
        }
    }

    @Nested
    @DisplayName("Login Tests")
    class LoginTests {

        @BeforeEach
        void initTests()
        {
            clear();
        }

        @Test
        void loginValidResponse() {
            loginUser();
        }

        @Test
        void loginNullUsername()
        {
            addUser();

            assertThrows(ResponseException.class,
                    () -> userService.login(new LoginRequest(null, "TestPassword")));
        }

        @Test
        void loginWrongUsername()
        {
            addUser();

            assertThrows(ResponseException.class,
                    () -> userService.login(new LoginRequest("FakeUser", "TestPassword")));
        }

        @Test
        void loginNullPassword()
        {
            addUser();

            assertThrows(ResponseException.class,
                    () -> userService.login(new LoginRequest("TestUsername", null)));
        }

        @Test
        void loginWrongPassword()
        {
            addUser();

            assertThrows(ResponseException.class,
                    () -> userService.login(new LoginRequest("TestUsername", "FakePassword")));
        }
    }

    @Nested
    @DisplayName("Logout Tests")
    class LogoutTests {

        @Test
        void logoutValidResponse() {
            String authToken = registerAndCheckUser();

            logoutUser(authToken);
        }

        @Test
        void logoutNullAuthToken() {
            registerAndCheckUser();

            assertThrows(ResponseException.class,
                    () -> userService.logout(new LogoutRequest(null)));
        }

        @Test
        void logoutDeletedAuthToken() {
            String authToken = registerAndCheckUser();

            logoutUser(authToken);

            assertThrows(ResponseException.class,
                    () -> userService.logout(new LogoutRequest(authToken)));
        }

        @Test
        void logoutInvalidAuthToken() {
            registerAndCheckUser();

            assertThrows(ResponseException.class,
                    () -> userService.logout(new LogoutRequest("FakeAuthToken")));
        }
    }

    /*****************************************
     Helper Functions
     ****************************************/

    // Clears the database
    private void clear()
    {
        assertDoesNotThrow(() ->
                clear.clear()
                , "No exception expected");

    }

    // Registers the user and checks it is inserted
    private String registerAndCheckUser()
    {
        String username = "username";
        String password = "password";
        String email = "email@email.com";

        RegisterResult registerResult = registerUser(username, password, email);

        checkUserInserted(username, password, email);

        assertNotNull(registerResult, "Creation Result should not be null");
        return registerResult.authToken();
    }

    // Registers the user
    private RegisterResult registerUser(String username, String password, String email)
    {
        return assertDoesNotThrow(() ->
                        userService.register(new RegisterRequest(username, password, email))
                , "No exception expected");
    }

    // Checks the user got inserted into the database
    private void checkUserInserted(String username, String password, String email)
    {
        assertDoesNotThrow(() ->
            {
                var user = userDAO.getUser(username);
                checkUserInfoEqual(user, password, email);
            },"registerUser should have inserted the user into the database");
    }

    // Checks to see if the user info matches
    private void checkUserInfoEqual(UserData user, String password, String email)
    {
        assert(BCrypt.checkpw(password, user.password())) :
                "registerUser Error: Passwords don't match";

        assert (Objects.equals(user.email(), email)) :
                "registerUser Error: Emails don't match";
    }

    // Runs the User Service login API. Fails the test if it finds a Response Exception.
    private void loginUser()
    {
        addUser();
        assertDoesNotThrow(() ->
        {
            userService.login(new LoginRequest("TestUsername", "TestPassword"));
        },"Login Response Exception not expected");
    }

    // Runs the Add User DAO. Fails the test if it finds a Data Access Exception.
    private void addUser()
    {
        assertDoesNotThrow(() ->
        {
            var encryptedPass = userService.encryptUserPassword("TestPassword");
            userDAO.createUser(new UserData("TestUsername", encryptedPass, "Test@Email.com"));
        },"No exception expected when creating user");
    }

    // Runs the User Service logout API. Fails the test if it finds a Response Exception.
    private void logoutUser(String authToken)
    {
        assertDoesNotThrow(() ->
        {
            userService.logout(new LogoutRequest(authToken));
        },"Logout Response Exception not expected");
    }
}
