package hr.tvz.volontiraj.repository;

import hr.tvz.volontiraj.model.Event;
import hr.tvz.volontiraj.model.EventCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    void testFindFilteredAndPaged_ByTitleAndCategory() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Event> results = eventRepository.findFilteredAndPaged(
                EventCategory.PETS, "Pet", null, null, null, null, null, pageable);

        assertFalse(results.getContent().isEmpty());
    }

    @Test
    void testFindLatestForHomePage() {
        List<Event> results = eventRepository.findLatestForHomePage();
        assertFalse(results.isEmpty());
    }

    @Test
    void testCountByCategory() {
        Long result = eventRepository.countByCategory(EventCategory.PETS);
        assertTrue(result >= 0);
    }

    @Test
    void testFindTop5ByOrderByUpvoteDesc() {
        List<Event> results = eventRepository.findTop5ByOrderByUpvoteDesc();
        assertFalse(results.isEmpty());
    }
}
