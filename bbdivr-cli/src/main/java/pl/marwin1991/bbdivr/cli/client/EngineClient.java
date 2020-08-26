package pl.marwin1991.bbdivr.cli.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EngineClient {

    private final RestTemplate restTemplate;
    @Value("${engine.url:http://localhost:8080}")
    private String engineUrl;

    @Autowired
    public EngineClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isEnrolled() {
        String path = engineUrl + "/enroll";
        String response = restTemplate.getForObject(path, String.class);
        return response != null && response.equals("\"ENROLLED\"");
    }

    public String enroll() {
        String path = engineUrl + "/enroll";
        String response = restTemplate.postForObject(path, null, String.class);
        return response != null ? response.replace("\"", "") : null;
    }
}
