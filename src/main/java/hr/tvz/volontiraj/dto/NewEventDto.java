package hr.tvz.volontiraj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    private String category;
    private String title;
    private String description;
    private String details;
    private String location;
    private String address;
    private LocalDateTime startDateTime;
    private Long creatorId;
    private List<MultipartFile> images;
}
