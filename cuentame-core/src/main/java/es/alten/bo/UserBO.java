package es.alten.bo;

import es.alten.domain.QUser;
import es.alten.domain.User;
import es.alten.dto.UserFilterDTO;

/** Define services to work with Users. */
public interface UserBO extends GenericCRUDService<User, Long, QUser, UserFilterDTO> {
  User findByEmail(String email);
}
