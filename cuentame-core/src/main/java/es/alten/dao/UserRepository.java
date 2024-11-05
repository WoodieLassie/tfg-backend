package es.alten.dao;

import es.alten.domain.QUser;
import es.alten.domain.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

/**
 * Repository interface for {@link User} instances. The interface is used to declare so called query
 * methods, methods to retrieve single entities or collections of them.
 */
public interface UserRepository
    extends ElvisBaseRepository<User, Long, QUser>,
        JpaSpecificationExecutor<User>,
        QuerydslPredicateExecutor<User>,
        QuerydslBinderCustomizer<QUser> {

  User findByEmail(String email);
}
