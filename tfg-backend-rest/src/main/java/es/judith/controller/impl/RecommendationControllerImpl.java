package es.judith.controller.impl;

import es.judith.bo.*;
import es.judith.controller.RecommendationController;
import es.judith.domain.Recommendation;
import es.judith.domain.Show;
import es.judith.domain.user.User;
import es.judith.dto.recommendation.RecommendationInputDTO;
import es.judith.dto.swagger.RecommendationSwaggerDTO;
import es.judith.exceptions.AlreadyExistsException;
import es.judith.exceptions.NotExistingIdException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serial;
import java.util.*;

@RestController
@RequestMapping("/api/recommendations")
@Tag(name = "recommendations")
public class RecommendationControllerImpl implements RecommendationController {

    @Serial
    private static final long serialVersionUID = 3595750705695577569L;
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
    @Operation(
            method = "GET",
            summary = "Get all sent recommendations")
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RecommendationSwaggerDTO.class))
            })
    @ApiResponse(
            responseCode = "404",
            description = "Not found",
            content = @Content(schema = @Schema(hidden = true)))
    @Override
    public ResponseEntity<Map<Long, ArrayList>> findAllSentRecommendations() {
        LOG.debug("RecommendationControllerImpl: Finding all sent recommendations");
        User currentUser = authBO.getCurrentUser();
        Map<Long, ArrayList> sentRecommendations = recommendationBO.getAllSentRecommendations(currentUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(sentRecommendations);
    }

    @GetMapping("/received")
    @Operation(
            method = "GET",
            summary = "Get all received recommendations")
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RecommendationSwaggerDTO.class))
            })
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = @Content(schema = @Schema(hidden = true)))
    @Override
    public ResponseEntity<Map<Long, ArrayList>> findAllReceivedRecommendations() {
        LOG.debug("RecommendationControllerImpl: Finding all received recommendations");
        User currentUser = authBO.getCurrentUser();
        Map<Long, ArrayList> receivedRecommendations = recommendationBO.getAllReceivedRecommendations(currentUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(receivedRecommendations);
    }
    @PostMapping
    @Operation(method = "POST", summary = "Send a recommendation to a user")
    @ApiResponse(
            responseCode = "201",
            description = "Created",
            content = {@Content(schema = @Schema(hidden = true))})
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = @Content(schema = @Schema(hidden = true)))
    @ApiResponse(
            responseCode = "409",
            description = "Conflict",
            content = {@Content(schema = @Schema(hidden = true))})
    @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = {@Content(schema = @Schema(hidden = true))})
    @SecurityRequirement(name = "Authorization")
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
        LOG.debug("RecommendationControllerImpl: Sending new recommendation");
        recommendationBO.save(recommendation);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @Override
    @Operation(
            method = "DELETE",
            summary = "Delete a recommendation")
    @ApiResponse(
            responseCode = "204",
            description = "No content",
            content = {@Content(schema = @Schema(hidden = true))})
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden",
            content = @Content(schema = @Schema(hidden = true)))
    @ApiResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(schema = @Schema(hidden = true)))
    @SecurityRequirement(name = "Authorization")
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
        LOG.debug("RecommendationControllerImpl: Deleting recomendation with id {}", recommendationId);
        recommendationBO.delete(recommendationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
