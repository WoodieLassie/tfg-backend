package es.judith.bo.impl;

import es.judith.bo.RecommendationBO;
import es.judith.dao.RecommendationRepository;
import es.judith.dao.ShowRepository;
import es.judith.dao.UserRepository;
import es.judith.domain.Recommendation;
import es.judith.dto.UserProfileDTO;

import java.util.Map;

public class RecommendationBOImpl extends GenericBOImpl<Recommendation, Long, RecommendationRepository> implements RecommendationBO {

    private final transient UserRepository userRepository;
    private final transient ShowRepository showRepository;
    public RecommendationBOImpl(RecommendationRepository repository, UserRepository userRepository, ShowRepository showRepository, UserRepository userRepository1, ShowRepository showRepository1) {
        super(repository);
        this.userRepository = userRepository1;
        this.showRepository = showRepository1;
    }

    @Override
    public Map<Long, UserProfileDTO> getAllSentRecommendations(Long userId) {
        return Map.of();
    }

    @Override
    public Map<Long, UserProfileDTO> getAllReceivedRecommendations(Long userId) {
        return Map.of();
    }

    @Override
    public boolean checkIfRecommended(Long senderId, Long receiverId, Long showId) {
        return false;
    }

    @Override
    public Map<Long, UserProfileDTO> convertToDTO(Map<Long, Long> recommendations) {
        return Map.of();
    }
}
