package service;

import dataaccess.*;
import exception.ResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
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

    private void clear()
    {
        clear.clear();
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
