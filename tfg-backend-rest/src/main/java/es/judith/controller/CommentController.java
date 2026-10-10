package es.judith.controller;

import es.judith.domain.Comment;
import es.judith.dto.comment.CommentDTO;
import es.judith.dto.comment.CommentInputDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.List;

public interface CommentController extends Serializable {
    ResponseEntity<Page<CommentDTO>> findAll(Long showId, Integer page, Integer size, Pageable pageable);
    ResponseEntity<Comment> add(CommentInputDTO commentDTO);
    ResponseEntity<Comment> delete(Long id);
}
