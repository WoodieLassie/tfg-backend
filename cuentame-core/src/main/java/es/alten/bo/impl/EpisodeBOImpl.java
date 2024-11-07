package es.alten.bo.impl;

import es.alten.bo.EpisodeBO;
import es.alten.dao.EpisodeRepository;
import es.alten.domain.*;
import es.alten.domain.Character;
import es.alten.dto.EpisodeFilterDTO;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EpisodeBOImpl
    extends ElvisGenericCRUDServiceImpl<
        Episode, Long, QEpisode, EpisodeFilterDTO, EpisodeRepository>
    implements EpisodeBO {

  private static final long serialVersionUID = -4565303369232666607L;
  private static final Logger LOG = LoggerFactory.getLogger(EpisodeBOImpl.class);

  public EpisodeBOImpl(EpisodeRepository repository) {
    super(repository);
  }
  @Override
  public List<Episode> findAllSortedAndPaged(
      Long seasonId, String title, Integer episodeNum) {
    List<Episode> episodes = repository.findBySeasonIdAndTitleAndEpisodeNum(seasonId, title, episodeNum);
    return episodes;
  }
}
