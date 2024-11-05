package es.alten.rest.impl;

import es.alten.exceptions.BadInputException;
import es.alten.rest.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements interface
 *
 * <p>{@link BaseController}.
 */
public abstract class BaseControllerImpl implements BaseController {

  /** serialVersionUID for object serialization. */
  private static final long serialVersionUID = -8209161763428335352L;

  /** class logger. */
  private static final Logger LOG = LoggerFactory.getLogger(BaseControllerImpl.class);

  /**
   * Validates paging parameters. If some parameter is not valid throws {@link BadInputException}.
   *
   * @param pageSize Page size. May be greater that zero.
   * @param pageNumber Page number. May be greater or equals zero.
   * @noinspection unused
   */
  protected void validatePaging(final int pageSize, final int pageNumber) {
    LOG.debug("validatePaging");
    if (pageSize <= 0) {
      LOG.warn("Requested paging with page size = {}", pageSize);
      throw new BadInputException();
    }
    if (pageNumber < 0) {
      LOG.warn("Requested paging with page number = {}", pageNumber);
      throw new BadInputException();
    }
  }
}
