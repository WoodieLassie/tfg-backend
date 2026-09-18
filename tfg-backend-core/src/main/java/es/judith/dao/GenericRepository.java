package es.judith.dao;

import es.judith.domain.GenericEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
public interface GenericRepository<T extends GenericEntity, I extends Serializable>
    extends JpaRepository<T, I>, JpaSpecificationExecutor<T> {

  @Override
  @Nonnull
  Optional<T> findById(@Nullable I id);

  @Override
  @Nonnull
  List<T> findAll();

}

