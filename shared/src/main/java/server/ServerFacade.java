package server;

import com.google.gson.Gson;
import exception.ResponseException;
import requestresult.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.Objects;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult registerUser(RegisterRequest req) throws ResponseException
    {
        var path = "/user";
        return this.makeRequest("POST", path, req, RegisterResult.class);
    }

    public LoginResult loginUser(LoginRequest req) throws ResponseException
    {
        var path = "/session";
        return this.makeRequest("POST", path, req, LoginResult.class);
    }

    public LogoutResult logoutUser(LogoutRequest req) throws ResponseException
    {
        var path = "/session";
        return this.makeRequest("DELETE", path, req, LogoutResult.class);
    }

    public CreateResult createGame(CreateRequest req) throws ResponseException
    {
        var path = "/game";
        return this.makeRequest("POST", path, req, CreateResult.class);
    }

    public ListResult listGames(ListRequest req) throws ResponseException
    {
        var path = "/game";
        return this.makeRequest("GET", path, req, ListResult.class);
    }

    public JoinResult joinGame(JoinRequest req) throws ResponseException
    {
        var path = "/game";
        return this.makeRequest("PUT", path, req, JoinResult.class);
    }

    public ClearResult clear() throws ResponseException
    {
        var path = "/db";
        return this.makeRequest("DELETE", path, null, ClearResult.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass)
            throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            if (Objects.equals(method, "GET"))
            {
                addHeaders(request, http);
            }
            else
            {
                http.setDoOutput(true);
                writeBody(request, http);
            }

            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            addHeaders(request, http);
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    private static void addHeaders(Object request, HttpURLConnection http)
    {
        try
        {
            Method getAuth = request.getClass().getMethod("authToken");
            Object authorization = getAuth.invoke(request);
            http.addRequestProperty("authorization",
                    (authorization instanceof String) ? (String) authorization : null);
            http.addRequestProperty("Content-Type", "application/json");
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
        {
            http.addRequestProperty("Content-Type", "application/json");
        }
    }



}
