package hr.tvz.volontiraj.service.implementation;

import hr.tvz.volontiraj.dto.EventSummaryDto;
import hr.tvz.volontiraj.model.Event;
import hr.tvz.volontiraj.model.EventCategory;
import hr.tvz.volontiraj.repository.EventRepository;
import hr.tvz.volontiraj.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private EventRepository eventRepository;


    @InjectMocks
    private AdminServiceImpl adminServiceImpl;

    @Test
    void getTotalEvents(){
        Long expectedTotalEvents = 10L;

        when(eventRepository.count()).thenReturn(expectedTotalEvents);

        Long result = adminServiceImpl.getTotalEvents();

        assertEquals(expectedTotalEvents, result);
        verify(eventRepository).count();
    }

    @Test
    void getEventCountByCategory_ShouldReturnEventCountByCategories() {
        Object[] row1 = new Object[]{EventCategory.PETS, 1L};
        Object[] row2 = new Object[]{EventCategory.PEOPLE, 2L};
        Object[] row3 = new Object[]{EventCategory.COMMUNITY, 3L};
        Object[] row4 = new Object[]{EventCategory.OTHER, 4L};

        List<Object[]> mockData = List.of(row1, row2, row3, row4);

        when(eventRepository.countEventsByCategory()).thenReturn(mockData);

        Map<EventCategory, Long> result = adminServiceImpl.getEventCountByCategory();

        assertEquals(mockData.size(), result.size());

        assertEquals(1L, result.get(EventCategory.PETS));
        assertEquals(2L, result.get(EventCategory.PEOPLE));
        assertEquals(3L, result.get(EventCategory.COMMUNITY));
        assertEquals(4L, result.get(EventCategory.OTHER));

        verify(eventRepository).countEventsByCategory();
    }

    @Test
    void getEventCountByCategory_ShouldReturnEventCountByCategory() {
        EventCategory eventCategory = EventCategory.PETS;

        when(eventRepository.countByCategory(eventCategory)).thenReturn(15L);

        Long result = adminServiceImpl.getEventCountByCategory(eventCategory);

        assertEquals(15, result);
        verify(eventRepository).countByCategory(eventCategory);
    }

    @Test
    void getTotalUsers_ShouldReturnTotalUsers() {
        Long expectedCount = 10L;
        when(userRepository.count()).thenReturn(expectedCount);

        Long result = adminServiceImpl.getTotalUsers();

        assertEquals(expectedCount, result);
        verify(userRepository).count();
    }

    @Test
    void getTop5EventsByUpvotes_ShouldReturnTop5EventsByUpvotes() {
        Event event1 = createEvent(1L, "Event 1", 100, EventCategory.COMMUNITY);
        Event event2 = createEvent(2L, "Event 2", 75, EventCategory.PETS);
        Event event3 = createEvent(3L, "Event 3", 50, EventCategory.PEOPLE);
        Event event4 = createEvent(4L, "Event 4", 25, EventCategory.OTHER);
        Event event5 = createEvent(5L, "Event 5", 15, EventCategory.PEOPLE);

        List<Event> mockData = List.of(event1, event2, event3, event4, event5);

        when(eventRepository.findTop5ByOrderByUpvoteDesc()).thenReturn(mockData);

        List<EventSummaryDto> result = adminServiceImpl.getTop5EventsByUpvotes();

        assertEquals(mockData.size(), result.size());

        for (int i = 0; i < result.size(); i++) {
            assertEqualsHelper(mockData.get(i), result.get(i));
        }

        verify(eventRepository).findTop5ByOrderByUpvoteDesc();
    }

    //helpers
    private Event createEvent(Long id, String title, int upvote, EventCategory category) {
        Event event = new Event();
        event.setId(id);
        event.setTitle(title);
        event.setUpvote(upvote);
        event.setCategory(category);
        return event;
    }

    private void assertEqualsHelper(Event event, EventSummaryDto dto) {
        assertEquals(event.getId(), dto.getId());
        assertEquals(event.getTitle(), dto.getTitle());
        assertEquals(event.getUpvote(), dto.getUpvote());
        assertEquals(event.getCategory().name(), dto.getCategory());
    }

}
