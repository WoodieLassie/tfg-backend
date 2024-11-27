package es.alten.bo.impl;

import es.alten.bo.SeasonBO;
import es.alten.dao.SeasonRepository;
import es.alten.domain.Episode;
import es.alten.domain.QSeason;
import es.alten.domain.Season;
import es.alten.dto.SeasonFilterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

  public List<Season> findAllByCharacters(String name) {
    LOG.debug("SeasonBOImpl: findAllByCharacters");
    List<Season> seasonList = repository.findAll();
    List<Episode> episodeSortedList = repository.findAllByCharacter(name);
    List<Season> newSeasonList = new ArrayList<>();
    for (Season season : seasonList) {
      List<Episode> newEpisodeSortedList = new ArrayList<>();
      for (Episode episode : episodeSortedList) {
        if (episode.getSeason().getId().equals(season.getId())) {
          Episode newEpisode = new Episode();
          newEpisode.setId(episode.getId());
          newEpisode.setEpisodeNum(episode.getEpisodeNum());
          newEpisode.setTitle(episode.getTitle());
          newEpisode.setSummary(episode.getSummary());
          newEpisode.setCharacters(null);
          newEpisodeSortedList.add(newEpisode);
        }
      }
      Season newSeason = new Season();
      newSeason.setId(season.getId());
      newSeason.setSeasonNum(season.getSeasonNum());
      newSeason.setDescription(season.getDescription());
      newSeason.setEpisodes(newEpisodeSortedList);
      newSeasonList.add(newSeason);
    }
    return newSeasonList;
  }

  public Boolean existsBySeasonNum(Integer seasonNum) {
    return repository.existsBySeasonNum(seasonNum);
  }
}
