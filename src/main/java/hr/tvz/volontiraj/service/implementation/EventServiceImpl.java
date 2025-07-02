package hr.tvz.volontiraj.service.implementation;

import hr.tvz.volontiraj.dto.*;
import hr.tvz.volontiraj.filterParams.EventFilterParams;
import hr.tvz.volontiraj.mapper.EventImageMapper;
import hr.tvz.volontiraj.mapper.EventMapper;
import hr.tvz.volontiraj.model.Event;
import hr.tvz.volontiraj.model.EventCategory;
import hr.tvz.volontiraj.model.EventImage;
import hr.tvz.volontiraj.model.UserEntity;
import hr.tvz.volontiraj.repository.EventImageRepository;
import hr.tvz.volontiraj.repository.EventRepository;
import hr.tvz.volontiraj.repository.UserRepository;
import hr.tvz.volontiraj.service.EventImageReadService;
import hr.tvz.volontiraj.service.EventService;
import hr.tvz.volontiraj.service.SupabaseService;
import hr.tvz.volontiraj.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventImageRepository eventImageRepository;

    private final EventImageReadService eventImageReadService;
    private final SupabaseService supabaseService;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public Map<Long, List<SearchEventDto>> findAllPagedAndFiltered(Pageable pageable, EventFilterParams eventFilterParams) {
        EventCategory eventCategory = eventFilterParams.getCategory() == null ? null : EventCategory.valueOf(eventFilterParams.getCategory());
        Page<Event> events = eventRepository
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

        Map<Long, List<SearchEventDto>> map = new HashMap<>();
        map.put(events.getTotalElements(), events.getContent().stream()
                .map(EventMapper::mapEventToSearchEventDto)
                .toList());
        return map;
    }

    @Override
    public EventDto findById(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent()) {
            EventDto eventDto = EventMapper.mapEventToEventDto(event.get());
            List<EventImageDto> eventImages = eventImageReadService.findAllByEventId(eventDto.getId()).stream().map(EventImageMapper::mapEventImageToEventImageDto).toList();
            eventDto.setImages(eventImages);
            return eventDto;
        } else {
            throw new EntityNotFoundException("Event with id: " + id + " not found!");
        }
    }

    @Transactional(rollbackFor = IOException.class)
    @Override
    public EventDto save(NewEventDto newEventDto) throws IOException {
        Event newEvent = EventMapper.mapNewEventDtoToEvent(newEventDto);

        Optional<UserEntity> user = userRepository.findById(newEventDto.getCreatorId());

        if (user.isPresent()) {
            newEvent.setCreator(user.get());
            Event savedEvent = eventRepository.save(newEvent);

            List<String> imagesURL = supabaseService.uploadImages(newEventDto.getImages());

            List<EventImage> eventImages = imagesURL.stream()
                    .map(imageURL -> new EventImage(imageURL, savedEvent)).toList();

            eventImageRepository.saveAll(eventImages);

            EventDto eventDto = EventMapper.mapEventToEventDto(savedEvent);
            eventDto.setImages(eventImages.stream().map(EventImageMapper::mapEventImageToEventImageDto).toList());

            return eventDto;
        } else throw new EntityNotFoundException("User with id: " + newEventDto.getCreatorId() + " not found!");
    }

    @Override
    public EventDto update(Long id, NewEventDto eventDto) throws IOException {
        Optional<Event> existingEvent = eventRepository.findById(id);
        if (existingEvent.isPresent()) {
            String creatorEmail = existingEvent.get().getCreator().getEmail();
            if (!creatorEmail.equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                throw new AccessDeniedException("You do not have permission to delete this event.");
            }
            Event eventToUpdate = existingEvent.get();
            eventToUpdate.setCategory(EventCategory.valueOf(eventDto.getCategory()));
            eventToUpdate.setDescription(eventDto.getDescription());
            eventToUpdate.setTitle(eventDto.getTitle());
            eventToUpdate.setDetails(eventDto.getDetails());
            eventToUpdate.setLocation(eventDto.getLocation());
            eventToUpdate.setAddress(eventDto.getAddress());
            eventToUpdate.setStartDateTime(eventDto.getStartDateTime());

            eventRepository.save(eventToUpdate);

            EventDto updatedEvent = EventMapper.mapEventToEventDto(eventToUpdate);

            //mozda treba provjeriti jos ali prvo treba testirati s frontenda ,
            if (eventDto.getImages() != null) {
                eventImageRepository.deleteAllByEventId(eventToUpdate.getId());

                List<String> imagesURL = supabaseService.uploadImages(eventDto.getImages());

                List<EventImage> eventImages = imagesURL.stream()
                        .map(imageURL -> new EventImage(imageURL, eventToUpdate)).toList();

                eventImageRepository.saveAll(eventImages);
                updatedEvent.setImages(eventImages.stream().map(EventImageMapper::mapEventImageToEventImageDto).toList());
            }

            return updatedEvent;
        } else
            throw new EntityNotFoundException("Event with id: " + id + " not found!");
    }

    @Override
    public void deleteById(Long id) {
        Optional<Event> eventToDelete = eventRepository.findById(id);
        if (eventToDelete.isPresent()) {
            String creatorEmail = eventToDelete.get().getCreator().getEmail();
            if (creatorEmail.equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                eventRepository.deleteById(id);
            } else {
                throw new AccessDeniedException("You do not have permission to delete this event.");
            }
        } else throw new EntityNotFoundException("Event with id: " + id + " not found!");
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
            //UserEntity mockUser = new UserEntity(1L); //Ovo cemo zamijeniti sa CURRENT USER--metoda ce biti implementirana kasnije kod security!
            UserEntity user = userService.getCurrentUser();
            Event existingEvent = event.get();
            existingEvent.getVolunteers().add(user);
            eventRepository.save(existingEvent);
        } else {
            throw new EntityNotFoundException("Event with id: " + eventId + " not found!");
        }
    }

    @Override
    public List<Event> findEventsWithinHour() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("in service");
        try {
            System.out.println("Finding events within the next hour from: " + now);
            return eventRepository.findEventsWithinHour(now, now.plusHours(1));
        } catch (Exception e) {
            System.out.println("Error in findEventsWithinHour: " + e);
            throw new RuntimeException("Error fetching events within the hour: " + e);
        }
    }

    //možemo i neki drugi approach, ali svakako se vidi namjera što želimo postići
    @Override
    public Map<String, List<HomePageDto>> getEventsForHomePage() {
        List<Event> events = eventRepository.findLatestForHomePage();
        Map<String, List<HomePageDto>> map = new HashMap<>();
        map.put("pets", new ArrayList<>());
        map.put("people", new ArrayList<>());

        events.forEach(event -> {
            if (event.getCategory() == EventCategory.PETS) {
                HomePageDto homePageDto = EventMapper.mapEventToHomePageDto(event);
                eventImageRepository.findImagePathByEventId(event.getId())
                        .ifPresent(homePageDto::setImagePath);

                map.get("pets").add(homePageDto);
            } else if (event.getCategory() == EventCategory.PEOPLE) {
                HomePageDto homePageDto = EventMapper.mapEventToHomePageDto(event);
                eventImageRepository.findImagePathByEventId(event.getId())
                        .ifPresent(homePageDto::setImagePath);

                map.get("people").add(homePageDto);
            }
        });
        return map;
    }
}
