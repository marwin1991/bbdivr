package pl.marwin1991.bbdivr.anchore.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class AnchoreAuthService {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "foobar";

    public <T> HttpEntity<T> addAuthHeader(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(USERNAME, PASSWORD);
        return new HttpEntity<>(body, headers);
    }
}
