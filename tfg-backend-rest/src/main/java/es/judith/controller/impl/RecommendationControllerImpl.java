package es.judith.controller.impl;

import es.judith.bo.AuthBO;
import es.judith.bo.FriendBO;
import es.judith.bo.RecommendationBO;
import es.judith.bo.UserBO;
import es.judith.controller.RecommendationController;
import es.judith.dto.ShowDTO;
import es.judith.dto.UserProfileDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/recommendations")
@Tag(name = "recommendations")
public class RecommendationControllerImpl implements RecommendationController {

    private final RecommendationBO recommendationBO;
    private final AuthBO authBO;
    private static final Logger LOG = LoggerFactory.getLogger(RecommendationControllerImpl.class);
    private final UserBO userBO;

    public RecommendationControllerImpl(RecommendationBO recommendationBO, AuthBO authBO, UserBO userBO) {
        this.recommendationBO = recommendationBO;
        this.authBO = authBO;
        this.userBO = userBO;
    }

    @GetMapping
    public ResponseEntity<Map<Long, List>> findAllSentRecommendations(Long userId) {
        Long currentUserId = authBO.getCurrentUser().getId();
        Map<Long, List> sentRecommendations = recommendationBO.getAllSentRecommendations(currentUserId);
        return ResponseEntity.status(HttpStatus.OK).body(sentRecommendations);
    }
}
