package hr.tvz.volontiraj.service;

import hr.tvz.volontiraj.model.EventImage;

public interface EventImageWriteService {
    EventImage save(EventImage eventImage);

    void deleteAllByEventId(Long eventId);

    void deleteById(Long id);
}
