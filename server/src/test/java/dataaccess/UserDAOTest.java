package dataaccess;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User DAO Tests")
public class UserDAOTest {
    boolean sqlDataAccess = true;
    static AuthDAO authDAO;
    static UserDAO userDAO;
    static GameDAO gameDAO;

    UserData testUser = new UserData("username", "password", "email");

    UserDAOTest() {
        if (sqlDataAccess) {
            assertDoesNotThrow(() -> authDAO = new MySqlAuthDAO(), "AuthDAO not initialized correctly.");
            assertDoesNotThrow(() -> userDAO = new MySqlUserDAO(), "UserDAO not initialized correctly.");
            assertDoesNotThrow(() -> gameDAO = new MySqlGameDAO(), "GameDAO not initialized correctly.");
        } else {
            authDAO = new MemoryAuthDAO();
            userDAO = new MemoryUserDAO();
            gameDAO = new MemoryGameDAO();
        }
    }
    @Nested
    @DisplayName("Create User Tests")
    class CreateUserTests {

        @BeforeEach
        void initTests() {
            assertDoesNotThrow(() -> authDAO.clear(), "We should be able to clear the Auth Data");
            assertDoesNotThrow(() -> userDAO.clear(), "We should be able to clear the User Data");
            assertDoesNotThrow(() -> gameDAO.clear(), "We should be able to clear the Game Data");
        }

        @Test
        void createUserValid() {
            assertDoesNotThrow(() -> createUser(testUser), "Create User should not throw an error.");
        }

        @Test
        void createUserDuplicate() {
            assertDoesNotThrow(() -> createUser(testUser), "Create User should not throw an error.");
            assertThrows(DataAccessException.class, () -> createUser(testUser), "Create User should not throw an error.");
        }

        @Test
        void createUserNullUsername() {
            var invalidUser = new UserData(null, "password", "email");
            assertThrows(DataAccessException.class, () -> createUser(invalidUser), "Create User should not throw an error.");
        }

        @Test
        void createUserNullPassword() {
            var invalidUser = new UserData("username", null, "email");
            assertThrows(DataAccessException.class, () -> createUser(invalidUser), "Create User should not throw an error.");
        }

        @Test
        void createUserNullEmail() {
            var invalidUser = new UserData("username", "password", null);
            assertThrows(DataAccessException.class, () -> createUser(invalidUser), "Create User should not throw an error.");
        }
    }
    @Nested
    @DisplayName("Get User Tests")
    class GetUserTests {

        @BeforeEach
        void initTests() {
            assertDoesNotThrow(() -> authDAO.clear(), "We should be able to clear the Auth Data");
            assertDoesNotThrow(() -> userDAO.clear(), "We should be able to clear the User Data");
            assertDoesNotThrow(() -> gameDAO.clear(), "We should be able to clear the Game Data");
        }

        @Test
        void getUserValid() {
            assertDoesNotThrow(() -> createUser(testUser), "Create User should not throw an error.");
            assertDoesNotThrow(() -> userDAO.getUser(testUser.username()), "Getting the created User should not throw an error.");
        }

        @Test
        void getUserNullUsername() {
            assertDoesNotThrow(() -> createUser(testUser), "Create User should not throw an error.");
            assertThrows(DataAccessException.class,() -> userDAO.getUser(null), "Getting the a null user should throw an error.");
        }

        @Test
        void getUserWrongUsername() {
            assertDoesNotThrow(() -> createUser(testUser), "Create User should not throw an error.");
            assertThrows(DataAccessException.class,() -> userDAO.getUser("IncorrectUsername"), "Getting the a wron user should throw an error.");
        }
    }

    @Nested
    @DisplayName("Clear User Test")
    class ClearUserTest {

        @BeforeEach
        void initTests() {
            assertDoesNotThrow(() -> authDAO.clear(), "We should be able to clear the Auth Data");
            assertDoesNotThrow(() -> userDAO.clear(), "We should be able to clear the User Data");
            assertDoesNotThrow(() -> gameDAO.clear(), "We should be able to clear the Game Data");
        }

        @Test
        void clearUserFull() {
            assertDoesNotThrow(() -> createUser(testUser), "Create User should not throw an error.");
            assertDoesNotThrow(() -> userDAO.getUser(testUser.username()), "Getting the created User should not throw an error.");
            assertDoesNotThrow(() -> userDAO.clear(), "Clear Users should not throw an error.");
        }

        @Test
        void clearUserEmpty() {
            assertDoesNotThrow(() -> userDAO.clear(), "Clear Users should not throw an error.");
        }

    }

        void createUser(UserData user) throws DataAccessException
    {
        userDAO.createUser(user);
    }
}
