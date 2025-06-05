package hr.tvz.volontiraj.controller;

import hr.tvz.volontiraj.service.SupabaseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/api/images")
public class UploadController {

    private SupabaseService supabaseService;

    @PutMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile images) {
        try {
            List<String> uploadedPaths = supabaseService.uploadImages(images);
            return ResponseEntity.ok().body(uploadedPaths);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload images: " + e.getMessage());
        }
    }
}
