package hr.tvz.volontiraj.service;

import hr.tvz.volontiraj.model.EventImage;

import java.util.List;

public interface EventImageReadService {
    EventImage findById(Long id);

    List<EventImage> findAllByEventId(Long eventId);
}
