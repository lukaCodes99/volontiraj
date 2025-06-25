package hr.tvz.volontiraj.util;

import org.springframework.data.domain.Sort;

public class SortStrategyFactory {

    public static Sort getSort(String sortBy, boolean ascending) {
        Sort.Direction direction = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(direction, sortBy);
    }
}
