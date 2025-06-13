package hr.tvz.volontiraj.service;

import hr.tvz.volontiraj.dto.EventSummaryDto;
import hr.tvz.volontiraj.model.EventCategory;

import java.util.List;
import java.util.Map;

public interface AdminService {
    long getTotalEvents();
    Map<EventCategory, Long> getEventCountByCategory();
    long getEventCountByCategory(EventCategory category);
    long getTotalUsers();
    List<EventSummaryDto> getTop5EventsByUpvotes();
}
