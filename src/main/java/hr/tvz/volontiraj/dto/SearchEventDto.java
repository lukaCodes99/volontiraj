package hr.tvz.volontiraj.dto;

import hr.tvz.volontiraj.model.EventCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchEventDto {
    private Long id;
    private EventCategory category;
    private String title;
    private String location;
    private LocalDateTime startDateTime;
    private Long creatorId;
    private String creatorProfileImageURL;
}
