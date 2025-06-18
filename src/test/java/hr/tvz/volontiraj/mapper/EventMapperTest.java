import hr.tvz.volontiraj.dto.*;
import hr.tvz.volontiraj.mapper.EventMapper;
import hr.tvz.volontiraj.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class EventMapperTest {

    @Test
    void mapEventToEventDto_shouldMapAllFields() {
        UserEntity creator = new UserEntity();
        creator.setId(123L);

        Event event = new Event();
        event.setId(1L);
        event.setCategory(EventCategory.PEOPLE);
        event.setTitle("Test Event");
        event.setDescription("Desc");
        event.setDetails("Details");
        event.setLocation("Location");
        event.setAddress("Address");
        event.setStartDateTime(LocalDateTime.of(2025,6,18,10,0));
        event.setUpvote(10);
        event.setCreator(creator);
        event.setVolunteers(new HashSet<>());
        event.getVolunteers().add(new UserEntity());

        EventDto dto = EventMapper.mapEventToEventDto(event);

        assertEquals(event.getId(), dto.getId());
        assertEquals(event.getCategory().toString(), dto.getCategory());
        assertEquals(event.getTitle(), dto.getTitle());
        assertEquals(event.getDescription(), dto.getDescription());
        assertEquals(event.getDetails(), dto.getDetails());
        assertEquals(event.getLocation(), dto.getLocation());
        assertEquals(event.getAddress(), dto.getAddress());
        assertEquals(event.getStartDateTime(), dto.getStartDateTime());
        assertEquals(event.getUpvote(), dto.getUpvote());
        assertEquals(creator.getId(), dto.getCreatorId());
        assertEquals(event.getVolunteers().size(), dto.getVolunteerCount());
    }

    @Test
    void mapEventToEventDto_shouldHandleNulls() {
        Event event = new Event();
        event.setId(2L);
        event.setCategory(EventCategory.COMMUNITY);
        event.setTitle("No Creator and Volunteers");

        EventDto dto = EventMapper.mapEventToEventDto(event);

        assertEquals(event.getId(), dto.getId());
        assertEquals(event.getCategory().toString(), dto.getCategory());
        assertEquals(event.getTitle(), dto.getTitle());
        assertNull(dto.getCreatorId());
    }

    @Test
    void mapEventDtoToEvent_shouldMapAllFields() {
        EventDto dto = new EventDto();
        dto.setId(1L);
        dto.setCategory(EventCategory.PETS.toString());
        dto.setTitle("Test Title");
        dto.setDescription("Desc");
        dto.setDetails("Details");
        dto.setLocation("Location");
        dto.setAddress("Address");
        dto.setStartDateTime(LocalDateTime.of(2025,6,18,10,0));
        dto.setUpvote(5);
        dto.setCreatorId(123L);

        Event event = EventMapper.mapEventDtoToEvent(dto);

        assertEquals(dto.getId(), event.getId());
        assertEquals(EventCategory.PETS, event.getCategory());
        assertEquals(dto.getTitle(), event.getTitle());
        assertEquals(dto.getDescription(), event.getDescription());
        assertEquals(dto.getDetails(), event.getDetails());
        assertEquals(dto.getLocation(), event.getLocation());
        assertEquals(dto.getAddress(), event.getAddress());
        assertEquals(dto.getStartDateTime(), event.getStartDateTime());
        assertEquals(dto.getUpvote(), event.getUpvote());
        assertNotNull(event.getCreator());
        assertEquals(dto.getCreatorId(), event.getCreator().getId());
        assertNotNull(event.getVolunteers());
        assertTrue(event.getVolunteers().isEmpty());
    }

    @Test
    void mapEventDtoToEvent_shouldHandleNullCreatorId() {
        EventDto dto = new EventDto();
        dto.setId(10L);
        dto.setCategory(EventCategory.OTHER.toString());
        dto.setTitle("No Creator");
        // creatorId = null

        Event event = EventMapper.mapEventDtoToEvent(dto);

        assertEquals(dto.getId(), event.getId());
        assertEquals(EventCategory.OTHER, event.getCategory());
        assertEquals(dto.getTitle(), event.getTitle());
        assertNull(event.getCreator());
        assertNotNull(event.getVolunteers());
        assertTrue(event.getVolunteers().isEmpty());
    }

    @Test
    void mapEventToHomePageDto_shouldMapCorrectly() {
        Event event = new Event();
        event.setId(5L);
        event.setTitle("Homepage Event");
        event.setLocation("Somewhere");
        event.setStartDateTime(LocalDateTime.now());

        HomePageDto dto = EventMapper.mapEventToHomePageDto(event);

        assertEquals(event.getId(), dto.getId());
        assertEquals(event.getTitle(), dto.getTitle());
        assertEquals(event.getLocation(), dto.getLocation());
        assertEquals(event.getStartDateTime(), dto.getStartDateTime());
    }

    @Test
    void mapNewEventDtoToEvent_shouldMapCorrectly() {
        NewEventDto newDto = new NewEventDto();
        newDto.setCategory(EventCategory.PEOPLE.toString());
        newDto.setTitle("New Event");
        newDto.setDescription("New Desc");
        newDto.setDetails("New Details");
        newDto.setLocation("New Location");
        newDto.setAddress("New Address");
        newDto.setStartDateTime(LocalDateTime.now());

        Event event = EventMapper.mapNewEventDtoToEvent(newDto);

        assertEquals(EventCategory.PEOPLE, event.getCategory());
        assertEquals(newDto.getTitle(), event.getTitle());
        assertEquals(newDto.getDescription(), event.getDescription());
        assertEquals(newDto.getDetails(), event.getDetails());
        assertEquals(newDto.getLocation(), event.getLocation());
        assertEquals(newDto.getAddress(), event.getAddress());
        assertEquals(newDto.getStartDateTime(), event.getStartDateTime());
    }

    @Test
    void mapEventToSearchEventDto_shouldMapCorrectly() {
        UserEntity creator = new UserEntity();
        creator.setId(99L);
        creator.setProfilePicturePath("/images/profile.jpg");

        Event event = new Event();
        event.setId(10L);
        event.setCategory(EventCategory.COMMUNITY);
        event.setTitle("Search Event");
        event.setLocation("Search Location");
        event.setStartDateTime(LocalDateTime.of(2025,6,18,15,0));
        event.setCreator(creator);

        SearchEventDto dto = EventMapper.mapEventToSearchEventDto(event);

        assertEquals(event.getId(), dto.getId());
        assertEquals(event.getCategory(), dto.getCategory());
        assertEquals(event.getTitle(), dto.getTitle());
        assertEquals(event.getLocation(), dto.getLocation());
        assertEquals(event.getStartDateTime(), dto.getStartDateTime());
        assertEquals(creator.getId(), dto.getCreatorId());
        assertEquals(creator.getProfilePicturePath(), dto.getCreatorProfileImageURL());
    }

    @Test
    void mapEventToSearchEventDto_shouldHandleNullCreator() {
        Event event = new Event();
        event.setId(20L);
        event.setCategory(EventCategory.PETS);
        event.setTitle("No Creator");
        event.setLocation("Unknown");
        event.setStartDateTime(LocalDateTime.now());
        event.setCreator(null);


        Exception exception = assertThrows(NullPointerException.class, () -> {
            EventMapper.mapEventToSearchEventDto(event);
        });
        assertNotNull(exception.getMessage());
    }
}
