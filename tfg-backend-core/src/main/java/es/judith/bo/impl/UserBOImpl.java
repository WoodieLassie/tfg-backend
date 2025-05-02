package es.judith.bo.impl;

import es.judith.bo.UserBO;
import es.judith.dao.UserRepository;
import es.judith.domain.Character;
import es.judith.domain.QUser;
import es.judith.domain.User;
import es.judith.dto.UserFilterDTO;
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

  @Override
  public byte[] findImageById(Long id) {
    LOG.debug("CharacterBOImpl: findImageById");
    Optional<User> user = repository.findById(id);
    return user.map(image -> ImageUtil.decompressImage(image.getImageData())).orElse(null);
  }
}
