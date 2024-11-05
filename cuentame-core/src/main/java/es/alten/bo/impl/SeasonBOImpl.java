package es.alten.bo.impl;

import es.alten.bo.SeasonBO;
import es.alten.dao.SeasonRepository;
import es.alten.domain.QSeason;
import es.alten.domain.Season;
import es.alten.dto.SeasonFilterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SeasonBOImpl
    extends ElvisGenericCRUDServiceImpl<Season, Long, QSeason, SeasonFilterDTO, SeasonRepository>
    implements SeasonBO {

  private static final long serialVersionUID = 7842584807701349758L;
  private static final Logger LOG = LoggerFactory.getLogger(SeasonBOImpl.class);

  public SeasonBOImpl(SeasonRepository repository) {
    super(repository);
  }
}
