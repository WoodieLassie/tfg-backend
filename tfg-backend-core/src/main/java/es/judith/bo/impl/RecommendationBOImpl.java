package es.judith.bo.impl;

import es.judith.bo.RecommendationBO;
import es.judith.bo.ShowBO;
import es.judith.bo.UserBO;
import es.judith.dao.RecommendationRepository;
import es.judith.dao.ShowRepository;
import es.judith.dao.UserRepository;
import es.judith.domain.Recommendation;
import es.judith.domain.Show;
import es.judith.domain.user.User;
import es.judith.dto.show.ShowDTO;
import es.judith.dto.show.ShowNoSeasonsDTO;
import es.judith.dto.user.UserProfileDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class RecommendationBOImpl extends GenericBOImpl<Recommendation, Long, RecommendationRepository> implements RecommendationBO {

    private final transient UserBO userBO;
    private final transient ShowBO showBO;
    public RecommendationBOImpl(RecommendationRepository repository, UserBO userBO, ShowBO showBO) {
        super(repository);
        this.userBO = userBO;
        this.showBO = showBO;
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
            ShowNoSeasonsDTO showDTO = new ShowNoSeasonsDTO();
            ArrayList userAndShowInformation = new ArrayList<>();
            User user = userBO.findOne(recommendation.getValue());
            Show show = showBO.findOne(recommendation.getKey());
            userDTO.loadFromDomain(user);
            showDTO.loadFromDomain(show);
            userAndShowInformation.add(userDTO);
            userAndShowInformation.add(showDTO);
            allRecommendationInfo.add(userAndShowInformation);
        }
        return allRecommendationInfo;
    }
}
