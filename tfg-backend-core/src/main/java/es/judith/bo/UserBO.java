package es.judith.bo;

import es.judith.domain.User;

/** Define services to work with Users. */
public interface UserBO extends GenericCRUDService<User, Long> {
  User findByEmail(String email);
  byte[] findImageById(Long id);
}
