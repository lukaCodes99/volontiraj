package hr.tvz.volontiraj.service.implementation;

import hr.tvz.volontiraj.service.SupabaseService;
import hr.tvz.volontiraj.service.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
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

    private final HttpClient httpClient;

    public SupabaseServiceImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public List<String> uploadImages(List<MultipartFile> files) throws IOException {
        try {
            List<String> uploadedPaths = new ArrayList<>();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    String uploadUrl = supabaseUrl + "/storage/v1/object/" + supabaseBucket + "/" + fileName;

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())));
                    headers.setBearerAuth(supabaseKey);

                    HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);
                    ResponseEntity<String> response = httpClient.put(uploadUrl, requestEntity);

                    if (response.getStatusCode().is2xxSuccessful()) {
                        uploadedPaths.add(uploadUrl);
                    }
                }
            }
            return uploadedPaths;
        } catch (Exception e) {
            throw new IOException("Upload failed: " + e.getMessage());
        }
    }
}
