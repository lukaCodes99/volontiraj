package hr.tvz.volontiraj.filterParams;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFilterParams {
    private String category;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startDateTimeFrom;
    private LocalDateTime startDateTimeTo;
    private Long creatorId;
}
