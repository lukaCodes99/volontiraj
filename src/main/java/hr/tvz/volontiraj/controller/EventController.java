package hr.tvz.volontiraj.controller;

import hr.tvz.volontiraj.dto.EventDto;
import hr.tvz.volontiraj.dto.HomePageDto;
import hr.tvz.volontiraj.dto.NewEventDto;
import hr.tvz.volontiraj.dto.SearchEventDto;
import hr.tvz.volontiraj.filterParams.EventFilterParams;
import hr.tvz.volontiraj.service.EventService;
import hr.tvz.volontiraj.util.PageableFactory;
import hr.tvz.volontiraj.util.SortStrategyFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/event")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class EventController {

    private final EventService eventService;
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @GetMapping
    public ResponseEntity<List<SearchEventDto>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDateTime") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            @ModelAttribute EventFilterParams filterParams) {

        Pageable pageable = PageableFactory.create(page, size, SortStrategyFactory.getSort(sortBy, ascending));

        List<SearchEventDto> events = eventService.findAllPagedAndFiltered(pageable, filterParams);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        try {
            EventDto event = eventService.findById(id);
            return ResponseEntity.ok(event);
        } catch (EntityNotFoundException e) {
            logger.error("Event not found with id: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/home")
    public ResponseEntity<Map<String, List<HomePageDto>>> getEventsForHomePage() {
        try {
            Map<String, List<HomePageDto>> events = eventService.getEventsForHomePage();
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            logger.error("Error fetching home page events: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<EventDto> createEvent(@ModelAttribute NewEventDto newEventDto) throws IOException {
        EventDto createdEvent = eventService.save(newEventDto);
        if (createdEvent == null) {
            logger.error("Failed to create event: {}", newEventDto);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.error("Event not found for deletion with id: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            logger.error("Access denied for deletion of event with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long id, @ModelAttribute NewEventDto updateEventDto) {
        try {
            EventDto updatedEvent = eventService.update(id, updateEventDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedEvent);
        } catch (EntityNotFoundException | IOException e) {
            logger.error("Error updating event with id: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            logger.error("Access denied for updating event with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
