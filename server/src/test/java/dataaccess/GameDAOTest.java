package dataaccess;
import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Game DAO Tests")
public class GameDAOTest {
    boolean sqlDataAccess = true;
    static GameDAO gameDAO;
    int gameID = 1;

    GameData testGame = new GameData(1, null, null, "GameName", new ChessGame());

    GameDAOTest() {
        if (sqlDataAccess) {
            assertDoesNotThrow(() -> gameDAO = new MySqlGameDAO(), "GameDAO not initialized correctly.");
        } else {
            gameDAO = new MemoryGameDAO();
        }
    }
    @Nested
    @DisplayName("Create Game Tests")
    class CreateGameTests {

        @BeforeEach
        void initTests() {
            assertDoesNotThrow(() -> gameDAO.clear(), "We should be able to clear the Game Data");
        }

        @Test
        void createGameValid() {
            assertDoesNotThrow(() -> gameID = createGame(testGame), "Create Game should not throw an error.");
        }

        @Test
        void createNullGameName() {
            GameData invalidGame = new GameData(1, null,
                    null, null, new ChessGame());
            assertThrows(DataAccessException.class, () -> createGame(invalidGame),
                    "Create Null Game Name should throw an error.");
        }
    }

    @Nested
    @DisplayName("Get Game Tests")
    class GetGameTests {

        @BeforeEach
        void initTests() {
            assertDoesNotThrow(() -> gameDAO.clear(), "We should be able to clear the Game Data");
        }

        @Test
        void getGameValid() {
            assertDoesNotThrow(() -> gameID = createGame(testGame), "Create Game should not throw an error.");
            assertDoesNotThrow(() -> gameDAO.getGame(gameID), "Get Game should not throw an error.");
        }

        @Test
        void getInvalidGameID() {
            assertDoesNotThrow(() -> gameID = createGame(testGame), "Create Game should not throw an error.");
            assertThrows(DataAccessException.class, () -> gameDAO.getGame(0),
                    "Get Incorrect Game ID should throw an error.");
        }
    }

    @Nested
    @DisplayName("List Game Tests")
    class ListGamesTests {

        @BeforeEach
        void initTests() {
            assertDoesNotThrow(() -> gameDAO.clear(), "We should be able to clear the Game Data");
        }

        @Test
        void listGamesValid() {
            var testGame2 = new GameData(2, null,
                    null, "GameName2", new ChessGame());
            assertDoesNotThrow(() -> gameID = createGame(testGame), "Create Game should not throw an error.");
            assertDoesNotThrow(() -> gameID = createGame(testGame2), "Create Game should not throw an error.");
            assertDoesNotThrow(() -> gameDAO.listGames(), "Get Game should not throw an error.");
        }

        @Test
        void listGamesEmpty() {
            var games = assertDoesNotThrow(() -> gameDAO.listGames(), "Get Game should not throw an error.");
            assertEquals(0, games.size(), "List Games should be empty");
        }
    }

    @Nested
    @DisplayName("Update Game Tests")
    class UpdateGamesTests {

        @BeforeEach
        void initTests() {
            assertDoesNotThrow(() -> gameDAO.clear(), "We should be able to clear the Game Data");
        }

        @Test
        void updateGamesValid() {
            var testGame2 = new GameData(1, "whiteUsername",
                    null, "GameName", new ChessGame());
            assertDoesNotThrow(() -> gameID = createGame(testGame), "Create Game should not throw an error.");
            assertDoesNotThrow(() -> gameDAO.updateGame(testGame2), "Update Game should not throw an error.");
            assertDoesNotThrow(() -> gameDAO.getGame(1), "Get Game should not throw an error.");
        }

        @Test
        void updateGamesNullGameName() {
            var testGame2 = new GameData(1, "whiteUsername",
                    null, null, new ChessGame());
            assertDoesNotThrow(() -> gameID = createGame(testGame), "Create Game should not throw an error.");
            assertThrows(DataAccessException.class,() -> gameDAO.updateGame(testGame2),
                    "Update Game should throw an error.");
        }

        @Test
        void updateGameInvalidID() {
            var testGame2 = new GameData(154, "whiteUsername",
                    null, "GameName", new ChessGame());
            assertDoesNotThrow(() -> gameID = createGame(testGame), "Create Game should not throw an error.");
            assertThrows(DataAccessException.class,() -> gameDAO.updateGame(testGame2),
                    "Update Game should throw an error.");
        }
    }

    @Nested
    @DisplayName("Clear Game Tests")
    class ClearGameTest {

        @BeforeEach
        void initTests() {
            assertDoesNotThrow(() -> gameDAO.clear(), "We should be able to clear the Game Data");
        }

        @Test
        void clearGamesFull() {
            assertDoesNotThrow(() -> createGame(testGame), "Create Game should not throw an error.");
            assertDoesNotThrow(() -> gameDAO.getGame(testGame.gameID()),
                    "Getting the created Game should not throw an error.");
            assertDoesNotThrow(() -> gameDAO.clear(), "Clear Game should not throw an error.");
            var games = assertDoesNotThrow(() -> gameDAO.listGames(), "Get Game should not throw an error.");
            assertEquals(0, games.size(), "List Games should be empty");
        }

        @Test
        void clearGamesEmpty() {
            assertDoesNotThrow(() -> gameDAO.clear(), "Clear Games should not throw an error.");
            var games = assertDoesNotThrow(() -> gameDAO.listGames(), "Get Game should not throw an error.");
            assertEquals(0, games.size(), "List Games should be empty");
        }

    }


    int createGame(GameData game) throws DataAccessException
    {
        return gameDAO.createGame(game).gameID();
    }
}
