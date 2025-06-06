package hr.tvz.volontiraj.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EVENT")
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EventCategory category;
    private String title;
    private String description;
    private String details;
    private String location;
    private String address;
    private LocalDateTime startDateTime;
    private Integer upvote;

    @ManyToOne
    @JoinColumn(name = "creatorId")
    private UserEntity creator;

    @ManyToMany(mappedBy = "attendingEvents")
    private Set<UserEntity> volunteers;

}
