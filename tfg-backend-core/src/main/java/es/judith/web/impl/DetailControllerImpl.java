package es.judith.web.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

import es.judith.web.i18n.CustomReloadableResourceBundleMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.querydsl.core.types.dsl.EntityPathBase;
import es.judith.bo.GenericCRUDService;
import es.judith.domain.ElvisEntity;
import es.judith.dto.BaseFilterDTO;
import es.judith.dto.ElvisBaseDTO;
import es.judith.web.DetailController;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


/**
 * Implements interface {@link DetailController}.
 *
 * @author irojas
 * @noinspection WeakerAccess, unused
 */
public abstract class DetailControllerImpl<T extends ElvisEntity, N extends Serializable, B extends GenericCRUDService<T, N, Q, F>, D extends ElvisBaseDTO<T>, Q extends EntityPathBase<T>, F extends BaseFilterDTO<T>>
    extends BaseControllerImpl implements DetailController<T, N, B, D, Q, F> {

  /** serialVersionUID for object serialization */
  private static final long serialVersionUID = -5947984618763222413L;
  /** The Constant LOG. */
  private static final Logger LOG = LoggerFactory.getLogger(DetailControllerImpl.class);

  private static final String REDIRECT = "redirect:";
  private static final String BACK_EDIT = "/backEdit";
  private static final String FORM_FAIL = "form.fail";

  /** DTO object to show / edit in detail */
  protected D dto;
  /** DTO object class to store form data */
  private Class<D> dtoType;
  /** Base controller URI */
  protected String baseUri;
  /** Define if controller allow to create new objects */
  protected Boolean allowCreate = false;

  /**
   * Business object to delegate control operations logic
   *
   * @noinspection SpringJavaAutowiredMembersInspection
   */
  protected final B bo;

  protected DetailControllerImpl(CustomReloadableResourceBundleMessageSource messageSource, B bo) {
    super(messageSource);
    this.bo = bo;
  }

  /** Init basic attributes to detail controller. */
  @SuppressWarnings("unchecked")
  @PostConstruct
  public void init() {
    LOG.debug("init");
    // Gets form type used by child controller
    ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();

    dtoType = (Class<D>) genericSuperclass
        .getActualTypeArguments()[genericSuperclass.getActualTypeArguments().length - 3];

    initDetails();
  }

  /** Implements module particular logic to initialize detail view */
  public abstract void initDetails();

  /**
   * Gets the dto.
   *
   * @return the dto
   */
  public D getDto() {
    return this.dto;
  }

  /**
   * Sets the dto.
   *
   * @param dto the new dto
   */
  public void setDto(D dto) {
    this.dto = dto;
  }

  /*
   * (non-Javadoc)
   *
   * @see es.judith.example.web.DetailController#detail()
   */
  @GetMapping
  @Override
  public String detail() {
    LOG.debug("detail");
    return REDIRECT + baseUri + "/detail/new";
  }

  /*
   * (non-Javadoc)
   *
   * @see es.judith.example.web.DetailController#detailNew(org.springframework.ui.
   * ModelMap)
   */
  @GetMapping(value = "/new")
  @Override
  public String detailNew(ModelMap map) {
    LOG.debug("detailNew");
    if (Boolean.TRUE.equals(allowCreate)) {
      try {
        if (!map.containsAttribute(FORM_KEY) || map.get(ERROR_KEY) == null) {
          map.addAttribute(FORM_KEY, (dtoType.getDeclaredConstructor().newInstance()));
        }
      } catch (InstantiationException | IllegalAccessException e) {
        LOG.error(String.format("Unable to instantiate an object of type %s", dtoType.getName()));
      } catch (Exception e) {
        LOG.error("Error to initialize form to URI {}/new", baseUri);
      }
      return baseUri + "/detail";
    }

    LOG.info("Permission denied to view {}/detail/new", baseUri);
    return REDIRECT + baseUri + "/list";
  }

  /*
   * (non-Javadoc)
   *
   * @see es.judith.example.web.DetailController#create(javax.servlet.http.
   * HttpServletRequest, es.judith.example.dto.BaseDTO,
   * org.springframework.validation.BindingResult,
   * org.springframework.web.servlet.mvc.support.RedirectAttributes, org.springframework.ui.Model)
   */
  @PostMapping(value = "/new")
  @Override
  public String create(HttpServletRequest req, @Valid D dto, BindingResult result,
      final RedirectAttributes redirectAttributes, Model map) {
    LOG.debug("create");
    return saveEntity(dto.obtainDomainObject(), redirectAttributes, result, req);
  }

  private String saveEntity(T entity, final RedirectAttributes redirectAttributes,
      BindingResult result, HttpServletRequest req) {
    if (result.hasErrors()) {
      redirectAttributes.addFlashAttribute(ERROR_KEY, result);
      redirectAttributes.addFlashAttribute(FORM_KEY, dto);
      return backToReferer(req);
    } else {
      try {
        if (bo.save(entity) != null) {
          this.addMessage(messageSource.getMessage("saved", req));
          redirectAttributes.addFlashAttribute(MESSAGES_KEY, this.messages);
        } else {
          result.reject("", messageSource.getMessage(FORM_FAIL, req));

          redirectAttributes.addFlashAttribute(ERROR_KEY, result);
          redirectAttributes.addFlashAttribute(FORM_KEY, dto);

          return backToReferer(req);
        }
      } catch (DataIntegrityViolationException e) {
        LOG.error("Error saving form", e);
        result.reject("", messageSource.getMessage("duplicate.entity", req));

        redirectAttributes.addFlashAttribute(ERROR_KEY, result);
        redirectAttributes.addFlashAttribute(FORM_KEY, dto);
        return backToReferer(req);
      } catch (Exception e) {
        LOG.error("Error saving form.", e);
        result.reject("", messageSource.getMessage(FORM_FAIL, req));

        redirectAttributes.addFlashAttribute(ERROR_KEY, result);
        redirectAttributes.addFlashAttribute(FORM_KEY, dto);
        return backToReferer(req);
      }
    }
    return backToReferer(req);
  }

  /*
   * (non-Javadoc)
   *
   * @see es.judith.example.web.DetailController#update(javax.servlet.http.
   * HttpServletRequest, java.io.Serializable, es.judith.example.dto.BaseDTO,
   * org.springframework.validation.BindingResult,
   * org.springframework.web.servlet.mvc.support.RedirectAttributes)
   */
  @Override
  @PostMapping(value = "{id}")
  public String update(HttpServletRequest req, @PathVariable N id,
      @Valid @ModelAttribute(FORM_KEY) D dto, BindingResult result,
      final RedirectAttributes redirectAttributes) {
    LOG.debug("update");
    return saveEntity(dto.obtainDomainObject(), redirectAttributes, result, req);
  }

  /*
   * (non-Javadoc)
   *
   * @see es.judith.example.web.DetailController#detailEdit(javax.servlet.http.
   * HttpServletRequest, org.springframework.ui.ModelMap, org.springframework.ui.Model,
   * java.io.Serializable)
   */
  @GetMapping(value = "/{id}")
  @Override
  public String detailEdit(HttpServletRequest req, ModelMap modelMap, Model model,
      @PathVariable("id") N id) {
    LOG.debug("detailEdit");
    String responseUrl = baseUri + "/detail";
    if (!modelMap.containsAttribute(FORM_KEY) || modelMap.get(ERROR_KEY) == null) {
      T domain = bo.findOne(id);
      if (domain != null) {
        try {
          dto = dtoType.getDeclaredConstructor().newInstance();
          dto.loadFromDomain(domain);
          modelMap.addAttribute(FORM_KEY, dto);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
            | InvocationTargetException exception) {
          LOG.error(String.format("Unable to instantiate an object of type %s", dtoType.getName()));
        }
      } else {
        LOG.error("Don't exists object with id {} in data store.", id);
        responseUrl = REDIRECT + baseUri + "/list";
      }
    }
    return responseUrl;
  }

  /**
   * Return a redirect string used by spring MVC, to return previous page.
   *
   * @param req object request
   * @return redirect string
   */
  protected String backToReferer(HttpServletRequest req) {
    LOG.debug("backToReferer");
    return REDIRECT + req.getHeader("Referer");
  }

  /*
   * (non-Javadoc)
   *
   * @see es.judith.example.web.DetailController#delete(javax.servlet.http.
   * HttpServletRequest, java.io.Serializable,
   * org.springframework.web.servlet.mvc.support.RedirectAttributes)
   */
  @GetMapping(value = "/delete/{id}")
  @Override
  public String delete(HttpServletRequest req, @PathVariable N id,
      final RedirectAttributes redirectAttributes) {
    LOG.debug("delete");
    String responseUrl = REDIRECT + baseUri + BACK_EDIT;

    T domain = bo.findOne(id);
    if (domain != null) {
      try {
        bo.delete(domain);
        this.addMessage(messageSource.getMessage("deleted", req));
        redirectAttributes.addFlashAttribute(MESSAGES_KEY, this.messages);
      } catch (Exception e) {
        DirectFieldBindingResult result = new DirectFieldBindingResult(null, "entity");
        result.reject(messageSource.getMessage("delete.fail", req));
        redirectAttributes.addFlashAttribute(ERROR_KEY, result);
        LOG.error("Error deleting entity", e);
      }
    }

    return responseUrl;
  }
}
