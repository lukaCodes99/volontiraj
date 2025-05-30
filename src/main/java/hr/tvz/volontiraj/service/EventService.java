package hr.tvz.volontiraj.service;

import hr.tvz.volontiraj.dto.EventDto;
import hr.tvz.volontiraj.filterParams.EventFilterParams;
import hr.tvz.volontiraj.model.Event;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventService {
    public List<EventDto> findAllPagedAndFiltered(Pageable pageable, EventFilterParams eventFilterParams);
    public Event findById(Long id);
    public Event save(EventDto eventDto);
    public void deleteById(Long id);
}
