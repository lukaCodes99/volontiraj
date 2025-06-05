package hr.tvz.volontiraj.mapper;

import hr.tvz.volontiraj.dto.EventImageDto;
import hr.tvz.volontiraj.model.Event;
import hr.tvz.volontiraj.model.EventImage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventImageMapper {

    public static EventImage mapImageURLToEventImage(Event event, String imageURL) {
        EventImage eventImage = new EventImage();
        eventImage.setEvent(event);
        eventImage.setImagePath(imageURL);
        return eventImage;
    }

    public static EventImageDto mapEventImageToEventImageDto(EventImage eventImage) {
        EventImageDto eventImageDto = new EventImageDto();
        eventImageDto.setId(eventImage.getId());
        eventImageDto.setImagePath(eventImage.getImagePath());
        return eventImageDto;
    }
}
