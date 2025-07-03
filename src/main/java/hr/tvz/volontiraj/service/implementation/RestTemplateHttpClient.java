package hr.tvz.volontiraj.service.implementation;

import hr.tvz.volontiraj.service.HttpClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateHttpClient implements HttpClient {

    private final RestTemplate restTemplate;

    public RestTemplateHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<String> put(String url, HttpEntity<?> requestEntity) {
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
    }
}
