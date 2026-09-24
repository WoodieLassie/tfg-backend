package es.judith.bo.impl;

import es.judith.bo.RecommendationBO;
import es.judith.dao.RecommendationRepository;
import es.judith.dao.ShowRepository;
import es.judith.dao.UserRepository;
import es.judith.domain.Friend;
import es.judith.domain.Recommendation;
import es.judith.domain.Show;
import es.judith.domain.User;
import es.judith.dto.ShowDTO;
import es.judith.dto.UserProfileDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class RecommendationBOImpl extends GenericBOImpl<Recommendation, Long, RecommendationRepository> implements RecommendationBO {

    private final transient UserRepository userRepository;
    private final transient ShowRepository showRepository;
    public RecommendationBOImpl(RecommendationRepository repository, UserRepository userRepository, ShowRepository showRepository) {
        super(repository);
        this.userRepository = userRepository;
        this.showRepository = showRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, ArrayList> getAllSentRecommendations(Long userId) {
        List<Recommendation> sentRecommendations = this.repository.getAllSentRecommendations(userId);
        Map<Long, Long> recommendationSentToIds = new HashMap<>();
        for (Recommendation sentRecommendation : sentRecommendations) {
            recommendationSentToIds.put(sentRecommendation.getShow().getId(), sentRecommendation.getUserReceiver().getId());
        }
        ArrayList userAndShowInfo = convertToDTO(recommendationSentToIds);
        Map<Long, ArrayList> recommendationsWithAllInfo = new HashMap<>();
        int counter = 0;
        for (Recommendation sentRecommendation : sentRecommendations) {
            recommendationsWithAllInfo.put(sentRecommendation.getId(), (ArrayList) userAndShowInfo.get(counter));
            counter++;
        }
        return recommendationsWithAllInfo;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, ArrayList> getAllReceivedRecommendations(Long userId) {
        List<Recommendation> receivedRecommendations = this.repository.getAllReceivedRecommendations(userId);
        Map<Long, Long> recommendationReceivedFromIds = new HashMap<>();
        for (Recommendation receivedRecommendation : receivedRecommendations) {
            recommendationReceivedFromIds.put(receivedRecommendation.getShow().getId(), receivedRecommendation.getUserSender().getId());
        }
        ArrayList userAndShowInfo = convertToDTO(recommendationReceivedFromIds);
        Map<Long, ArrayList> recommendationsWithAllInfo = new HashMap<>();
        int counter = 0;
        for (Recommendation receivedRecommendation : receivedRecommendations) {
            recommendationsWithAllInfo.put(receivedRecommendation.getId(), (ArrayList) userAndShowInfo.get(counter));
            counter++;
        }
        return recommendationsWithAllInfo;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkIfRecommended(Long senderId, Long receiverId, Long showId) {
        return this.repository.getBySenderAndReceiverAndShowId(senderId, receiverId, showId) != null;
    }

    @Override
    public ArrayList convertToDTO(Map<Long, Long> recommendations) {
        ArrayList allRecommendationInfo = new ArrayList<>();
        for(Map.Entry<Long, Long> recommendation : recommendations.entrySet()){
            UserProfileDTO userDTO = new UserProfileDTO();
            ShowDTO showDTO = new ShowDTO();
            ArrayList userAndShowInformation = new ArrayList<>();
            Optional<User> optionalUser = userRepository.findById(recommendation.getValue());
            Optional<Show> optionalShow = showRepository.findById(recommendation.getKey());
            optionalUser.ifPresent(userDTO::loadFromDomain);
            optionalShow.ifPresent(showDTO::loadFromDomain);
            userAndShowInformation.add(userDTO);
            userAndShowInformation.add(showDTO);
            allRecommendationInfo.add(userAndShowInformation);
        }
        return allRecommendationInfo;
    }
}
