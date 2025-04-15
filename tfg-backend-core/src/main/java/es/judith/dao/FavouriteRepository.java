package es.judith.dao;

import es.judith.domain.Favourite;
import es.judith.domain.QFavourite;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavouriteRepository
    extends ElvisBaseRepository<Favourite, Long, QFavourite>,
        JpaSpecificationExecutor<Favourite>,
        QuerydslPredicateExecutor<Favourite>,
        QuerydslBinderCustomizer<QFavourite> {
  @Query(
      nativeQuery = true,
      value = "SELECT f.*, s.id AS id2, s.name FROM favourites f LEFT JOIN shows s ON f.show_id = s.id WHERE f.create_user_id = :userId")
  List<Favourite> findAllByUser(@Param("userId") Long userId);
}
