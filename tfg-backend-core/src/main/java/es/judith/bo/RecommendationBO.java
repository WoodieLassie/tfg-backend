package es.judith.bo;

import es.judith.domain.Recommendation;
import es.judith.dto.ShowDTO;
import es.judith.dto.UserProfileDTO;

import java.util.*;

public interface RecommendationBO extends GenericBO<Recommendation, Long> {
    Map<Long, ArrayList> getAllSentRecommendations(Long userId);
    Map<Long, ArrayList> getAllReceivedRecommendations(Long userId);
    boolean checkIfRecommended(Long senderId, Long receiverId, Long showId);
    ArrayList convertToDTO(Map<Long, Long> recommendations);
}
