package es.judith.bo;

import es.judith.domain.Comment;
import es.judith.domain.QComment;
import es.judith.dto.CommentDTO;
import es.judith.dto.CommentFilterDTO;

import java.util.List;

public interface CommentBO extends GenericCRUDService<Comment, Long, QComment, CommentFilterDTO> {
    List<CommentDTO> findAllByShowIdWithUser(Long showId);
}
