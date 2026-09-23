package es.judith.bo;

import es.judith.domain.Recommendation;
import es.judith.dto.ShowDTO;
import es.judith.dto.UserProfileDTO;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface RecommendationBO extends GenericBO<Recommendation, Long> {
    Map<Long, List> getAllSentRecommendations(Long userId);
//    Map<Long, Map<UserProfileDTO, ShowDTO>> getAllReceivedRecommendations(Long userId);
    boolean checkIfRecommended(Long senderId, Long receiverId, Long showId);
    Map<String, List> convertToDTO(Map<Long, Long> recommendations);
}
