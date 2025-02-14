package service;

import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exception.ResponseException;
import model.UserData;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    static final UserService service = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());

//    @BeforeEach
//    void clear() throws ResponseException
//    {
//        service.
//    }

    @Test
    void clear() throws ResponseException
    {


    }
}
