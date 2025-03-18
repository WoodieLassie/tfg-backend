package es.judith.bo.impl;

import es.judith.bo.ShowBO;
import es.judith.dao.ShowRepository;
import es.judith.domain.QShow;
import es.judith.domain.Show;
import es.judith.dto.ShowFilterDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShowBOImpl
    extends ElvisGenericCRUDServiceImpl<Show, Long, QShow, ShowFilterDTO, ShowRepository>
    implements ShowBO {

  public ShowBOImpl(ShowRepository repository) {
    super(repository);
  }

  @Override
  public List<Show> findAllByName(String name) {
    return repository.findAllByName(name);
  }
}
