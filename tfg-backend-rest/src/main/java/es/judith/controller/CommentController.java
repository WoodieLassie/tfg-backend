package es.judith.controller;

import es.judith.domain.Comment;
import es.judith.dto.CommentDTO;
import es.judith.dto.CommentInputDTO;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.List;

public interface CommentController extends Serializable {
    ResponseEntity<List<CommentDTO>> findAll(Long showId);
    ResponseEntity<Comment> add(CommentInputDTO commentDTO);
    ResponseEntity<Comment> delete(Long id);
}
