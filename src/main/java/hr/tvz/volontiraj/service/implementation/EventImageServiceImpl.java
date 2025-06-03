package hr.tvz.volontiraj.service.implementation;

import hr.tvz.volontiraj.model.Event;
import hr.tvz.volontiraj.model.EventImage;
import hr.tvz.volontiraj.repository.EventImageRepository;
import hr.tvz.volontiraj.service.EventImageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventImageServiceImpl implements EventImageService {

    private final EventImageRepository eventImageRepository;

    @Override
    public EventImage save(EventImage eventImage) {
        return eventImageRepository.save(eventImage);
    }

    @Override
    public EventImage findById(Long id) {
        Optional<EventImage> eventImage = eventImageRepository.findById(id);
        if (eventImage.isPresent()) {
            return eventImage.get();
        } else {
            throw new EntityNotFoundException("Event image with id: " + id + " not found!");
        }
    }

    @Override
    public List<EventImage> findAllByEventId(Long eventId) {
        return eventImageRepository.findAllByEventId(eventId);
    }

    @Override
    public void deleteAllByEventId(Long eventId) {
        Optional<EventImage> eventImagesToDelete = eventImageRepository.findByEventId(eventId);
        if (eventImagesToDelete.isPresent()) {
            eventImageRepository.deleteAllByEventId(eventId);
        } else throw new EntityNotFoundException("Event images with event id: " + eventId + " not found!");
    }

    @Override
    public void deleteById(Long id) {
        Optional<EventImage> eventImageToDelete = eventImageRepository.findById(id);
        if (eventImageToDelete.isPresent()) {
            eventImageRepository.deleteById(id);
        } else throw new EntityNotFoundException("Event Image with id: " + id + " not found!");
    }
}
