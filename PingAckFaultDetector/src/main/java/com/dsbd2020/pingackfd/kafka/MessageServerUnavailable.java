package dsbd2020.pingackfd.kafka;

public class MessageServerUnavailable {
    private String serverUnavailable;
    
    public MessageServerUnavailable(String error) {
        this.serverUnavailable = error;
    }
}
