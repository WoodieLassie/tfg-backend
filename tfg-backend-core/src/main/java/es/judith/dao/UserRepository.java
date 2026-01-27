package es.judith.dao;

import es.judith.domain.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository interface for {@link User} instances. The interface is used to declare so called query
 * methods, methods to retrieve single entities or collections of them.
 */
public interface UserRepository
    extends ElvisBaseRepository<User, Long>,
        JpaSpecificationExecutor<User> {

  User findByEmail(String email);
}
