package hr.tvz.volontiraj.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface HttpClient {
    ResponseEntity<String> put(String url, HttpEntity<?> requestEntity);
}
