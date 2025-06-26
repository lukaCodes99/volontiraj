package hr.tvz.volontiraj.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableFactory {

    public static Pageable create(int page, int size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }
}
