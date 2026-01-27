package es.judith.dao;

import es.judith.domain.Image;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends ElvisBaseRepository<Image, Long>,
        JpaSpecificationExecutor<Image> {
    @Query("SELECT i FROM Image i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Image> findByName(@Param("name") String name);
}
