package hr.tvz.volontiraj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventSummaryDto {
    private Long id;
    private String title;
    private Integer upvote;
    private String category;
}
