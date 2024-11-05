package es.alten.web;

import java.io.Serializable;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.querydsl.core.types.dsl.EntityPathBase;
import es.alten.bo.GenericCRUDService;
import es.alten.domain.ElvisEntity;
import es.alten.dto.BaseFilterDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Define list views control logic operations.
 *
 * @param <T> Domain class type.
 * @param <I> Domain class identifier type.
 * @param <B> BO class type.
 * @param <F> DTO filter class type.
 * @author irojas
 * @noinspection unused
 */
public interface ListController<T extends ElvisEntity, I extends Serializable, B extends GenericCRUDService<T, I, Q, F>, Q extends EntityPathBase<T>, F extends BaseFilterDTO<T>>
    extends BaseController {

  /**
   * Return from detail control operation. Forward the user to list view.
   *
   * @param search Filter data object.
   * @param httpSession Operation HTTP session.
   * @return Model data and view to forward user lo list view.
   */
  ModelAndView backEdit(@ModelAttribute F search, HttpSession httpSession);

  /**
   * List objects control operation.
   *
   * @param search Search filter data.
   * @param pageable Result table paging data.
   * @param model Model object.
   * @param orderBy Result table ordering data.
   * @param httpSession Operation HTTP session.
   * @return Model data and view to forward user lo list view.
   */
  ModelAndView list(@ModelAttribute F search, Pageable pageable, Model model,
      @RequestParam(value = "orderBy", required = false) String orderBy, HttpSession httpSession);

  /**
   * Search objects control operation.
   *
   * @param search Search filter data.
   * @param pageable Result table paging data.
   * @param model Model object.
   * @param orderBy Result table ordering data.
   * @param httpSession Operation HTTP session.
   * @return Model data and view to forward user lo list view.
   */
  ModelAndView search(@ModelAttribute F search, Pageable pageable, Model model,
      @RequestParam(value = "orderBy", required = false) String orderBy, HttpSession httpSession);

  /**
   * Export result table data to excel control operation.
   *
   * @param httpSession Operation HTTP session.
   * @param response Operation HTTP response.
   * @param search Search filter data.
   */
  void exportExcel(HttpSession httpSession, HttpServletResponse response, @ModelAttribute F search);
}
