package service;

import dataaccess.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public abstract class ServiceTests {
    boolean sqlDataAccess = true;

    AuthDAO authDAO = new MemoryAuthDAO();
    UserDAO userDAO = new MemoryUserDAO();
    GameDAO gameDAO = new MemoryGameDAO();

    GameService gameService = null;
    UserService userService;
    ClearService clear;

    AuthDAO updateAuthDAO(boolean sqlDataAccess)
    {
        this.sqlDataAccess = sqlDataAccess;
        if (sqlDataAccess)
        {
            assertDoesNotThrow(() -> authDAO = new MySqlAuthDAO(), "AuthDAO not initialized correctly.");
        }
        else
        {
            authDAO = new MemoryAuthDAO();
        }
        return authDAO;
    }

    UserDAO updateUserDAO(boolean sqlDataAccess)
    {
        this.sqlDataAccess = sqlDataAccess;
        if (sqlDataAccess)
        {
            assertDoesNotThrow(() -> userDAO = new MySqlUserDAO(), "UserDAO not initialized correctly.");
        }
        else
        {
            userDAO = new MemoryUserDAO();
        }
        return userDAO;
    }

    GameDAO updateGameDAO(boolean sqlDataAccess)
    {
        this.sqlDataAccess = sqlDataAccess;
        if (sqlDataAccess)
        {
            assertDoesNotThrow(() -> gameDAO = new MySqlGameDAO(), "GameDAO not initialized correctly.");
        }
        else
        {
            gameDAO = new MemoryGameDAO();
        }
        return gameDAO;
    }

    UserService updateUserService()
    {
        userService = new UserService(userDAO, authDAO);
        return userService;
    }

    GameService updateGameService()
    {
        gameService = new GameService(authDAO, gameDAO);
        return gameService;
    }

    ClearService updateClearService()
    {
        clear = new ClearService(userDAO, authDAO, gameDAO);
        return clear;
    }
}
