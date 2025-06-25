package hr.tvz.volontiraj.mapper;

import hr.tvz.volontiraj.dto.EventImageDto;
import hr.tvz.volontiraj.mapper.EventImageMapper;
import hr.tvz.volontiraj.model.Event;
import hr.tvz.volontiraj.model.EventImage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventImageMapperTest {

    @Test
    void mapEventImageToEventImageDto_shouldMapIdAndImagePath() {
        EventImage eventImage = new EventImage();
        eventImage.setId(1L);
        eventImage.setImagePath("/images/sample.jpg");

        EventImageDto dto = EventImageMapper.mapEventImageToEventImageDto(eventImage);

        assertEquals(eventImage.getId(), dto.getId());
        assertEquals(eventImage.getImagePath(), dto.getImagePath());
    }
}
