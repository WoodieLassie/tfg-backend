package es.judith.bo.impl;

import es.judith.bo.ShowBO;
import es.judith.dao.ShowRepository;
import es.judith.domain.Show;
import es.judith.utils.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShowBOImpl
    extends ElvisGenericCRUDServiceImpl<Show, Long, ShowRepository>
    implements ShowBO {

  private final static long serialVersionUID = -3276571133320739328L;
  private static final Logger LOG = LoggerFactory.getLogger(ShowBOImpl.class);

  public ShowBOImpl(ShowRepository repository) {
    super(repository);
  }

  @Override
  public List<Show> findAllByName(String name) {
    LOG.debug("ShowBOImpl: findAllByName");
    return repository.findAllByName(name);
  }

  @Override
  public byte[] findImageById(Long id) {
    LOG.debug("ShowBOImpl: findImageById");
    Optional<Show> show = repository.findById(id);
    return show.map(image -> ImageUtil.decompressImage(image.getImageData())).orElse(null);
  }
}
