package hr.tvz.volontiraj.service.implementation;

import hr.tvz.volontiraj.dto.EventSummaryDto;
import hr.tvz.volontiraj.model.Event;
import hr.tvz.volontiraj.model.EventCategory;
import hr.tvz.volontiraj.repository.EventRepository;
import hr.tvz.volontiraj.repository.UserRepository;
import hr.tvz.volontiraj.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public long getTotalEvents() {
        return eventRepository.count();
    }

    @Override
    public Map<EventCategory, Long> getEventCountByCategory() {
        List<Object[]> eventCounts = eventRepository.countEventsByCategory();
        return eventCounts.stream()
                .collect(Collectors.toMap(
                        e -> EventCategory.valueOf((String) e[0]),
                        e -> (Long) e[1]
                ));
    }

    @Override
    public long getEventCountByCategory(EventCategory category) {
        return eventRepository.countByCategory(category);
    }

    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public List<EventSummaryDto> getTop5EventsByUpvotes() {
        List<Event> events = eventRepository.findTop5ByOrderByUpvoteDesc();
        return events.stream()
                .map(event -> new EventSummaryDto(
                        event.getId(),
                        event.getTitle(),
                        event.getUpvote(),
                        event.getCategory().name()
                ))
                .collect(Collectors.toList());
    }
}
