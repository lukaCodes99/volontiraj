package hr.tvz.volontiraj.service.implementation;

import hr.tvz.volontiraj.dto.EventDto;
import hr.tvz.volontiraj.filterParams.EventFilterParams;
import hr.tvz.volontiraj.mapper.EventMapper;
import hr.tvz.volontiraj.model.Event;
import hr.tvz.volontiraj.repository.EventRepository;
import hr.tvz.volontiraj.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public List<EventDto> findAllPagedAndFiltered(Pageable pageable, EventFilterParams eventFilterParams) {
        return eventRepository.findAll().stream().map(EventMapper::mapEventToEventDto).toList();
    }

    @Override
    public Event findById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public Event save(EventDto eventDto) {
        Event newEvent = EventMapper.mapEventDtoToEvent(eventDto);
        return eventRepository.save(newEvent);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Event> eventToDelete = eventRepository.findById(id);
        if(eventToDelete.isPresent()) {
            eventRepository.deleteById(id);
        }
        else throw new EntityNotFoundException("Event with id: " + id + " not found!");
    }
}
