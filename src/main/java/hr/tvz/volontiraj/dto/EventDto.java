package hr.tvz.volontiraj.dto;

import hr.tvz.volontiraj.model.EventImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private Long id;
    private String category;
    private String title;
    private String description;
    private String details;
    private String location;
    private String address;
    private LocalDateTime startDateTime;
    private Integer upvote;
    private Long creatorId;
    private Integer volunteerCount;
    private List<EventImageDto> images;
}
