package es.judith.bo.impl;

import es.judith.bo.UserBO;
import es.judith.dao.UserRepository;
import es.judith.domain.Role;
import es.judith.domain.user.User;
import es.judith.utils.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implements interface {@link UserBO}.
 *
 * @noinspection unused
 */
@Service
@Transactional
public class UserBOImpl
    extends GenericBOImpl<User, Long, UserRepository>
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

  @Override
  public User findByUsername(String username) {
    LOG.debug("UserBOImpl: findByUsername");
    return this.repository.findByUsername(username);
  }

  @Override
  public byte[] findImageById(Long id) {
    LOG.debug("UserBOImpl: findImageById");
    Optional<User> user = repository.findById(id);
    return user.map(image -> ImageUtil.decompressImage(image.getImageData())).orElse(null);
  }

  @Override
  public void promoteUser(Long id) {
    LOG.debug("UserBOImpl: promoteUser");
    Optional<User> user = repository.findById(id);
    if (user.isPresent()) {
      user.get().setRole(Role.ADMIN);
      repository.save(user.get());
    }
  }
}
