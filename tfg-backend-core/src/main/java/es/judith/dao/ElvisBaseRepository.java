package es.judith.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import es.judith.domain.ElvisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.NoRepositoryBean;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Interface to mark every spring data jpa repository.
 *
 * @author rbarroso
 */
@NoRepositoryBean
public interface ElvisBaseRepository<T extends ElvisEntity, I extends Serializable, Q extends EntityPathBase<T>>
    extends JpaRepository<T, I>, JpaSpecificationExecutor<T>, QuerydslPredicateExecutor<T>,
    QuerydslBinderCustomizer<Q> {

  @Override
  @Nonnull
  Optional<T> findById(@Nullable I id);

  @Override
  @Nonnull
  List<T> findAll();

  @Override
  @Nonnull
  List<T> findAll(@Nullable Predicate predicate);

  @Override
  @SuppressWarnings("all")
  default void customize(@Nullable final QuerydslBindings bindings, @Nullable final Q q) {
    if (bindings != null) {
      bindings.bind(String.class)
          .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }
  }
}

