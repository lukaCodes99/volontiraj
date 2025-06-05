package hr.tvz.volontiraj.service.implementation;

import hr.tvz.volontiraj.service.SupabaseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class SupabaseServiceImpl implements SupabaseService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String supabaseBucket;

    @Override
    public List<String> uploadImages(MultipartFile file) throws IOException {
        List<String> uploadedPaths = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();


            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String uploadUrl = supabaseUrl + "/storage/v1/object/" + supabaseBucket + "/" + fileName;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(file.getContentType()));
            headers.setBearerAuth(supabaseKey);

            HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);
            ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.PUT, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                uploadedPaths.add(fileName);
            } else {
                throw new RuntimeException("Upload failed: " + response.getBody());
            }


        return uploadedPaths;
    }
}
