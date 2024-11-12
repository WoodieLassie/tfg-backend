package es.alten.bo.impl;

import es.alten.bo.EpisodeBO;
import es.alten.dao.EpisodeRepository;
import es.alten.domain.*;
import es.alten.domain.Character;
import es.alten.dto.EpisodeFilterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
  public List<Episode> findAllSortedAndPaged(Long seasonId, String title, Integer episodeNum) {
    return repository.findBySeasonIdAndTitleAndEpisodeNum(seasonId, title, episodeNum);
  }

  @Override
  public Episode findOne(Long id) {
    Episode episode = repository.findById(id).get();
    List<Long> characterIds =
        episode.getCharacters().stream().map(Character::getId).collect(Collectors.toList());
    // Recorre las IDs de cada personaje que aparece en un episodio y lo convierte en una lista de
    // Long
    List<Character> charactersWithActors = repository.findByIdWithCharacters(characterIds);
    for (Character character : episode.getCharacters()) {
      for (Character characterWithActor : charactersWithActors) {
        // Revisa si el personaje sin datos de actor es el mismo que el personaje con datos de
        // actor, y si lo es, le inserta los datos de actor. Si no se hace este check, provocará un
        // error de "found shared references in a collecion"
        if (character.getId().equals(characterWithActor.getId())) {
          character.setActors(characterWithActor.getActors());
        }
      }
    }
    return episode;
  }
}
