package es.judith.controller.impl;

import es.judith.bo.*;
import es.judith.controller.ReviewController;
import es.judith.domain.Review;
import es.judith.domain.Role;
import es.judith.domain.Show;
import es.judith.domain.user.User;
import es.judith.dto.review.ReviewDTO;
import es.judith.dto.review.ReviewInputDTO;
import es.judith.dto.swagger.FriendSwaggerDTO;
import es.judith.exceptions.BadInputException;
import es.judith.exceptions.NotExistingIdException;
import es.judith.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.Serial;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "reviews")
public class ReviewControllerImpl implements ReviewController {

  @Serial
  private static final long serialVersionUID = 8386981531402903923L;
  private static final Logger LOG = LoggerFactory.getLogger(ReviewControllerImpl.class);
  private final transient ReviewBO bo;
  private final transient ShowBO showBO;
  private final transient AuthBO authBO;
  private final transient FriendBO friendBO;
  private final transient UserBO userBO;
  private final ReviewBO reviewBO;

  public ReviewControllerImpl(ReviewBO bo, ShowBO showBO, AuthBO authBO, FriendBO friendBO, UserBO userBO, ReviewBO reviewBO) {
    this.bo = bo;
    this.showBO = showBO;
    this.authBO = authBO;
    this.friendBO = friendBO;
    this.userBO = userBO;
    this.reviewBO = reviewBO;
  }

  @Override
  @Operation(
          method = "GET",
          summary = "Get average rating for show by show ID",
          parameters = @Parameter(ref = "showId"))
  @ApiResponse(
          responseCode = "200",
          description = "OK",
          content = {
                  @Content(mediaType = "application/json", schema = @Schema(implementation = HashMap.class))
          })
  @ApiResponse(
          responseCode = "404",
          description = "Not found",
          content = @Content(schema = @Schema(hidden = true)))
  @GetMapping("/show/{showId}")
  public ResponseEntity<HashMap<String, Double>> findAverageRatingByShow(@PathVariable Long showId) {
    LOG.debug("ReviewControllerImpl: Fetching average rating of show id {}", showId);
    Double totalReviewScore = reviewBO.calculateAverageRating(showId);
    HashMap<String, Double> response = new HashMap<>();
    response.put("averageRating", totalReviewScore);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Override
  @Operation(
          method = "GET",
          summary = "Get all reviews from user by user ID",
          parameters = @Parameter(ref = "userId"))
  @ApiResponse(
          responseCode = "200",
          description = "OK",
          content = {
                  @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReviewDTO.class)))
          })
  @ApiResponse(
          responseCode = "403",
          description = "Forbidden",
          content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(
          responseCode = "404",
          description = "Not found",
          content = @Content(schema = @Schema(hidden = true)))
  @SecurityRequirement(name = "Authorization")
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<ReviewDTO>> findAllByUserId(@PathVariable Long userId) {
    User currentUser = authBO.getCurrentUser();
    if (userBO.findOne(userId) == null) {
      throw new NotExistingIdException(
              "User with id " + userId + " does not exist"
      );
    }
    if (!friendBO.checkIfFriend(currentUser.getId(), userId) && !Objects.equals(currentUser.getId(), userId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
    LOG.debug("ReviewControllerImpl: Finding all reviews by user id {}", userId);
    List<Review> userReviews = reviewBO.findAllByUserId(userId);
    List<ReviewDTO> convertedUserReviews = new ArrayList<>();
    for (Review userReview : userReviews) {
      convertedUserReviews.add(bo.convertToDTO(userReview));
    }
    return ResponseEntity.status(HttpStatus.OK).body(convertedUserReviews);
  }

  @Override
  @Operation(method = "POST", summary = "Save a new review")
  @ApiResponse(
          responseCode = "201",
          description = "Created",
          content = {@Content(schema = @Schema(implementation = Review.class))})
  @ApiResponse(
          responseCode = "403",
          description = "Forbidden",
          content = @Content(schema = @Schema(hidden = true)))
  @SecurityRequirement(name = "Authorization")
  @PostMapping
  public ResponseEntity<Review> add(@RequestBody ReviewInputDTO reviewDTO) {
    if (!reviewDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    if (reviewDTO.getRating() < 1 || reviewDTO.getRating() > 5) {
      throw new BadInputException("Rating value must not be greater than 5 or less than 1");
    }
    User user = authBO.getCurrentUser();
    Review existingUserReviewInShow =
        bo.checkIfUserReviewInShow(reviewDTO.getShowId(), user.getId());
    Review newReviewInfo = reviewDTO.obtainDomainObject();
    Show show = showBO.findOne(reviewDTO.getShowId());
    if (show == null) {
      throw new NotFoundException("Show with id " + reviewDTO.getShowId() +  "does not exist");
    }
    newReviewInfo.setShow(show);
    newReviewInfo.setUser(user);
    if (existingUserReviewInShow != null) {
      newReviewInfo.setId(existingUserReviewInShow.getId());
      URI location = ServletUriComponentsBuilder
              .fromCurrentRequest()
              .path("/{id}")
              .buildAndExpand(newReviewInfo.getId())
              .toUri();
      bo.save(newReviewInfo);
      return ResponseEntity.status(HttpStatus.CREATED).location(location).body(newReviewInfo);
    }
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newReviewInfo.getId())
            .toUri();
    LOG.debug("ReviewControllerImpl: Saving new review");
    bo.save(newReviewInfo);
    return ResponseEntity.status(HttpStatus.CREATED).location(location).body(newReviewInfo);
  }

  @Override
  @Operation(method = "DELETE", summary = "Delete a review", parameters = @Parameter(ref = "id"))
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
          description = "Not found",
          content = @Content(schema = @Schema(hidden = true)))
  @SecurityRequirement(name = "Authorization")
  @DeleteMapping("/{id}")
  public ResponseEntity<Review> delete(@PathVariable Long id) {
    if (!bo.exists(id)) {
      throw new NotFoundException("Review with id " + id + " does not exist");
    }
    User currentUser = authBO.getCurrentUser();
    if (!Objects.equals(bo.findOne(id).getUser().getId(), currentUser.getId())
        && currentUser.getRole() != Role.ADMIN) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
    LOG.debug("ReviewControllerImpl: Deleting data with id {}", id);
    bo.delete(id);
    return ResponseEntity.noContent().build();
  }
}
