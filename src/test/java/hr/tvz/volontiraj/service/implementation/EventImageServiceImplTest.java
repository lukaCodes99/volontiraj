package hr.tvz.volontiraj.service.implementation;

import hr.tvz.volontiraj.model.Event;
import hr.tvz.volontiraj.model.EventImage;
import hr.tvz.volontiraj.repository.EventImageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventImageServiceImplTest {

    @Mock
    private EventImageRepository eventImageRepository;

    @InjectMocks
    private EventImageServiceImpl eventImageServiceImpl;

    @Test
    void save_ShouldReturnEventImage() {
        Event event = new Event();
        event.setId(1L);
        EventImage eventImage = new EventImage(1L, "imagePath", event);

        when(eventImageRepository.save(eventImage)).thenReturn(eventImage);

        EventImage result = eventImageServiceImpl.save(eventImage);

        assertEquals(eventImage, result);
        verify(eventImageRepository).save(eventImage);
    }

    @Test
    void findById_ShouldReturnEventImage() {
        Long imageId = 1L;
        Optional<EventImage> image = Optional.of(getEventImage(imageId));

        when(eventImageRepository.findById(imageId)).thenReturn(image);

        EventImage result = eventImageServiceImpl.findById(imageId);

        assertEquals(imageId, result.getId());
        verify(eventImageRepository).findById(imageId);
    }

    @Test
    void findById_ShouldThrowException_WhenEventImageIsNull() {
        Long id = 1L;

        when(eventImageRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> eventImageServiceImpl.findById(id)
        );

        assertEquals("Event image with id: 1 not found!", exception.getMessage());
        verify(eventImageRepository).findById(id);
    }

    @Test
    void findAllByEventId_ShouldReturnEventImage() {
        Long eventId = 1L;

        List<EventImage> eventImages = List.of(getEventImage(1L), getEventImage(2L));

        when(eventImageRepository.findAllByEventId(eventId)).thenReturn(eventImages);

        List<EventImage> result = eventImageServiceImpl.findAllByEventId(eventId);

        assertEquals(eventImages, result);
        verify(eventImageRepository).findAllByEventId(eventId);
    }

    @Test
    void deleteAllByEventId_ShouldDeleteEventImage() {
        Long eventId = 1L;
        Optional<EventImage> eventImage = Optional.of(getEventImage(1L));

        when(eventImageRepository.findByEventId(eventId)).thenReturn(eventImage);

        eventImageServiceImpl.deleteAllByEventId(eventId);

        verify(eventImageRepository).findByEventId(eventId);
        verify(eventImageRepository).deleteAllByEventId(eventId);
    }

    @Test
    void deleteAllByEventId_ShouldThrowException_WhenEventImageIsNull() {
        Long eventId = 1L;

        when(eventImageRepository.findByEventId(eventId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> eventImageServiceImpl.deleteAllByEventId(eventId)
        );

        assertEquals("Event images with event id: 1 not found!", exception.getMessage());
        verify(eventImageRepository).findByEventId(eventId);
        verify(eventImageRepository, never()).deleteAllByEventId(eventId);
    }

    @Test
    void deleteById_ShouldDeleteEventImage() {
        Long imageId = 1L;
        Optional<EventImage> eventImage = Optional.of(getEventImage(imageId));

        when(eventImageRepository.findById(imageId)).thenReturn(eventImage);

        eventImageServiceImpl.deleteById(imageId);

        verify(eventImageRepository).findById(imageId);
        verify(eventImageRepository).deleteById(imageId);
    }

    @Test
    void deleteById_ShouldThrowException_WhenEventImageIsNull() {
        Long id = 1L;

        when(eventImageRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> eventImageServiceImpl.deleteById(id)
        );

        assertEquals("Event Image with id: 1 not found!", exception.getMessage());
        verify(eventImageRepository).findById(id);
        verify(eventImageRepository, never()).deleteById(id);
    }


    private EventImage getEventImage(Long id) {
        Event event = new Event();
        event.setId(1L);
        return new EventImage(id, "imagePath/" + id, event);
    }

}
