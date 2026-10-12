package es.judith.dao;

import es.judith.domain.user.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for {@link User} instances. The interface is used to declare so called query
 * methods, methods to retrieve single entities or collections of them.
 */
public interface UserRepository
    extends GenericRepository<User, Long>,
        JpaSpecificationExecutor<User> {
  @Query(
          "SELECT u FROM User u "
                  + "WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')) ")
  List<User> findAllByUsername(@Param("username")String username);
  User findByEmail(String email);
  User findByUsername(String username);
}
