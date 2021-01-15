package dsbd2020.pingackfd.kafka;

import java.util.Map;
import java.time.Instant;

public class Message {
    private String key;
    private Map<String, String> value;
    
    public Message(String JsonResponse, String hostname) {
        this.key = "service_down";
        this.value.put("time",Long.toString(Instant.now().getEpochSecond()));
        this.value.put("status",JsonResponse);
        this.value.put("service",hostname);
    }
}
