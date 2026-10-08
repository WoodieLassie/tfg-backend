package es.judith.bo.impl;

import es.judith.bo.CommentBO;
import es.judith.dao.CommentRepository;
import es.judith.domain.Comment;
import es.judith.dto.comment.CommentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CommentBOImpl extends GenericBOImpl<Comment, Long, CommentRepository> implements CommentBO {

  private static final Logger LOG = LoggerFactory.getLogger(CommentBOImpl.class);
  @Serial
  private static final long serialVersionUID = 418692179283394167L;

  public CommentBOImpl(CommentRepository repository) {
    super(repository);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CommentDTO> findAllByShowIdWithUser(Long showId) {
    LOG.debug("CommentBOImpl: findAllByShowIdWithUser");
    List<Object[]> commentList = repository.findAll(showId);
    List<CommentDTO> convertedCommentList = new ArrayList<>();
    for (Object[] comment : commentList) {
      CommentDTO convertedComment = new CommentDTO();
      convertedComment.setId((Long) comment[0]);
      convertedComment.setText((String) comment[1]);
      convertedComment.setUsername((String) comment[4]);
      convertedCommentList.add(convertedComment);
    }
    return convertedCommentList;
  }
}
