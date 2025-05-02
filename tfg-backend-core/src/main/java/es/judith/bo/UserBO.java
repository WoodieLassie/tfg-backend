package es.judith.bo;

import es.judith.domain.QUser;
import es.judith.domain.User;
import es.judith.dto.UserFilterDTO;

/** Define services to work with Users. */
public interface UserBO extends GenericCRUDService<User, Long, QUser, UserFilterDTO> {
  User findByEmail(String email);
  byte[] findImageById(Long id);
}
