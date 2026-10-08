package es.judith.bo.impl;

import es.judith.bo.FavouriteBO;
import es.judith.dao.FavouriteRepository;
import es.judith.domain.Favourite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serial;
import java.util.List;

@Service
@Transactional
public class FavouriteBOImpl extends GenericBOImpl<Favourite, Long,FavouriteRepository> implements FavouriteBO {

  @Serial
  private static final long serialVersionUID = 1937349908693174450L;
  private static final Logger LOG = LoggerFactory.getLogger(FavouriteBOImpl.class);

  public FavouriteBOImpl(FavouriteRepository repository) {
    super(repository);
  }

  @Transactional(readOnly = true)
  @Override
  public List<Favourite> findAllByUser(Long userId) {
    LOG.debug("FavouriteBOImpl: findAllByUser");
    return repository.findAllByUser(userId);
  }
}
