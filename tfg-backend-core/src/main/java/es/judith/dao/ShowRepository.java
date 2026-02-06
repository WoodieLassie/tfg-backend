package es.judith.dao;

import es.judith.domain.Show;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShowRepository extends ElvisBaseRepository<Show, Long>,
        JpaSpecificationExecutor<Show> {

    @Query("SELECT s FROM Show s LEFT JOIN FETCH s.seasons WHERE s.id = :id")
    Optional<Show> findById(@Param("id") Long id);
    @Query("SELECT s FROM Show s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY s.id")
    List<Show> findAllByName(@Param("name") String name);
    @Query("DELETE FROM Season s WHERE s.show.id = :showId")
    @Modifying
    void deleteAllSeasons(@Param("showId") Long showId);
}
