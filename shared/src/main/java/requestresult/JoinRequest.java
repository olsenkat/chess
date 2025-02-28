package requestresult;

public record JoinRequest (String authToken,
                           String playerColor,
                           Integer gameID) {
}
