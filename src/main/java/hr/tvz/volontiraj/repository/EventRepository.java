package hr.tvz.volontiraj.repository;

import hr.tvz.volontiraj.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
