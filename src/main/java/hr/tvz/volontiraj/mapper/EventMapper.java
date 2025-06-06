package hr.tvz.volontiraj.mapper;

import hr.tvz.volontiraj.dto.EventDto;
import hr.tvz.volontiraj.dto.HomePageDto;
import hr.tvz.volontiraj.dto.NewEventDto;
import hr.tvz.volontiraj.dto.SearchEventDto;
import hr.tvz.volontiraj.model.Event;
import hr.tvz.volontiraj.model.EventCategory;
import hr.tvz.volontiraj.model.EventImage;
import hr.tvz.volontiraj.model.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static EventDto mapEventToEventDto(Event event) {
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setCategory(event.getCategory().toString());
        eventDto.setTitle(event.getTitle());
        eventDto.setDescription(event.getDescription());
        eventDto.setDetails(event.getDetails());
        eventDto.setLocation(event.getLocation());
        eventDto.setAddress(event.getAddress());
        eventDto.setStartDateTime(event.getStartDateTime());
        eventDto.setUpvote(event.getUpvote());

        if (event.getCreator() != null) {
            eventDto.setCreatorId(event.getCreator().getId());
        }
        if(event.getVolunteers() != null) {
            eventDto.setVolunteerCount(event.getVolunteers().size());
        }

        return eventDto;
    }

    public static Event mapEventDtoToEvent(EventDto eventDto) {
        Event event = new Event();
        event.setId(eventDto.getId());
        event.setCategory(EventCategory.valueOf(eventDto.getCategory()));
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setDescription(eventDto.getDetails());
        event.setLocation(eventDto.getLocation());
        event.setAddress(eventDto.getAddress());
        event.setStartDateTime(eventDto.getStartDateTime());
        event.setUpvote(eventDto.getUpvote());

        // ovo je samo placeholder, treba u service napravit lookup, iako TO SVE bi se svakako trebalo overrideati s obzirom da ce krator biti ulogirani korisnik
        if (eventDto.getCreatorId() != null) {
            UserEntity creator = new UserEntity();
            creator.setId(eventDto.getCreatorId());
            event.setCreator(creator);
        }

        //ovo treba biti lookup u servisu iako to se nece tako ni spremati tako da je ovo vjv dovoljno
        event.setVolunteers(new HashSet<>());

        return event;
    }


    public static HomePageDto mapEventToHomePageDto(Event event) {
        HomePageDto homePageDto = new HomePageDto();
        homePageDto.setId(event.getId());
        homePageDto.setTitle(event.getTitle());
        homePageDto.setLocation(event.getLocation());
        homePageDto.setStartDateTime(event.getStartDateTime());

        return homePageDto;
    }

    public static Event mapNewEventDtoToEvent(NewEventDto newEventDto) {
        Event event = new Event();
        event.setCategory(EventCategory.valueOf(newEventDto.getCategory()));
        event.setTitle(newEventDto.getTitle());
        event.setDescription(newEventDto.getDescription());
        event.setDetails(newEventDto.getDetails());
        event.setLocation(newEventDto.getLocation());
        event.setAddress(newEventDto.getAddress());
        event.setStartDateTime(newEventDto.getStartDateTime());

        return event;
    }

    public static SearchEventDto mapEventToSearchEventDto(Event event) {
        SearchEventDto searchEventDto = new SearchEventDto();
        searchEventDto.setId(event.getId());
        searchEventDto.setCategory(event.getCategory());
        searchEventDto.setTitle(event.getTitle());
        searchEventDto.setLocation(event.getLocation());
        searchEventDto.setStartDateTime(event.getStartDateTime());

        searchEventDto.setCreatorId(event.getCreator().getId());
        searchEventDto.setCreatorProfileImageURL(event.getCreator().getProfilePicturePath());

        return searchEventDto;
    }
}
