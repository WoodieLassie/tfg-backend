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

import java.io.Serial;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements interface {@link UserBO}.
 *
 * @noinspection unused
 */
@Service
@Transactional
public class UserBOImpl extends GenericBOImpl<User, Long, UserRepository> implements UserBO {

  @Serial
  private static final long serialVersionUID = -4166529873832767435L;
  private static final Logger LOG = LoggerFactory.getLogger(UserBOImpl.class);

  public UserBOImpl(UserRepository repository) {
    super(repository);
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> findAllByUsername(String username) {
    return repository.findAllByUsername(username);
  }

  @Override
  @Transactional(readOnly = true)
  public User findByEmail(String email) {
    LOG.debug("UserBOImpl: findByEmail");
    return this.repository.findByEmail(email);
  }

  @Override
  @Transactional(readOnly = true)
  public User findByUsername(String username) {
    LOG.debug("UserBOImpl: findByUsername");
    return this.repository.findByUsername(username);
  }

  @Override
  @Transactional(readOnly = true)
  public byte[] findImageById(Long id) {
    LOG.debug("UserBOImpl: findImageById");
    Optional<User> user = repository.findById(id);
    return user.map(image -> ImageUtil.decompressImage(image.getImageData())).orElse(null);
  }

  @Override
  @Transactional
  public void promoteUser(Long id) {
    LOG.debug("UserBOImpl: promoteUser");
    Optional<User> user = repository.findById(id);
    if (user.isPresent()) {
      user.get().setRole(Role.ADMIN);
      repository.save(user.get());
    }
  }

  public String checkForIllegalStrings(String email, String password, String username) {
    Pattern usernameRegex = Pattern.compile("[^A-Za-z0-9 ]");
    Pattern emailRegex = Pattern.compile("[^A-Za-z0-9 @.]");
    Pattern passwordRegex = Pattern.compile("[^A-Za-z0-9 @_!#%&*¿?.]");
    Matcher usernameMatcher = usernameRegex.matcher(username);
    Matcher emailMatcher = emailRegex.matcher(email);
    Matcher passwordMatcher = passwordRegex.matcher(password);
    if (emailMatcher.find() || passwordMatcher.find() || usernameMatcher.find()) {
      return "Username fields contain illegal characters";
    }
    if (password.length() < 8) {
      return "Password must be greater than 8";
    }
    if (email.length() > 100 || username.length() > 20 || password.length() > 100) {
      return "Username fields exceed max length";
    }
    return "";
  }
}
