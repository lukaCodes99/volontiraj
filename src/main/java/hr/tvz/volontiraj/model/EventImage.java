package hr.tvz.volontiraj.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EVENT_IMAGE")
public class EventImage {
//ovo će se pozvati posebno da se resursi ne troše bezveze!

    public EventImage(String imagePath, Event event) {
        this.imagePath = imagePath;
        this.event = event;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "eventId")
    private Event event;
}

