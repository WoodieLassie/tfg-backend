package es.alten.web.impl;

import java.io.Serializable;
import java.lang.reflect.Field;

import es.alten.web.i18n.CustomReloadableResourceBundleMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.querydsl.core.types.dsl.EntityPathBase;
import es.alten.bo.GenericCRUDService;
import es.alten.domain.ElvisEntity;
import es.alten.dto.BaseFilterDTO;
import es.alten.utils.PageWrapper;
import es.alten.web.ListController;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;

/**
 * Implements interface {@link ListController}.
 *
 * @author irojas
 * @noinspection unused, WeakerAccess
 */
public abstract class ListControllerImpl<
        T extends ElvisEntity,
        I extends Serializable,
        B extends GenericCRUDService<T, I, Q, F>,
        Q extends EntityPathBase<T>,
        F extends BaseFilterDTO<T>>
    extends BaseControllerImpl implements ListController<T, I, B, Q, F> {

  /** serialVersionUID for object serialization */
  private static final long serialVersionUID = 1888014367419563856L;

  /** The Constant LOG. */
  private static final Logger LOG = LoggerFactory.getLogger(ListControllerImpl.class);

  private static final String ORDER_BY = "orderby";
  private static final String SEARCH = "search";
  private static final String LIST = "./list";

  /** View list name */
  protected String listView;

  /** Fields to export to excel */
  protected String[] exportFields;

  /**
   * Business object to delegate control operations logic
   *
   * @noinspection SpringJavaAutowiredMembersInspection
   */
  protected final B bo;

  protected ListControllerImpl(CustomReloadableResourceBundleMessageSource messageSource, B bo) {
    super(messageSource);
    this.bo = bo;
  }

  /** Init basic attributes to list controller. */
  @PostConstruct
  public void init() {
    LOG.debug("init");
    initDetails();
  }

  /** Implements module particular logic to initialize list view */
  public abstract void initDetails();

  /*
   * (non-Javadoc)
   *
   * @see es.alten.example.web.ListController#backEdit(es.alten.example.dto.
   * BaseFilterDTO, javax.servlet.http.HttpSession)
   */
  @Override
  @GetMapping(value = "/backEdit")
  public ModelAndView backEdit(@ModelAttribute F search, HttpSession httpSession) {
    LOG.debug("backEdit");
    ModelAndView modelAndView = new ModelAndView();
    Pageable pageable = (Pageable) httpSession.getAttribute("pageable");
    String orderBy = (String) httpSession.getAttribute(ORDER_BY);
    @SuppressWarnings("unchecked")
    F filter = (F) httpSession.getAttribute(SEARCH);
    if (filter != null) {
      if (filter.getClass().toString().equals(search.getClass().toString())) {
        search = filter;
      }

      modelAndView.addObject("page", this.populateFilter(search, pageable));
    } else {
      modelAndView.addObject("page", this.populateNew(pageable, orderBy));
    }
    modelAndView.addObject(SEARCH, search);
    modelAndView.setViewName(listView);

    return modelAndView;
  }

  /*
   * (non-Javadoc)
   *
   * @see es.alten.example.web.ListController#list(es.alten.example.dto.
   * BaseFilterDTO, org.springframework.data.domain.Pageable, org.springframework.ui.Model,
   * java.lang.String, javax.servlet.http.HttpSession)
   */
  @Override
  @GetMapping(value = "list")
  public ModelAndView list(
      @ModelAttribute F search,
      Pageable pageable,
      Model model,
      @RequestParam(value = ORDER_BY, required = false) String orderBy,
      HttpSession httpSession) {
    LOG.debug("list");
    ModelAndView modelAndView = new ModelAndView();

    httpSession.setAttribute(ORDER_BY, orderBy);
    @SuppressWarnings("unchecked")
    F filter = (F) httpSession.getAttribute(SEARCH);
    if (filter != null) {
      if (!filter.getClass().toString().equals(search.getClass().toString())) {
        /*
         * If search object received in this method is not equal to session search object, then user
         * changed to a different list view, in this case delete session object
         */
        httpSession.removeAttribute(SEARCH);
      } else {
        search = filter;
      }
      modelAndView.addObject("page", this.populateFilter(search, pageable));
    } else {
      modelAndView.addObject("page", this.populateNew(pageable, orderBy));
    }
    modelAndView.addObject(SEARCH, search);
    modelAndView.setViewName(listView);

    return modelAndView;
  }

  /*
   * (non-Javadoc)
   *
   * @see es.alten.example.web.ListController#search(es.alten.example.dto.
   * BaseFilterDTO, org.springframework.data.domain.Pageable, org.springframework.ui.Model,
   * java.lang.String, javax.servlet.http.HttpSession)
   */
  @Override
  @PostMapping(value = "/search")
  public ModelAndView search(
      @ModelAttribute F search,
      Pageable pageable,
      Model model,
      @RequestParam(value = ORDER_BY, required = false) String orderBy,
      HttpSession httpSession) {
    LOG.debug(SEARCH);
    ModelAndView modelAndView = new ModelAndView();

    httpSession.setAttribute(ORDER_BY, orderBy);

    modelAndView.setViewName(listView);
    httpSession.setAttribute(SEARCH, search);
    if (!isFiltered(search)) {
      httpSession.removeAttribute(SEARCH);
    }
    modelAndView.addObject(SEARCH, search);
    modelAndView.addObject("page", this.populateFilter(search, pageable));

    return modelAndView;
  }

  /**
   * Determines if a filter object contains data to filter.
   *
   * @param filter Filter object.
   * @return <code>true</code> if filter object contains data to filter, <code>false</code>
   *     otherwise.
   */
  protected boolean isFiltered(F filter) {
    LOG.debug("isFiltered");
    if (filter == null) {
      return false;
    }
    for (Field f : filter.getClass().getDeclaredFields()) {
      f.setAccessible(true);
      try {
        if (!f.getName().equals("serialVersionUID")
            && f.get(filter) != null
            && !f.get(filter).equals("")) {
          return true;
        }
      } catch (IllegalArgumentException | IllegalAccessException e1) {
        LOG.error("Error consulting isFiltered filter", e1);
      }
    }
    return false;
  }

  /**
   * Populates a new pageable list of objects.
   *
   * @param pageable Paging options to get objects list.
   * @param orderBy Ordering options to get objects list.
   * @return Pageable list of object with paging and ordering options.
   */
  protected PageWrapper<T> populateNew(Pageable pageable, String orderBy) {
    LOG.debug("populateNew");
    if (orderBy != null) {
      return new PageWrapper<>(bo.findAll(pageable), LIST, orderBy);
    } else {
      return new PageWrapper<>(bo.findAll(pageable), LIST, null);
    }
  }

  /**
   * Populates a filtered pageable list of objects.
   *
   * @param search Filtering options to get objects list.
   * @param pageable Paging options to get objects list.
   * @return Pageable list of object with paging and filtering options.
   */
  protected PageWrapper<T> populateFilter(F search, Pageable pageable) {
    LOG.debug("populateFilter");
    try {
      return new PageWrapper<>(bo.findByFilter(search, pageable), LIST, null);
    } catch (Exception e) {
      LOG.error("Error searching with the filter", e);
    }

    return null;
  }
}
