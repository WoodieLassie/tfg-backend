package es.alten.dao;

import es.alten.domain.Image;
import es.alten.domain.QImage;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends ElvisBaseRepository<Image, Long, QImage>,
        JpaSpecificationExecutor<Image>,
        QuerydslPredicateExecutor<Image>,
        QuerydslBinderCustomizer<QImage> {
    @Query("SELECT i FROM Image i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Image> findByName(@Param("name") String name);
}
