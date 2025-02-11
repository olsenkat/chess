package request_result;

public record LoginResult (String username,
                           String authToken,
                           String message)
{ }
