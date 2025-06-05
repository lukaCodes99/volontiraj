package hr.tvz.volontiraj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    private Long id;
    private String category;
    private String title;
    private String description;
    private String location;
    private String address;
    private LocalDateTime startDateTime;
    private Long creatorId;
    private List<String> imagesURL;
}
