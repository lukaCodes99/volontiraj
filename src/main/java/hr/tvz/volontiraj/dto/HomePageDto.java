package hr.tvz.volontiraj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomePageDto {
    private Long id;
    private String title;
    private String imagePath;
    private String location;
    private LocalDateTime startDateTime;
}
