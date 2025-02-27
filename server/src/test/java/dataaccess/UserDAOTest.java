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
    static UserDAO userDAO;

    UserData testUser = new UserData("username", "password", "email");

    UserDAOTest() {
        if (sqlDataAccess) {
            assertDoesNotThrow(() -> userDAO = new MySqlUserDAO(), "UserDAO not initialized correctly.");
        } else {
            userDAO = new MemoryUserDAO();
        }
    }
    @Nested
    @DisplayName("Create User Tests")
    class CreateUserTests {

        @BeforeEach
        void initTests() {
            assertDoesNotThrow(() -> userDAO.clear(), "We should be able to clear the User Data");
        }

        @Test
        void createUserValid() {
            assertDoesNotThrow(() -> createUser(testUser), "Create User should not throw an error.");
        }

        @Test
        void createUserDuplicate() {
            assertDoesNotThrow(() -> createUser(testUser), "Create User should not throw an error.");
            assertThrows(DataAccessException.class, () -> createUser(testUser),
                    "Create Invalid User should throw an error.");
        }

        @Test
        void createUserNullUsername() {
            var invalidUser = new UserData(null, "password", "email");
            assertThrows(DataAccessException.class, () -> createUser(invalidUser),
                    "Create Invalid User should throw an error.");
        }

        @Test
        void createUserNullPassword() {
            var invalidUser = new UserData("username", null, "email");
            assertThrows(DataAccessException.class, () -> createUser(invalidUser),
                    "Create Invalid User should not throw an error.");
        }

        @Test
        void createUserNullEmail() {
            var invalidUser = new UserData("username", "password", null);
            assertThrows(DataAccessException.class, () -> createUser(invalidUser),
                    "Create Invalid User should throw an error.");
        }
    }
    @Nested
    @DisplayName("Get User Tests")
    class GetUserTests {

        @BeforeEach
        void initTests() {
            assertDoesNotThrow(() -> userDAO.clear(), "We should be able to clear the User Data");
        }

        @Test
        void getUserValid() {
            assertDoesNotThrow(() -> createUser(testUser), "Create User should not throw an error.");
            assertDoesNotThrow(() -> userDAO.getUser(testUser.username()),
                    "Getting the created User should not throw an error.");
        }

        @Test
        void getUserNullUsername() {
            assertDoesNotThrow(() -> createUser(testUser), "Create User should not throw an error.");
            assertThrows(DataAccessException.class,() -> userDAO.getUser(null),
                    "Getting the a null user should throw an error.");
        }

        @Test
        void getUserWrongUsername() {
            assertDoesNotThrow(() -> createUser(testUser), "Create User should not throw an error.");
            assertThrows(DataAccessException.class,() -> userDAO.getUser("IncorrectUsername"),
                    "Getting the a wrong user should throw an error.");
        }
    }

    @Nested
    @DisplayName("Clear User Test")
    class ClearUserTest {

        @BeforeEach
        void initTests() {
            assertDoesNotThrow(() -> userDAO.clear(), "We should be able to clear the User Data");
        }

        @Test
        void clearUserFull() {
            assertDoesNotThrow(() -> createUser(testUser), "Create User should not throw an error.");
            assertDoesNotThrow(() -> userDAO.getUser(testUser.username()),
                    "Getting the created User should not throw an error.");
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
