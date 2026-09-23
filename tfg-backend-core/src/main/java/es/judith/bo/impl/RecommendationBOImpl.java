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
    public Map<Long, List> getAllSentRecommendations(Long userId) {
        List<Recommendation> sentRecommendations = this.repository.getAllSentRecommendations(userId);
        Map<Long, Long> recommendationSentToIds = new HashMap<>();
        for (Recommendation sentRecommendation : sentRecommendations) {
            recommendationSentToIds.put(sentRecommendation.getShow().getId(), sentRecommendation.getUserReceiver().getId());
        }
        Map<String, List> recommendationsWithShowAndUserInfo = convertToDTO(recommendationSentToIds);
        Map<Long, List> recommendationsWithAllInfo = new HashMap<>();
        int counter = 0;
        for (Recommendation sentRecommendation : sentRecommendations) {
            recommendationsWithAllInfo.put(sentRecommendation.getId(), recommendationsWithShowAndUserInfo.get(String.valueOf(counter)));
            counter++;
        }
        return recommendationsWithAllInfo;
    }

//    @Override
//    @Transactional(readOnly = true)
//    public Map<Long, Map<UserProfileDTO, ShowDTO>> getAllReceivedRecommendations(Long userId) {
//        List<Recommendation> receivedRecommendations = this.repository.getAllReceivedRecommendations(userId);
//        Map<Long, Long> recommendationReceivedByIds = new HashMap<>();
//        for (Recommendation receivedRecommendation : receivedRecommendations) {
//            recommendationReceivedByIds.put(receivedRecommendation.getId(), receivedRecommendation.getUserReceiver().getId());
//        }
//        return convertToDTO(recommendationReceivedByIds);
//    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkIfRecommended(Long senderId, Long receiverId, Long showId) {
        return this.repository.getBySenderAndReceiverAndShowId(senderId, receiverId, showId) != null;
    }

    @Override
    public Map<String, List> convertToDTO(Map<Long, Long> recommendations) {
        Map<String, List> recommendationsWithShowAndUserInfo = new LinkedHashMap<>();
        int counter = 0;
        for(Map.Entry<Long, Long> recommendation : recommendations.entrySet()){
            UserProfileDTO userDTO = new UserProfileDTO();
            ShowDTO showDTO = new ShowDTO();
            Optional<User> optionalUser = userRepository.findById(recommendation.getValue());
            Optional<Show> optionalShow = showRepository.findById(recommendation.getKey());
            optionalUser.ifPresent(userDTO::loadFromDomain);
            optionalShow.ifPresent(showDTO::loadFromDomain);
            Map<String, UserProfileDTO> userInfo = new HashMap<>();
            Map<String, ShowDTO> showInfo = new HashMap<>();
            userInfo.put("user", userDTO);
            showInfo.put("show", showDTO);
            List userAndShowInformation = new ArrayList<>();
            userAndShowInformation.add(userInfo);
            userAndShowInformation.add(showInfo);
            recommendationsWithShowAndUserInfo.put(String.valueOf(counter), userAndShowInformation);
            counter++;
        }
        return recommendationsWithShowAndUserInfo;
    }
}
