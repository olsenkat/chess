package dataaccess;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Auth DAO Tests")
public class AuthDAOTest {
    boolean sqlDataAccess = true;
    static AuthDAO authDAO;

    String testAuthToken = AuthDAO.generateToken();
    AuthData testAuth = new AuthData(testAuthToken, "username");

    AuthDAOTest() {
        if (sqlDataAccess) {
            assertDoesNotThrow(() -> authDAO = new MySqlAuthDAO(), "AuthDAO not initialized correctly.");
        } else {
            authDAO = new MemoryAuthDAO();
        }
    }
    @Nested
    @DisplayName("Create Auth Tests")
    class CreateAuthTests {

        @BeforeEach
        void initTests() {
            assertDoesNotThrow(() -> authDAO.clear(), "We should be able to clear the Auth Data");
        }

        @Test
        void createAuthValid() {
            assertDoesNotThrow(() -> createAuth(testAuth), "Create Auth should not throw an error.");
        }

        @Test
        void createAuthDuplicate() {
            assertDoesNotThrow(() -> createAuth(testAuth), "Create Auth should not throw an error.");
            assertThrows(DataAccessException.class, () -> createAuth(testAuth),
                    "Create Duplicate Auth should throw an error.");
        }

        @Test
        void createNullAuthToken() {
            var invalidAuth = new AuthData(null, "username");
            assertThrows(DataAccessException.class, () -> createAuth(invalidAuth),
                    "Create Invalid Auth should throw an error.");
        }

        @Test
        void createNullUsername() {
            var invalidAuth = new AuthData(testAuthToken, null);
            assertThrows(DataAccessException.class, () -> createAuth(invalidAuth),
                    "Create Invalid Auth should throw an error.");
        }
    }

    @Nested
    @DisplayName("Get Auth Tests")
    class GetAuthTests {

        @BeforeEach
        void initTests() {
            assertDoesNotThrow(() -> authDAO.clear(), "We should be able to clear the Auth Data");
        }

        @Test
        void getAuthValid() {
            assertDoesNotThrow(() -> createAuth(testAuth), "Create Auth should not throw an error.");
            assertDoesNotThrow(() -> authDAO.getAuth(testAuthToken), "Get Auth should not throw an error.");
        }

        @Test
        void getWrongAuthToken() {
            assertDoesNotThrow(() -> createAuth(testAuth), "Create Auth should not throw an error.");
            assertThrows(DataAccessException.class, () -> authDAO.getAuth("InvalidToken"),
                    "Get Invalid Auth Token should throw an error.");
        }

        @Test
        void getNullAuthToken() {
            assertDoesNotThrow(() -> createAuth(testAuth), "Create Auth should not throw an error.");
            assertThrows(DataAccessException.class, () -> authDAO.getAuth(null),
                    "Get Null Auth Token should throw an error.");
        }
    }

    @Nested
    @DisplayName("Delete Auth Tests")
    class DeleteAuthTests {

        @BeforeEach
        void initTests() {
            assertDoesNotThrow(() -> authDAO.clear(), "We should be able to clear the Auth Data");
        }

        @Test
        void deleteAuthValid() {
            assertDoesNotThrow(() -> createAuth(testAuth), "Create Auth should not throw an error.");
            assertDoesNotThrow(() -> authDAO.getAuth(testAuthToken), "Get Auth should not throw an error.");
            assertDoesNotThrow(() -> authDAO.deleteAuth(testAuth), "Delete Auth should not throw an error.");
            assertThrows(DataAccessException.class, () -> authDAO.getAuth(testAuthToken),
                    "Get Deleted Auth Token should throw an error.");
        }

        @Test
        void deleteNullAuthToken() {
            assertDoesNotThrow(() -> createAuth(testAuth), "Create Auth should not throw an error.");
            assertDoesNotThrow(() -> authDAO.getAuth(testAuthToken), "Get Auth should not throw an error.");
            assertThrows(DataAccessException.class, () -> authDAO.deleteAuth(new AuthData(null,
                            "username")), "Get Deleted Auth Token should throw an error.");
        }

        @Test
        void deleteInvalidAuthToken() {
            assertDoesNotThrow(() -> createAuth(testAuth), "Create Auth should not throw an error.");
            assertDoesNotThrow(() -> authDAO.getAuth(testAuthToken), "Get Auth should not throw an error.");
            assertThrows(DataAccessException.class, () -> authDAO.deleteAuth(new AuthData("InvalidAuth",
                    "username")), "Get Deleted Auth Token should throw an error.");
        }
    }

    @Nested
    @DisplayName("Clear Auth Tests")
    class ClearAuthTest {

        @BeforeEach
        void initTests() {
            assertDoesNotThrow(() -> authDAO.clear(), "We should be able to clear the Auth Data");
        }

        @Test
        void clearAuthFull() {
            assertDoesNotThrow(() -> createAuth(testAuth), "Create Auth should not throw an error.");
            assertDoesNotThrow(() -> authDAO.getAuth(testAuthToken),
                    "Getting the created Auth should not throw an error.");
            assertDoesNotThrow(() -> authDAO.clear(), "Clear Auth should not throw an error.");
            assertThrows(DataAccessException.class, () -> authDAO.getAuth(testAuthToken),
                    "Getting the deleted Auth should throw an error.");
        }

        @Test
        void clearAuthEmpty() {
            assertDoesNotThrow(() -> authDAO.clear(), "Clear Auth should not throw an error.");
        }

    }

    void createAuth(AuthData auth) throws DataAccessException {
        authDAO.createAuth(auth);
    }
}
