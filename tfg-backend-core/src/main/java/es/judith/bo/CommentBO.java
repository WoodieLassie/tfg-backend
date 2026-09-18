package es.judith.bo;

import es.judith.domain.Comment;
import es.judith.dto.CommentDTO;

import java.util.List;

public interface CommentBO extends GenericBO<Comment, Long> {
    List<CommentDTO> findAllByShowIdWithUser(Long showId);
}
