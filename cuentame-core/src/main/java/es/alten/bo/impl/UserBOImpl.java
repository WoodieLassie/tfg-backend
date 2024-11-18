package es.alten.bo.impl;

import es.alten.bo.UserBO;
import es.alten.dao.UserRepository;
import es.alten.domain.QUser;
import es.alten.domain.User;
import es.alten.dto.UserFilterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements interface {@link UserBO}.
 *
 * @noinspection unused
 */
@Service
@Transactional
public class UserBOImpl
    extends ElvisGenericCRUDServiceImpl<User, Long, QUser, UserFilterDTO, UserRepository>
    implements UserBO {

  private static final long serialVersionUID = -4166529873832767435L;
  private static final Logger LOG = LoggerFactory.getLogger(UserBOImpl.class);

  public UserBOImpl(UserRepository repository) {
    super(repository);
  } 
  
  @Override
  public User findByEmail(String email) {
    LOG.debug("UserBOImpl: findByEmail");
    return this.repository.findByEmail(email);
  }
}
