package hr.tvz.volontiraj.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface SupabaseService {
    List<String> uploadImages(List<MultipartFile> file) throws IOException;
}
