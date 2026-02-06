package es.judith.bo.impl;

import es.judith.bo.SeasonBO;
import es.judith.dao.SeasonRepository;
import es.judith.domain.Episode;
import es.judith.domain.Season;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SeasonBOImpl
    extends ElvisGenericCRUDServiceImpl<Season, Long, SeasonRepository>
    implements SeasonBO {

  private static final long serialVersionUID = 7842584807701349758L;
  private static final Logger LOG = LoggerFactory.getLogger(SeasonBOImpl.class);

  public SeasonBOImpl(SeasonRepository repository) {
    super(repository);
  }

  @Override
  public List<Season> findAll(Long showId) {
    return repository.findAll(showId);
  }

  @Transactional(readOnly = true)
  public Boolean existsBySeasonNumAndShowId(Integer seasonNum, Long showId) {
    return repository.existsBySeasonNumAndShowId(seasonNum, showId);
  }
}
