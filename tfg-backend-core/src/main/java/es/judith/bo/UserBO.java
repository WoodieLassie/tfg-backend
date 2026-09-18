package es.judith.bo;

import es.judith.domain.User;

/** Define services to work with Users. */
public interface UserBO extends GenericBO<User, Long> {
  User findByEmail(String email);
  User findByUsername(String username);
  byte[] findImageById(Long id);
  void promoteUser(Long id);
}
