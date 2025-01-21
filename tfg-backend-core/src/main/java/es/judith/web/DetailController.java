package es.judith.web;

import java.io.Serializable;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.querydsl.core.types.dsl.EntityPathBase;
import es.judith.bo.GenericCRUDService;
import es.judith.domain.ElvisEntity;
import es.judith.dto.BaseFilterDTO;
import es.judith.dto.ElvisBaseDTO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Define detail views control logic operations.
 *
 * @param <T> Domain class type.
 * @param <N> Domain class identifier type.
 * @param <B> BO class type.
 * @param <D> DTO class type.
 * @param <F> DTO filter class type.
 * @author irojas
 * @noinspection unused
 */
public interface DetailController<T extends ElvisEntity, N extends Serializable, B extends GenericCRUDService<T, N, Q, F>, D extends ElvisBaseDTO<T>, Q extends EntityPathBase<T>, F extends BaseFilterDTO<T>>
    extends BaseController {

  /**
   * Base operation in a detail control logic. Forward the user to edit view in creation mode.
   *
   * @return Forward edit view URL.
   */
  String detail();

  /**
   * Create new object control operation. Forward the user to edit view in creation mode.
   *
   * @param map Model view map.
   * @return Forward edit view URL.
   */
  String detailNew(ModelMap map);

  /**
   * Edit existing object control operation. Forward the user to edit view.
   *
   * @param req Operation HTTP request.
   * @param modelMap Model view map.
   * @param model Model object.
   * @param id Object to edit identifier.
   * @return Forward edit view URL.
   */
  String detailEdit(HttpServletRequest req, ModelMap modelMap, Model model, N id);

  /**
   * Store new object control operation. Forward the user to list view.
   *
   * @param req Operation HTTP request.
   * @param dto New object data.
   * @param result Validation result object data.
   * @param redirectAttributes Object redirection attributes.
   * @param map Model object.
   * @return Forward list view URL if store is ok, no navigation if validation errors occurs.
   */
  String create(HttpServletRequest req, D dto, BindingResult result,
      final RedirectAttributes redirectAttributes, Model map);

  /**
   * Update existing object data control operation. Forward the user to list view.
   *
   * @param req Operation HTTP request.
   * @param id Object to update identifier.
   * @param dto Update object data.
   * @param result Validation result object data.
   * @param redirectAttributes Object redirection attributes.
   * @return Forward list view URL if update is ok, no navigation if validation errors occurs.
   */
  String update(HttpServletRequest req, N id, D dto, BindingResult result,
      final RedirectAttributes redirectAttributes);

  /**
   * Delete object control operation. Forward the user to list view.
   *
   * @param req Operation HTTP request.
   * @param id Object to delete identifier.
   * @param redirectAttributes Object redirection attributes.
   * @return Forward list view URL.
   */
  String delete(HttpServletRequest req, N id, final RedirectAttributes redirectAttributes);
}
