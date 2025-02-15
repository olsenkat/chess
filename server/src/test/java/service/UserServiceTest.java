package service;

import dataaccess.*;
import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.*;
import request_result.*;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Service Tests")
class UserServiceTest {
    static final MemoryAuthDAO authDAO = new MemoryAuthDAO();
    static final MemoryUserDAO userDAO = new MemoryUserDAO();
    static final MemoryGameDAO gameDAO = new MemoryGameDAO();
    static final UserService userService = new UserService(userDAO, authDAO);
    static final ClearService clear = new ClearService(userDAO, authDAO, gameDAO);
    String authToken = " ";

    @BeforeEach
    void initTests()
    {
        this.clear();
    }
    @Nested
    @DisplayName("Register Tests")
    class RegisterTests {

        @Test
        void registerValidResponse() {
            registerUser();
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
            registerUser();
            assertThrows(ResponseException.class,
                    () -> userService.register(new RegisterRequest("username",
                            "password",
                            "email@email.com")));
        }
    }

    @Nested
    @DisplayName("Login Tests")
    class LoginTests {

        @Test
        void loginValidResponse() {
            loginUser();
        }

        @Test
        @Disabled
        void loginAlreadyLoggedInUser()
        {
            loginUser();

            assertThrows(ResponseException.class,
                    () -> userService.login(new LoginRequest("TestUsername", "TestPassword")));
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

    private void clear()
    {
        clear.clear();
    }

    private void loginUser()
    {
        addUser();

        try
        {
            userService.login(new LoginRequest("TestUsername", "TestPassword"));
        }
        catch (ResponseException e)
        {
            fail("Login Response Exception not expected: " + e);
        }
    }

    private void addUser()
    {
        try
        {
            userDAO.createUser(new UserData("TestUsername", "TestPassword", "Test@Email.com"));
        }
        catch (DataAccessException e)
        {
            fail("No exception expected when creating user");
        }
    }
    private void registerUser()
    {
        String username = "username";
        String password = "password";
        String email = "email@email.com";

        RegisterResult registerResult = null;

        try {
            registerResult = userService.register(new RegisterRequest(username, password, email));
        } catch (ResponseException e) {
            fail("No exception expected");
        }

        try
        {
            var user = userDAO.getUser("username");
            if (!Objects.equals(user.password(), password))
            {
                fail("registerUser Error: Passwords don't match");
            }
            else if (!Objects.equals(user.email(), email))
            {
                fail("registerUser Error: Passwords don't match");
            }
        }
        catch (DataAccessException e)
        {
            fail("registerUser should have inserted the user into the database");
        }

        assertNotNull(registerResult, "Creation Result should not be null");
    }
}
