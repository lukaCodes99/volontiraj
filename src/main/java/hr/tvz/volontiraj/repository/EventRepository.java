package hr.tvz.volontiraj.repository;

import hr.tvz.volontiraj.filterParams.EventFilterParams;
import hr.tvz.volontiraj.model.Event;
import hr.tvz.volontiraj.model.EventCategory;
import org.springframework.data.domain.ManagedTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {


    @Query("SELECT e FROM Event e " +
            "WHERE (:categoryParam IS NULL OR e.category = :categoryParam) " +
            "AND (:title IS NULL OR e.title LIKE %:title%) " +
            "AND (:description IS NULL OR e.description LIKE %:description%) " +
            "AND (:location IS NULL OR e.location LIKE %:location%) " +
            "AND (:startDateTimeFrom IS NULL OR e.startDateTime >= :startDateTimeFrom) " +
            "AND (:startDateTimeTo IS NULL OR e.startDateTime <= :startDateTimeTo) " +
            "AND (:creatorId IS NULL OR e.creator.id = :creatorId)")
    Page<Event> findFilteredAndPaged(
            @Param("categoryParam") EventCategory category,
            @Param("title") String title,
            @Param("description") String description,
            @Param("location") String location,
            @Param("startDateTimeFrom") LocalDateTime startDateTimeFrom,
            @Param("startDateTimeTo") LocalDateTime startDateTimeTo,
            @Param("creatorId") Long creatorId,
            Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.category = 'PETS' "+
            "ORDER BY e.id DESC LIMIT 4 "+
            "UNION ALL "+
            "SELECT e FROM Event e WHERE e.category = 'PEOPLE' "+
            "ORDER BY e.id DESC LIMIT 4 ")
    List<Event> findLatestForHomePage();


    @Query("SELECT e.category, COUNT(e) FROM Event e GROUP BY e.category")
    List<Object[]> countEventsByCategory();

    @Query("SELECT e FROM Event e ORDER BY e.upvote DESC LIMIT 5")
    List<Event> findTop5ByOrderByUpvoteDesc();

    long countByCategory(EventCategory category);

    @Query("SELECT e FROM Event e " +
            "WHERE e.startDateTime >= :from " +
            "AND e.startDateTime <= :to")
    List<Event> findEventsWithinHour(@Param("from") LocalDateTime from,
                                     @Param("to") LocalDateTime to);
}
