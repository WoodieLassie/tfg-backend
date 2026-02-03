package es.judith.controller.impl;

import es.judith.bo.CommentBO;
import es.judith.bo.ShowBO;
import es.judith.bo.UserBO;
import es.judith.controller.CommentController;
import es.judith.domain.Comment;
import es.judith.domain.Role;
import es.judith.domain.Show;
import es.judith.domain.User;
import es.judith.dto.CommentDTO;
import es.judith.dto.CommentInputDTO;
import es.judith.dto.UserDTO;
import es.judith.exceptions.BadInputException;
import es.judith.exceptions.NotExistingIdException;
import es.judith.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

// El endpoint de DELETE debe tener un sistema de seguridad similar al de favourite que no permita a
// un usuario normal eliminar comentarios de otros
@RestController
@RequestMapping("/api/comments")
@Tag(name = "comments")
public class CommentControllerImpl extends GenericControllerImpl implements CommentController {

  private static final Logger LOG = LoggerFactory.getLogger(CommentControllerImpl.class);
  private final CommentBO bo;
  private final ShowBO showBO;

  public CommentControllerImpl(UserBO userBO, CommentBO bo, ShowBO showBO) {
    super(userBO);
    this.bo = bo;
    this.showBO = showBO;
  }

  @Override
  @Operation(method = "GET", summary = "Get all comments")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = CommentDTO.class)))
      })
  @GetMapping("/{showId}")
  public ResponseEntity<List<CommentDTO>> findAll(@PathVariable Long showId) {
    LOG.debug("Fetching results with user id {}", showId);
    List<CommentDTO> commentList = bo.findAllByShowIdWithUser(showId);
    return ResponseEntity.ok(commentList);
  }

  @Override
  @PostMapping
  public ResponseEntity<Comment> add(@RequestBody CommentInputDTO commentDTO) {
    if (!commentDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    Show show = showBO.findOne(commentDTO.getShowId());
    if (show == null) {
      throw new NotExistingIdException(
          "Show with id " + commentDTO.getShowId() + " does not exist");
    }
    User user = this.getCurrentUser();
    Comment comment = commentDTO.obtainDomainObject();
    comment.setShow(show);
    comment.setUser(user);
    LOG.debug("CommentControllerImpl: Saving data");
    bo.save(comment);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Comment> delete(@PathVariable Long id) {
    if (!bo.exists(id)) {
      throw new NotFoundException("Comment with id " + id + " does not exist");
    }
    if (!Objects.equals(bo.findOne(id).getUser().getId(), this.getCurrentUser().getId())
        && this.getCurrentUser().getRole() != Role.ADMIN) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
    LOG.debug("CommentControllerImpl: Deleting data with id {}", id);
    bo.delete(id);
    return ResponseEntity.noContent().build();
  }
}
