package hr.tvz.volontiraj.repository;

import hr.tvz.volontiraj.model.Event;
import hr.tvz.volontiraj.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    @Query("SELECT e FROM UserEntity u JOIN u.attendingEvents e WHERE u.id = e.creator.id order by e.startDateTime DESC limit 5")
    List<Event> findAttendingEventsByUserId(@Param("userId") Long userId);
}
