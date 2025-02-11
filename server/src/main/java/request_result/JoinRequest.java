package request_result;

public record JoinRequest (String authToken,
                           String playerColor,
                           Integer gameID) {
}
