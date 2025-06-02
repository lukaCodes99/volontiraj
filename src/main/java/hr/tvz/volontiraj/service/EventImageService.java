package hr.tvz.volontiraj.service;

import hr.tvz.volontiraj.model.EventImage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventImageService {

    EventImage save(EventImage eventImage);

    EventImage findByEventId(Long eventId);

    List<EventImage> findAllByEventId(Long eventId);

}
