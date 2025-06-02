package hr.tvz.volontiraj.repository;

import hr.tvz.volontiraj.model.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventImageRepository extends JpaRepository<EventImage, Long> {

    List<EventImage> findAllByEventId(Long eventId);

    EventImage findByEventId(Long eventId);
}
