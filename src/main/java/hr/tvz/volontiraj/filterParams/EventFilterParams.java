package hr.tvz.volontiraj.filterParams;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFilterParams {
    private String category;
    private String title;
    private String description;
    private String location;
    private String startDateTimeFrom;
    private String startDateTimeTo;
    private Long creatorId;
}
