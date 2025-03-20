package es.judith.bo.impl;

import es.judith.bo.CommentBO;
import es.judith.dao.CommentRepository;
import es.judith.domain.Comment;
import es.judith.domain.QComment;
import es.judith.domain.Show;
import es.judith.dto.CommentDTO;
import es.judith.dto.CommentFilterDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class CommentBOImpl
    extends ElvisGenericCRUDServiceImpl<
        Comment, Long, QComment, CommentFilterDTO, CommentRepository>
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
