package es.alten.bo.impl;

import es.alten.bo.EpisodeBO;
import es.alten.bo.SeasonBO;
import es.alten.dao.EpisodeRepository;
import es.alten.domain.Episode;
import es.alten.domain.QEpisode;
import es.alten.domain.Season;
import es.alten.dto.EpisodeFilterDTO;
import es.alten.exceptions.NotExistingIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class EpisodeBOImpl
    extends ElvisGenericCRUDServiceImpl<
        Episode, Long, QEpisode, EpisodeFilterDTO, EpisodeRepository>
    implements EpisodeBO {

  private static final long serialVersionUID = -4565303369232666607L;
  private static final Logger LOG = LoggerFactory.getLogger(EpisodeBOImpl.class);

  public EpisodeBOImpl(EpisodeRepository repository, SeasonBO seasonBO) {
    super(repository);
  }
}
