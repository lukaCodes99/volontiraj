package hr.tvz.volontiraj.controller;

import hr.tvz.volontiraj.dto.*;
import hr.tvz.volontiraj.filterParams.EventFilterParams;
import hr.tvz.volontiraj.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllEvents_ShouldReturnEventsList() {
        List<SearchEventDto> mockEvents = List.of(new SearchEventDto(), new SearchEventDto());
        when(eventService.findAllPagedAndFiltered(any(Pageable.class), any(EventFilterParams.class)))
                .thenReturn(mockEvents);

        ResponseEntity<List<SearchEventDto>> response = eventController.getAllEvents(0, 10, "startDateTime", true, new EventFilterParams());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockEvents, response.getBody());
    }

    @Test
    void getAllEvents_WhenException_ShouldReturnInternalServerError() {
        when(eventService.findAllPagedAndFiltered(any(Pageable.class), any(EventFilterParams.class)))
                .thenThrow(new RuntimeException("DB error"));

        ResponseEntity<List<SearchEventDto>> response = eventController.getAllEvents(0, 10, "startDateTime", true, new EventFilterParams());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getEventById_ShouldReturnEvent() {
        EventDto eventDto = new EventDto();
        when(eventService.findById(1L)).thenReturn(eventDto);

        ResponseEntity<EventDto> response = eventController.getEventById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eventDto, response.getBody());
    }

    @Test
    void getEventById_WhenNotFound_ShouldReturnNotFound() {
        when(eventService.findById(1L)).thenThrow(new EntityNotFoundException());

        ResponseEntity<EventDto> response = eventController.getEventById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getEventsForHomePage_ShouldReturnMap() {
        Map<String, List<HomePageDto>> map = new HashMap<>();
        map.put("pets", List.of(new HomePageDto()));
        map.put("people", List.of(new HomePageDto()));

        when(eventService.getEventsForHomePage()).thenReturn(map);

        ResponseEntity<Map<String, List<HomePageDto>>> response = eventController.getEventsForHomePage();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(map, response.getBody());
    }

    @Test
    void getEventsForHomePage_WhenException_ShouldReturnInternalServerError() {
        when(eventService.getEventsForHomePage()).thenThrow(new RuntimeException());

        ResponseEntity<Map<String, List<HomePageDto>>> response = eventController.getEventsForHomePage();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createEvent_ShouldReturnCreatedEvent() throws IOException {
        NewEventDto newEventDto = new NewEventDto();
        EventDto createdEvent = new EventDto();
        when(eventService.save(any(NewEventDto.class))).thenReturn(createdEvent);

        ResponseEntity<EventDto> response = eventController.createEvent(newEventDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdEvent, response.getBody());
    }

    @Test
    void createEvent_WhenNullReturn_ShouldReturnInternalServerError() throws IOException {
        NewEventDto newEventDto = new NewEventDto();
        when(eventService.save(any(NewEventDto.class))).thenReturn(null);

        ResponseEntity<EventDto> response = eventController.createEvent(newEventDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteEvent_ShouldReturnNoContent() {
        doNothing().when(eventService).deleteById(1L);

        ResponseEntity<Void> response = eventController.deleteEvent(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteEvent_WhenNotFound_ShouldReturnNotFound() {
        doThrow(new EntityNotFoundException()).when(eventService).deleteById(1L);

        ResponseEntity<Void> response = eventController.deleteEvent(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteEvent_WhenAccessDenied_ShouldReturnForbidden() {
        doThrow(new org.springframework.security.access.AccessDeniedException("")).when(eventService).deleteById(1L);

        ResponseEntity<Void> response = eventController.deleteEvent(1L);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void updateEvent_ShouldReturnUpdatedEvent() {
        NewEventDto updateDto = new NewEventDto();
        EventDto updatedEvent = new EventDto();
        try {
            when(eventService.update(eq(1L), any(NewEventDto.class))).thenReturn(updatedEvent);
        } catch (IOException e) {
            fail("IOException not expected here");
        }

        ResponseEntity<EventDto> response = eventController.updateEvent(1L, updateDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(updatedEvent, response.getBody());
    }

    @Test
    void updateEvent_WhenException_ShouldReturnNotFound() throws IOException {
        NewEventDto updateDto = new NewEventDto();
        when(eventService.update(eq(1L), any(NewEventDto.class))).thenThrow(new EntityNotFoundException());

        ResponseEntity<EventDto> response = eventController.updateEvent(1L, updateDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateEvent_WhenAccessDenied_ShouldReturnForbidden() throws IOException {
        NewEventDto updateDto = new NewEventDto();
        when(eventService.update(eq(1L), any(NewEventDto.class))).thenThrow(new org.springframework.security.access.AccessDeniedException(""));

        ResponseEntity<EventDto> response = eventController.updateEvent(1L, updateDto);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
