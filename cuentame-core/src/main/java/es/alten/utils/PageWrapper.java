package es.alten.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO to wrap {@link Page} interface to get display only a number of pages.
 * http://www.javacodegeeks.com/2013/03/implement-bootstrap-pagination-with-
 * spring-data-and-thymeleaf.html
 *
 * @param <T> the generic type
 * @author jlosorno
 */
@Data
public class PageWrapper<T> implements Serializable {

  /** serialVersionUID for object serialization. */
  private static final long serialVersionUID = 4203568659934641278L;

  /** The page. */
  private transient Page<T> page;

  /** The items. */
  private transient List<PageItem> items;

  /** The current number. */
  private int currentNumber;

  /** The url. */
  private String url;

  /** The sort. */
  private String sort;

  /**
   * Instantiates a new page wrapper.
   *
   * @param page the page
   * @param url the url
   * @param orderBy the order by
   */
  public PageWrapper(Page<T> page, String url, String orderBy) {
    this.page = page;
    this.url = url;

    if (orderBy == null) {
      this.sort = page.getSort().toString().replace(":", ",").replaceAll("\\s+", "");
    } else {
      this.sort = orderBy;
    }

    items = new ArrayList<>();

    // start from 1 to match page parameter
    currentNumber = page.getNumber() + 1;

    int start;
    int size;
    if (page.getTotalPages() <= Constants.PAGES_PER_PAGINATION_BAR) {
      start = 1;
      size = page.getTotalPages();
    } else {
      if (currentNumber
          <= Constants.PAGES_PER_PAGINATION_BAR - Constants.PAGES_PER_PAGINATION_BAR / 2) {
        start = 1;
        size = Constants.PAGES_PER_PAGINATION_BAR;
      } else if (currentNumber >= page.getTotalPages() - Constants.PAGES_PER_PAGINATION_BAR / 2) {
        start = page.getTotalPages() - Constants.PAGES_PER_PAGINATION_BAR + 1;
        size = Constants.PAGES_PER_PAGINATION_BAR;
      } else {
        start = currentNumber - Constants.PAGES_PER_PAGINATION_BAR / 2;
        size = Constants.PAGES_PER_PAGINATION_BAR;
      }
    }

    for (int i = 0; i < size; i++) {
      items.add(new PageItem(start + i, (start + i) == currentNumber));
    }
  }

  /** The Class PageItem. */
  @Data
  @AllArgsConstructor
  public class PageItem {

    /** The number. */
    protected int number;

    /** The current. */
    protected boolean current;
  }
}
