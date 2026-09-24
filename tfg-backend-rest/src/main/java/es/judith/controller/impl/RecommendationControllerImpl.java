package es.judith.controller.impl;

import es.judith.bo.*;
import es.judith.controller.RecommendationController;
import es.judith.domain.Recommendation;
import es.judith.domain.Show;
import es.judith.domain.User;
import es.judith.dto.RecommendationInputDTO;
import es.judith.dto.ShowDTO;
import es.judith.dto.UserProfileDTO;
import es.judith.exceptions.AlreadyExistsException;
import es.judith.exceptions.NotExistingIdException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/recommendations")
@Tag(name = "recommendations")
public class RecommendationControllerImpl implements RecommendationController {

    private final RecommendationBO recommendationBO;
    private final transient AuthBO authBO;
    private static final Logger LOG = LoggerFactory.getLogger(RecommendationControllerImpl.class);
    private final transient UserBO userBO;
    private final transient FriendBO friendBO;
    private final transient ShowBO showBO;

    public RecommendationControllerImpl(RecommendationBO recommendationBO, AuthBO authBO, UserBO userBO, FriendBO friendBO, ShowBO showBO) {
        this.recommendationBO = recommendationBO;
        this.authBO = authBO;
        this.userBO = userBO;
        this.friendBO = friendBO;
        this.showBO = showBO;
    }

    @GetMapping("/sent")
    @Override
    public ResponseEntity<Map<Long, ArrayList>> findAllSentRecommendations() {
        User currentUser = authBO.getCurrentUser();
        Map<Long, ArrayList> sentRecommendations = recommendationBO.getAllSentRecommendations(currentUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(sentRecommendations);
    }

    @GetMapping("/received")
    @Override
    public ResponseEntity<Map<Long, ArrayList>> findAllReceivedRecommendations() {
        User currentUser = authBO.getCurrentUser();
        Map<Long, ArrayList> receivedRecommendations = recommendationBO.getAllReceivedRecommendations(currentUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(receivedRecommendations);
    }
    @PostMapping
    @Override
    public ResponseEntity<Recommendation> sendRecommendation(@RequestBody RecommendationInputDTO recommendationDTO) {
        User currentUser = authBO.getCurrentUser();
        User sentToUser = userBO.findOne(recommendationDTO.getUserReceiverId());
        Show recommendedShow = showBO.findOne(recommendationDTO.getShowId());
        if (sentToUser == null) {
            throw new NotExistingIdException(
                    "User with id " + recommendationDTO.getUserReceiverId() + " does not exist"
            );
        }
        if (recommendedShow == null) {
            throw new NotExistingIdException(
                    "Show with id " + recommendationDTO.getShowId() + " does not exist"
            );
        }
        if (!friendBO.checkIfFriend(currentUser.getId(), recommendationDTO.getUserReceiverId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        if (recommendationBO.checkIfRecommended(currentUser.getId(), recommendationDTO.getUserReceiverId(), recommendationDTO.getShowId())) {
            throw new AlreadyExistsException(
                    "Show already recommended to user"
            );
        }
        Recommendation recommendation = recommendationDTO.obtainDomainObject();
        recommendation.setUserReceiver(sentToUser);
        recommendation.setShow(recommendedShow);
        recommendation.setUserSender(currentUser);
        recommendationBO.save(recommendation);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Override
    @DeleteMapping("/{recommendationId}")
    public ResponseEntity<Recommendation> delete(@PathVariable Long recommendationId) {
        User currentUser = authBO.getCurrentUser();
        Recommendation recommendation = recommendationBO.findOne(recommendationId);
        if (recommendation == null) {
            throw new NotExistingIdException(
                    "Recommendation with id " + recommendationId + " does not exist"
            );
        }
        if (!Objects.equals(currentUser.getId(), recommendation.getUserSender().getId()) && !Objects.equals(currentUser.getId(), recommendation.getUserReceiver().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        recommendationBO.delete(recommendationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
