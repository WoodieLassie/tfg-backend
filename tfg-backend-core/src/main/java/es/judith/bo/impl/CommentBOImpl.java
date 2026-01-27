package es.judith.bo.impl;

import es.judith.bo.CommentBO;
import es.judith.dao.CommentRepository;
import es.judith.domain.Comment;
import es.judith.dto.CommentDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CommentBOImpl
    extends ElvisGenericCRUDServiceImpl<
        Comment, Long, CommentRepository>
    implements CommentBO {

  public CommentBOImpl(CommentRepository repository) {
    super(repository);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CommentDTO> findAllByShowIdWithUser(Long showId) {
    List<Object[]> commentList = repository.findAll(showId);
    List<CommentDTO> convertedCommentList = new ArrayList<>();
    for (Object[] comment : commentList) {
      CommentDTO convertedComment = new CommentDTO();
      convertedComment.setId((Long) comment[0]);
      convertedComment.setText((String) comment[1]);
      convertedComment.setEmail((String) comment[8]);
      convertedCommentList.add(convertedComment);
    }
    return convertedCommentList;
  }
}
