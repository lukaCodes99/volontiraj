package hr.tvz.volontiraj.service;

import hr.tvz.volontiraj.dto.EventDto;
import hr.tvz.volontiraj.dto.HomePageDto;
import hr.tvz.volontiraj.dto.NewEventDto;
import hr.tvz.volontiraj.dto.SearchEventDto;
import hr.tvz.volontiraj.filterParams.EventFilterParams;
import hr.tvz.volontiraj.model.Event;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface EventService {
    public Map<Long, List<SearchEventDto>> findAllPagedAndFiltered(Pageable pageable, EventFilterParams eventFilterParams);
    public EventDto findById(Long id);
    public EventDto save(NewEventDto newEventDto) throws IOException;
    public EventDto update(Long id, NewEventDto eventDto) throws IOException;
    public void deleteById(Long id);
    public void upvoteEvent(Long eventId);
    public void addVolunteer(Long eventId);
    public List<Event> findEventsWithinHour();
    Map<String, List<HomePageDto>> getEventsForHomePage();
}
