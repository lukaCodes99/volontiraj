package hr.tvz.volontiraj.repository;

import hr.tvz.volontiraj.model.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EventImageRepository extends JpaRepository<EventImage, Long> {

    List<EventImage> findAllByEventId(Long eventId);

    Optional<EventImage> findByEventId(Long eventId);

    void deleteAllByEventId(Long eventId);

    @Query("SELECT e.imagePath FROM EventImage e WHERE e.event.id = :id")
    Optional<String> findImagePathByEventId(Long id);
}
