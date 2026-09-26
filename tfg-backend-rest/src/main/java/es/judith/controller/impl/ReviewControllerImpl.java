package es.judith.controller.impl;

import es.judith.bo.*;
import es.judith.controller.ReviewController;
import es.judith.domain.Review;
import es.judith.domain.Role;
import es.judith.domain.Show;
import es.judith.domain.User;
import es.judith.dto.ReviewInputDTO;
import es.judith.exceptions.BadInputException;
import es.judith.exceptions.NotExistingIdException;
import es.judith.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "reviews")
public class ReviewControllerImpl implements ReviewController {
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
  @GetMapping("/show/{showId}")
  public ResponseEntity<HashMap<String, Double>> findAllByShowId(@PathVariable Long showId) {
    LOG.debug("ReviewControllerImpl: Fetching all results");
    List<Review> reviewList = bo.findAllByShowId(showId);
    Double totalReviewScore = 0.0;
    for (Review review : reviewList) {
      totalReviewScore += review.getRating();
    }
    totalReviewScore = totalReviewScore / reviewList.size();
    String totalReviewScoreTruncated = totalReviewScore.toString().substring(0,3);
    totalReviewScore = Double.valueOf(totalReviewScoreTruncated);
    HashMap<String, Double> response = new HashMap<>();
    response.put("averageRating", totalReviewScore);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Review>> findAllByUserId(@PathVariable Long userId) {
    User currentUser = authBO.getCurrentUser();
    if (userBO.findOne(userId) == null) {
      throw new NotExistingIdException(
              "User with id " + userId + " does not exist"
      );
    }
    if (!friendBO.checkIfFriend(currentUser.getId(), userId) && !Objects.equals(currentUser.getId(), userId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
    List<Review> userReviews = reviewBO.findAllByUserId(userId);
    return ResponseEntity.status(HttpStatus.OK).body(userReviews);
  }

  @Override
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
      bo.save(newReviewInfo);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
    bo.save(newReviewInfo);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @Override
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
