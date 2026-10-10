package es.judith.controller.impl;

import es.judith.bo.AuthBO;
import es.judith.bo.CommentBO;
import es.judith.bo.ShowBO;
import es.judith.controller.CommentController;
import es.judith.domain.Comment;
import es.judith.domain.Role;
import es.judith.domain.Show;
import es.judith.domain.user.User;
import es.judith.dto.comment.CommentDTO;
import es.judith.dto.comment.CommentInputDTO;
import es.judith.dto.favourite.FavouriteDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.Serial;
import java.net.URI;
import java.util.List;
import java.util.Objects;

// El endpoint de DELETE debe tener un sistema de seguridad similar al de favourite que no permita a
// un usuario normal eliminar comentarios de otros
@RestController
@RequestMapping("/api/comments")
@Tag(name = "comments")
public class CommentControllerImpl implements CommentController {

  @Serial
  private static final long serialVersionUID = -6875550298927879264L;
  private static final Logger LOG = LoggerFactory.getLogger(CommentControllerImpl.class);
  private final transient CommentBO bo;
  private final transient ShowBO showBO;
  private final transient AuthBO authBO;

  public CommentControllerImpl(CommentBO bo, ShowBO showBO, AuthBO authBO) {
    this.bo = bo;
    this.showBO = showBO;
    this.authBO = authBO;
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
  public ResponseEntity<Page<CommentDTO>> findAll(
          @PathVariable Long showId,
          @Parameter @RequestParam(defaultValue = "0") Integer page,
          @Parameter @RequestParam(defaultValue = "20") Integer size,
          Pageable pageable) {
    LOG.debug("Fetching results with user id {}", showId);
    List<CommentDTO> commentList = bo.findAllByShowIdWithUser(showId);
    int pageEnd = Math.min((page + size), commentList.size());
    Page<CommentDTO> pagedCommentList = new PageImpl<>(commentList.subList(page, pageEnd), pageable, commentList.size());
    return ResponseEntity.ok(pagedCommentList);
  }

  @Override
  @Operation(method = "POST", summary = "Post a new comment")
  @ApiResponse(
          responseCode = "201",
          description = "Created",
          content = {@Content(schema = @Schema(implementation = Comment.class))})
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
  @PostMapping
  public ResponseEntity<Comment> add(@RequestBody CommentInputDTO commentDTO) {
    if (!commentDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    if (bo.checkForIllegalStrings(commentDTO.getText(), 255)) {
      throw new BadInputException("Comment content cannot be greater than 255 characters");
    }
    Show show = showBO.findOne(commentDTO.getShowId());
    if (show == null) {
      throw new NotExistingIdException(
          "Show with id " + commentDTO.getShowId() + " does not exist");
    }
    User user = authBO.getCurrentUser();
    Comment comment = commentDTO.obtainDomainObject();
    comment.setShow(show);
    comment.setUser(user);
    LOG.debug("CommentControllerImpl: Saving data");
    bo.save(comment);
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(comment.getId())
            .toUri();
    return ResponseEntity.status(HttpStatus.CREATED).location(location).body(comment);
  }

  @Override
  @Operation(
          method = "DELETE",
          summary = "Delete a comment")
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
  @DeleteMapping("/{id}")
  public ResponseEntity<Comment> delete(@PathVariable Long id) {
    if (!bo.exists(id)) {
      throw new NotFoundException("Comment with id " + id + " does not exist");
    }
    if (!Objects.equals(bo.findOne(id).getUser().getId(), authBO.getCurrentUser().getId())
        && authBO.getCurrentUser().getRole() != Role.ADMIN) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
    LOG.debug("CommentControllerImpl: Deleting data with id {}", id);
    bo.delete(id);
    return ResponseEntity.noContent().build();
  }
}
