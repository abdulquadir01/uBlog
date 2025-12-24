package dev.aq.ublog.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static dev.aq.ublog.constants.UBlogConstants.DEFAULT_SORT_DIRECTION;
import static dev.aq.ublog.constants.UBlogConstants.REVERSE_SORT_DIRECTION;

public class PageableUtils {

  private PageableUtils() {
    throw new IllegalStateException("Util class not to be instantiated.");
  }

  public static Pageable createSortedPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

//      Be cautious of this statement
//      !! CAUTION !! TBD - find a way to initialize sort with some other value than null
    Sort sort = null;

    if (sortDir.equalsIgnoreCase(DEFAULT_SORT_DIRECTION)) {
      sort = Sort.by(sortBy).ascending();
    } else if (sortDir.equalsIgnoreCase(REVERSE_SORT_DIRECTION)) {
      sort = Sort.by(sortBy).descending();
    }

//      !! CAUTION !! TBD - find a way to initialize sort with some other value than null
    assert sort != null;
    return PageRequest.of(pageNumber, pageSize, sort);
  }

}
