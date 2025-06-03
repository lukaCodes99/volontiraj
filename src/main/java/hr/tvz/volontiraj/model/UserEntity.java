package hr.tvz.volontiraj.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USER_ENTITY")
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    //@Column(name = "profilePicturePath")
    private String profilePicturePath; //tu cemo dodati neke urlove u bazu koji su besplatno dostupni online kako bi imali konzistentan prikaz
    private String username;
    private String name;
    private String surname;
    private String password;
    private String email;
    private String bio;

    @ManyToMany
    @JoinTable(
            name = "ATTENDING EVENTS",
            joinColumns = @JoinColumn(name = "userEntityId"),
            inverseJoinColumns = @JoinColumn(name = "eventId"))
    Set<Event> attendingEvents;

    public UserEntity(Long id) {
        this.id = id;
    }
}
