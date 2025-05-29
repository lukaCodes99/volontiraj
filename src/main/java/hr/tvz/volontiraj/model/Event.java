package hr.tvz.volontiraj.model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

public class Event {
    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startDateTime;
    private Integer upvote;
    private Integer confirmedArrival;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
