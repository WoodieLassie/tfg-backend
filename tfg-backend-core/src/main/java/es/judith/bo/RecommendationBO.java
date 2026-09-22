package es.judith.bo;

import es.judith.domain.Recommendation;
import es.judith.dto.UserProfileDTO;

import java.util.Map;

public interface RecommendationBO extends GenericBO<Recommendation, Long> {
    Map<Long, UserProfileDTO> getAllSentRecommendations(Long userId);
    Map<Long, UserProfileDTO> getAllReceivedRecommendations(Long userId);
    boolean checkIfRecommended(Long senderId, Long receiverId, Long showId);
    Map<Long, UserProfileDTO> convertToDTO(Map<Long, Long> recommendations);
}
