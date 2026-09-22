package es.judith.dao;

import es.judith.domain.Recommendation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
//TODO: Ver en el perfil del propio usuario las recomendaciones hechas y las recibidas. No se verán en el perfil de otro usuario
public interface RecommendationRepository extends GenericRepository<Recommendation, Long>, JpaSpecificationExecutor<Recommendation> {
    @Query(
            nativeQuery = true,
            value = "SELECT r.* FROM recommendations r WHERE r.sender_id = :userId"
    )
    List<Recommendation> getAllSentRecommendations(@Param("userId") Long userId);
    @Query(
            nativeQuery = true,
            value = "SELECT r.* FROM recommendations r WHERE r.receiver_id = :userId"
    )
    List<Recommendation> getAllReceivedRecommendations(@Param("userId") Long userId);
    @Query(
            nativeQuery = true,
            value = "SELECT r.* FROM recommendations r WHERE r.sender_id = :senderId AND r.receiver_id = :receiverId AND r.show_id = :showId "
    )
    Recommendation getBySenderAndReceiverAndShowId(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId, @Param("showId") Long showId);
}
