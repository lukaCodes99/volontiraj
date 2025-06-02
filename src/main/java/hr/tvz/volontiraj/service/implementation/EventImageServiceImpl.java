package hr.tvz.volontiraj.service.implementation;

import hr.tvz.volontiraj.model.EventImage;
import hr.tvz.volontiraj.repository.EventImageRepository;
import hr.tvz.volontiraj.service.EventImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EventImageServiceImpl implements EventImageService {

    private final EventImageRepository eventImageRepository;

    @Override
    public EventImage save(EventImage eventImage) {
       return eventImageRepository.save(eventImage);
    }

    @Override
    public EventImage findByEventId(Long eventId) {
        return eventImageRepository.findByEventId(eventId);
    }

    @Override
    public List<EventImage> findAllByEventId(Long eventId) {
        return eventImageRepository.findAllByEventId(eventId);
    }
}
