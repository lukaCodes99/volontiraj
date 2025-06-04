package hr.tvz.volontiraj.service;

import hr.tvz.volontiraj.model.EventImage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventImageService {

    EventImage save(EventImage eventImage);

    EventImage findById(Long id);

    List<EventImage> findAllByEventId(Long eventId);

    void deleteAllByEventId(Long eventId);

    void deleteById(Long id);

}
