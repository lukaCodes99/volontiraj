package hr.tvz.volontiraj.service.implementation;

import hr.tvz.volontiraj.dto.EventDto;
import hr.tvz.volontiraj.filterParams.EventFilterParams;
import hr.tvz.volontiraj.mapper.EventMapper;
import hr.tvz.volontiraj.model.Event;
import hr.tvz.volontiraj.model.EventCategory;
import hr.tvz.volontiraj.model.UserEntity;
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
        EventCategory eventCategory = eventFilterParams.getCategory() == null ? null : EventCategory.valueOf(eventFilterParams.getCategory());
        List<Event> events = eventRepository
                .findFilteredAndPaged(
                    eventCategory,
                    eventFilterParams.getTitle(),
                    eventFilterParams.getDescription(),
                    eventFilterParams.getLocation(),
                    eventFilterParams.getStartDateTimeFrom(),
                    eventFilterParams.getStartDateTimeTo(),
                    eventFilterParams.getCreatorId(),
                    pageable
                );

        return events.stream()
                    .map(EventMapper::mapEventToEventDto)
                    .toList();
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
    public Event update(Long id, EventDto eventDto) {
        Optional<Event> existingEvent = eventRepository.findById(id);
        if(existingEvent.isPresent()) {
            Event eventToUpdate = existingEvent.get();
            eventToUpdate.setCategory(EventCategory.valueOf(eventDto.getCategory()));
            eventToUpdate.setTitle(eventDto.getTitle());
            eventToUpdate.setDescription(eventDto.getDescription());
            eventToUpdate.setLocation(eventDto.getLocation());
            eventToUpdate.setStartDateTime(eventDto.getStartDateTime());
            return eventRepository.save(eventToUpdate);
        } else {
            throw new EntityNotFoundException("Event with id: " + id + " not found!");
        }
    }

    @Override
    public void deleteById(Long id) {
        Optional<Event> eventToDelete = eventRepository.findById(id);
        if(eventToDelete.isPresent()) {
            eventRepository.deleteById(id);
        }
        else throw new EntityNotFoundException("Event with id: " + id + " not found!");
    }

    @Override
    public void upvoteEvent(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            Event existingEvent = event.get();
            existingEvent.setUpvote(existingEvent.getUpvote() + 1);
            eventRepository.save(existingEvent);
        } else {
            throw new EntityNotFoundException("Event with id: " + eventId + " not found!");
        }
    }

    @Override
    public void addVolunteer(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            UserEntity mockUser = new UserEntity(1L); //Ovo cemo zamijeniti sa CURRENT USER--metoda ce biti implementirana kasnije kod security!
            Event existingEvent = event.get();
            existingEvent.getVolunteers().add(mockUser);
            eventRepository.save(existingEvent);
        } else {
            throw new EntityNotFoundException("Event with id: " + eventId + " not found!");
        }
    }
}
