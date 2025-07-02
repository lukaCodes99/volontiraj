package hr.tvz.volontiraj.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EVENT")
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Event {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
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
