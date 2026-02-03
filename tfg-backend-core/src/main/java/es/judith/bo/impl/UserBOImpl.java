package es.judith.bo.impl;

import es.judith.bo.UserBO;
import es.judith.dao.UserRepository;
import es.judith.domain.Role;
import es.judith.domain.User;
import es.judith.utils.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    extends ElvisGenericCRUDServiceImpl<User, Long, UserRepository>
    implements UserBO {

  private static final long serialVersionUID = -4166529873832767435L;
  private static final Logger LOG = LoggerFactory.getLogger(UserBOImpl.class);
  private final BCryptPasswordEncoder passwordEncoder;
    {
      new BCryptPasswordEncoder(10);
    }
  private final AuthenticationManager authenticationManager;

  public UserBOImpl(UserRepository repository, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
    super(repository);
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
  }
  
  @Override
  public User findByEmail(String email) {
    LOG.debug("UserBOImpl: findByEmail");
    return this.repository.findByEmail(email);
  }

  @Override
  public byte[] findImageById(Long id) {
    LOG.debug("UserBOImpl: findImageById");
    Optional<User> user = repository.findById(id);
    return user.map(image -> ImageUtil.decompressImage(image.getImageData())).orElse(null);
  }
  @Override
  public boolean verify(String email, String password) {
    LOG.debug("UserBOImpl: login");
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    return authentication.isAuthenticated();
  }

  @Override
  public String encryptPassword(String password) {
    LOG.debug("UserBOImpl: encryptPassword");
    return passwordEncoder.encode(password);
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
