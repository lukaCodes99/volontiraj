package hr.tvz.volontiraj.service;

import hr.tvz.volontiraj.dto.EventDto;
import hr.tvz.volontiraj.dto.HomePageDto;
import hr.tvz.volontiraj.dto.NewEventDto;
import hr.tvz.volontiraj.filterParams.EventFilterParams;
import hr.tvz.volontiraj.model.Event;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface EventService {
    public List<EventDto> findAllPagedAndFiltered(Pageable pageable, EventFilterParams eventFilterParams);
    public Event findById(Long id);
    public Event save(NewEventDto newEventDto);
    public Event update(Long id, EventDto eventDto);
    public void deleteById(Long id);
    public void upvoteEvent(Long eventId);
    public void addVolunteer(Long eventId);

    Map<String, List<HomePageDto>> getEventsForHomePage();
}
