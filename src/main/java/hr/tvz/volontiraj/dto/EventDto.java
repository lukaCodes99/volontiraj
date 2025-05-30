package hr.tvz.volontiraj.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private Long id;
    private String category;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startDateTime;
    private Integer upvote;
    private Long creatorId;
    private Integer volunteerCount;
}
