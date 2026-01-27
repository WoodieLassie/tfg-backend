package es.judith.bo.impl;

import es.judith.bo.FavouriteBO;
import es.judith.dao.FavouriteRepository;
import es.judith.domain.Favourite;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FavouriteBOImpl
    extends ElvisGenericCRUDServiceImpl<
        Favourite, Long,FavouriteRepository>
    implements FavouriteBO {

  public FavouriteBOImpl(FavouriteRepository repository) {
    super(repository);
  }

  @Transactional(readOnly = true)
  @Override
  public List<Favourite> findAllByUser(Long userId) {
    return repository.findAllByUser(userId);
  }
}
