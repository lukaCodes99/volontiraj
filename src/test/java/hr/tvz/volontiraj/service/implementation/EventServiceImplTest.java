package hr.tvz.volontiraj.service.implementation;

import hr.tvz.volontiraj.dto.*;
import hr.tvz.volontiraj.model.Event;
import hr.tvz.volontiraj.filterParams.EventFilterParams;
import hr.tvz.volontiraj.mapper.EventImageMapper;
import hr.tvz.volontiraj.mapper.EventMapper;
import hr.tvz.volontiraj.model.EventCategory;
import hr.tvz.volontiraj.model.EventImage;
import hr.tvz.volontiraj.model.UserEntity;
import hr.tvz.volontiraj.repository.EventImageRepository;
import hr.tvz.volontiraj.repository.EventRepository;
import hr.tvz.volontiraj.repository.UserRepository;
import hr.tvz.volontiraj.service.EventImageService;
import hr.tvz.volontiraj.service.SupabaseService;
import hr.tvz.volontiraj.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventImageRepository eventImageRepository;

    @Mock
    private EventImageService eventImageService;
    @Mock
    private SupabaseService supabaseService;
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private EventServiceImpl eventServiceImpl;

    @Test
    void findAllPagedAndFiltered_ShouldReturnAllEvents() {
        Pageable pageable = PageRequest.of(0, 10);
        EventFilterParams filterParams = new EventFilterParams();
        filterParams.setCategory(EventCategory.PEOPLE.name());
        filterParams.setTitle("Clean-up");
        filterParams.setDescription("Beach");
        filterParams.setLocation("LA");
        filterParams.setStartDateTimeFrom(LocalDateTime.now().minusDays(1));
        filterParams.setStartDateTimeTo(LocalDateTime.now().plusDays(1));
        filterParams.setCreatorId(1L);

        Event event1 = new Event();
        event1.setId(1L);
        Page<Event> mockEventList =  new PageImpl<>(List.of(event1));

        when(eventRepository.findFilteredAndPaged(
                EventCategory.PEOPLE,
                "Clean-up",
                "Beach",
                "LA",
                filterParams.getStartDateTimeFrom(),
                filterParams.getStartDateTimeTo(),
                filterParams.getCreatorId(),
                pageable
        )).thenReturn(mockEventList);

        try (MockedStatic<EventMapper> mocked = mockStatic(EventMapper.class)) {
            SearchEventDto dto1 = new SearchEventDto();

            mocked.when(() -> EventMapper.mapEventToSearchEventDto(event1)).thenReturn(dto1);

            Map<Long,List<SearchEventDto>> result = eventServiceImpl.findAllPagedAndFiltered(pageable, filterParams);

            assertFalse(result.isEmpty());
            assertEquals(mockEventList.getSize(), result.size());

            Optional<List<SearchEventDto>> first = result.values().stream().findFirst();
            first.ifPresent(searchEventDto -> assertEquals(dto1, searchEventDto.getFirst()));
        }
    }

    @Test
    void findById_ShouldReturnEvent() {
        Long id = 1L;

        hr.tvz.volontiraj.model.Event event = new hr.tvz.volontiraj.model.Event();
        event.setId(id);

        EventDto mappedEventDto = new EventDto();
        mappedEventDto.setId(id);

        EventImage image = new EventImage();
        EventImageDto mappedImageDto = new EventImageDto();

        List<EventImage> images = List.of(image);

        when(eventRepository.findById(id)).thenReturn(Optional.of(event));
        when(eventImageService.findAllByEventId(id)).thenReturn(images);

        try (
                MockedStatic<EventMapper> eventMapperMock = mockStatic(EventMapper.class);
                MockedStatic<EventImageMapper> imageMapperMock = mockStatic(EventImageMapper.class)
        ) {
            eventMapperMock.when(() -> EventMapper.mapEventToEventDto(event)).thenReturn(mappedEventDto);
            imageMapperMock.when(() -> EventImageMapper.mapEventImageToEventImageDto(image)).thenReturn(mappedImageDto);

            EventDto result = eventServiceImpl.findById(id);

            assertEquals(mappedEventDto, result);
            assertEquals(1, result.getImages().size());
            assertEquals(mappedImageDto, result.getImages().getFirst());
        }
    }

    @Test
    void findById_shouldThrowException_whenEventNotFound() {
        Long id = 42L;
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            eventServiceImpl.findById(id);
        });

        assertEquals("Event with id: 42 not found!", exception.getMessage());
    }

    @Test
    void save_ShouldSaveEvent() throws IOException {
        Long creatorId = 1L;
        NewEventDto newEventDto = new NewEventDto();
        newEventDto.setCreatorId(creatorId);
        newEventDto.setImages(List.of(new MockMultipartFile("img1", new byte[]{})));

        UserEntity user = new UserEntity();
        user.setId(creatorId);

        Event eventToSave = new Event();
        Event savedEvent = new Event();
        savedEvent.setId(1L);

        List<String> imageUrls = List.of("url1", "url2");

        List<EventImage> eventImages = imageUrls.stream()
                .map(url -> new EventImage(url, savedEvent))
                .toList();

        EventDto eventDtoMapped = new EventDto();
        eventDtoMapped.setId(10L);

        when(userRepository.findById(creatorId)).thenReturn(Optional.of(user));
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);
        when(supabaseService.uploadImages(newEventDto.getImages())).thenReturn(imageUrls);

        try (
                MockedStatic<EventMapper> eventMapperMock = mockStatic(EventMapper.class);
                MockedStatic<EventImageMapper> imageMapperMock = mockStatic(EventImageMapper.class)
        ) {
            eventMapperMock.when(() -> EventMapper.mapNewEventDtoToEvent(newEventDto)).thenReturn(eventToSave);
            eventMapperMock.when(() -> EventMapper.mapEventToEventDto(savedEvent)).thenReturn(eventDtoMapped);

            for (String url : imageUrls) {
                imageMapperMock.when(() -> EventImageMapper.mapEventImageToEventImageDto(any())).thenReturn(new EventImageDto());
            }

            EventDto result = eventServiceImpl.save(newEventDto);

            assertEquals(eventDtoMapped.getId(), result.getId());
            assertEquals(2, result.getImages().size());

            verify(userRepository).findById(creatorId);
            verify(eventRepository).save(eventToSave);
            verify(eventImageRepository).saveAll(anyList());
            verify(supabaseService).uploadImages(newEventDto.getImages());
        }
    }

    @Test
    void save_shouldThrow_whenUserNotFound() {
        NewEventDto dto = new NewEventDto();
        dto.setCreatorId(1L);
        dto.setCategory(EventCategory.PEOPLE.name());

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            eventServiceImpl.save(dto);
        });

        assertEquals("User with id: 1 not found!", exception.getMessage());
    }

    @Test
    void update_ShouldUpdateEvent() throws IOException {
        Long eventId = 1L;
        String loggedInUserEmail = "creator@example.com";

        // Mock SecurityContext
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(loggedInUserEmail);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Existing event
        Event existingEvent = new Event();
        existingEvent.setId(eventId);
        existingEvent.setTitle("Old Title");
        UserEntity creator = new UserEntity();
        creator.setEmail(loggedInUserEmail);
        existingEvent.setCreator(creator);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // DTO with new data
        NewEventDto newEventDto = new NewEventDto();
        newEventDto.setTitle("New Title");
        newEventDto.setCategory("COMMUNITY");
        newEventDto.setDescription("New Desc");
        newEventDto.setDetails("New Details");
        newEventDto.setLocation("New Loc");
        newEventDto.setAddress("New Addr");
        newEventDto.setStartDateTime(LocalDateTime.now());
        newEventDto.setImages(List.of()); // Skip images in this test

        EventDto result = eventServiceImpl.update(eventId, newEventDto);

        assertEquals("New Title", result.getTitle());
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void update_shouldThrowAccessDenied_whenUserIsNotCreator() {
        Long eventId = 1L;

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("other@example.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Event event = new Event();
        event.setId(eventId);
        UserEntity creator = new UserEntity();
        creator.setEmail("creator@example.com");
        event.setCreator(creator);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        NewEventDto dto = new NewEventDto();
        dto.setCategory(EventCategory.PEOPLE.name());

        assertThrows(AccessDeniedException.class, () ->
                eventServiceImpl.update(eventId, dto)
        );
    }

    @Test
    void update_shouldThrowEntityNotFound_whenEventDoesNotExist() {
        Long eventId = 99L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        NewEventDto dto = new NewEventDto();
        dto.setCategory(EventCategory.PETS.name());

        assertThrows(EntityNotFoundException.class, () -> {
            eventServiceImpl.update(eventId, dto);
        });
    }

    @Test
    void deleteById_shouldDeleteEvent() {
        Long eventId = 1L;
        String userEmail = "creator@example.com";
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(userEmail);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Event event = new Event();
        UserEntity creator = new UserEntity();
        creator.setEmail(userEmail);
        event.setCreator(creator);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        eventServiceImpl.deleteById(eventId);

        verify(eventRepository).deleteById(eventId);
    }

    @Test
    void deleteById_shouldThrowAccessDenied_whenUserIsNotCreator() {
        Long eventId = 1L;
        String userEmail = "someoneother@example.com";

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(userEmail);
        SecurityContext sc = mock(SecurityContext.class);
        when(sc.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(sc);

        Event event = new Event();
        event.setId(eventId);
        UserEntity creator = new UserEntity();
        creator.setEmail("creator@example.com");
        event.setCreator(creator);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        assertThrows(AccessDeniedException.class, () -> eventServiceImpl.deleteById(eventId));
        verify(eventRepository, never()).deleteById(eventId);
    }

    @Test
    void deleteById_shouldThrowEntityNotFound_whenEventDoesNotExist() {
        Long eventId = 1L;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> eventServiceImpl.deleteById(eventId));
        assertEquals("Event with id: 1 not found!", ex.getMessage());

        verify(eventRepository, never()).deleteById(eventId);
    }

    @Test
    void upvoteEvent_ShouldUpvoteEvent() {
        Long eventId = 1L;
        Integer upvoteCount = 1;
        Event event = new Event();
        event.setId(eventId);
        event.setUpvote(upvoteCount);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        eventServiceImpl.upvoteEvent(eventId);

        assertEquals(upvoteCount + 1, event.getUpvote());
        verify(eventRepository).save(event);
    }

    @Test
    void upvoteEvent_ShouldThrowEntityNotFoundException() {
        Long eventId = 1L;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> eventServiceImpl.upvoteEvent(eventId)
        );

        assertEquals("Event with id: 1 not found!", exception.getMessage());
        verify(eventRepository, never()).save(any());
    }

    @Test
    void addVolunteer_shouldAddUserToVolunteersAndSave() {
        Long eventId = 1L;

        Event event = new Event();
        event.setVolunteers(new HashSet<>());

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        eventServiceImpl.addVolunteer(eventId);

        verify(eventRepository).save(event);
    }

    @Test
    void addVolunteer_shouldThrowException_whenEventNotFound() {
        Long eventId = 404L;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            eventServiceImpl.addVolunteer(eventId);
        });

        assertEquals("Event with id: 404 not found!", exception.getMessage());
        verify(eventRepository, never()).save(any());
    }

    @Test
    void findEventsWithinHour_ShouldFindEventsWithinHour() {
        Event event1 = new Event();
        Event event2 = new Event();

        List<Event> events = List.of(event1, event2);

        when(eventRepository.findEventsWithinHour(any(), any())).thenReturn(events);

        List<Event> result = eventServiceImpl.findEventsWithinHour();

        assertEquals(events, result);
        verify(eventRepository).findEventsWithinHour(any(), any());
    }

    @Test
    void findEventsWithinHour_ShouldThrowException_whenEventNotFound() {
        when(eventRepository.findEventsWithinHour(any(), any())).thenThrow(RuntimeException.class);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> eventServiceImpl.findEventsWithinHour());

        assertTrue(exception.getMessage().contains("Error fetching events within the hour:"));
    }


    @Test
    void getEventsForHomePage_ShouldReturnEventsForHomePage() {
        Event petEvent = new Event();
        petEvent.setId(1L);
        petEvent.setCategory(EventCategory.PETS);
        petEvent.setTitle("Pet Event");

        Event peopleEvent = new Event();
        peopleEvent.setId(2L);
        peopleEvent.setCategory(EventCategory.PEOPLE);
        peopleEvent.setTitle("People Event");

        List<Event> mockEvents = List.of(petEvent, peopleEvent);

        when(eventRepository.findLatestForHomePage()).thenReturn(mockEvents);
        when(eventImageRepository.findImagePathByEventId(1L)).thenReturn(Optional.of("petImage.jpg"));
        when(eventImageRepository.findImagePathByEventId(2L)).thenReturn(Optional.of("peopleImage.jpg"));

        Map<String, List<HomePageDto>> result = eventServiceImpl.getEventsForHomePage();

        assertTrue(result.containsKey("pets"));
        assertTrue(result.containsKey("people"));

        List<HomePageDto> petDto = result.get("pets");
        List<HomePageDto> peopleDto = result.get("people");

        assertEquals(1, petDto.size());
        assertEquals("Pet Event", petDto.getFirst().getTitle());
        assertEquals("petImage.jpg", petDto.getFirst().getImagePath());

        assertEquals(1, peopleDto.size());
        assertEquals("People Event", peopleDto.getFirst().getTitle());
        assertEquals("peopleImage.jpg", peopleDto.getFirst().getImagePath());

        verify(eventRepository).findLatestForHomePage();
        verify(eventImageRepository).findImagePathByEventId(1L);
        verify(eventImageRepository).findImagePathByEventId(2L);

    }


}
