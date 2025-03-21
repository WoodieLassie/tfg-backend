package es.judith.bo.impl;

import es.judith.bo.EpisodeBO;
import es.judith.dao.EpisodeRepository;
import es.judith.domain.*;
import es.judith.domain.Character;
import es.judith.dto.EpisodeFilterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

  @Transactional(readOnly = true)
  @Override
  public List<Episode> findAllSortedAndPaged(Long seasonId, String title, Integer episodeNum) {
    LOG.debug("EpisodeBOImpl: findAllSortedAndPaged");
    return repository.findBySeasonIdAndTitleAndEpisodeNum(seasonId, title, episodeNum);
  }

  @Override
  public List<Episode> findAll(Long seasonId) {
    return repository.findAllBySeason(seasonId);
  }

  @Transactional(readOnly = true)
  @Override
  public Episode findOneWithCharacters(Long id) {
    LOG.debug("EpisodeBOImpl: findOne");
    Optional<Episode> episodeOptional = repository.findById(id);
    Episode episode = new Episode();
    if (episodeOptional.isPresent()) {
      episode = episodeOptional.get();
    }
    List<Long> characterIds =
        episode.getCharacters().stream().map(Character::getId).toList();
    List<Character> charactersWithActors = repository.findByIdWithCharacters(characterIds);
    for (Character character : episode.getCharacters()) {
      for (Character characterWithActor : charactersWithActors) {
        if (character.getId().equals(characterWithActor.getId())) {
          character.setActors(characterWithActor.getActors());
        }
      }
    }
    return episode;
  }
}
