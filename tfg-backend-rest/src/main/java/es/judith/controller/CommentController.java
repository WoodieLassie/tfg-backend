package es.judith.controller;

import es.judith.domain.Comment;
import es.judith.dto.CommentDTO;
import es.judith.dto.CommentInputDTO;
import es.judith.rest.BaseController;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentController extends BaseController {
    ResponseEntity<List<CommentDTO>> findAll(Long showId);
    ResponseEntity<Comment> add(CommentInputDTO commentDTO);
    ResponseEntity<Comment> delete(Long id);
}
