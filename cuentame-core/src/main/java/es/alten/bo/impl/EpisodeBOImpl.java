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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
  public List<Episode> findAll() {
    List<Episode> episodes = repository.findAll();
    List<Character> characters = repository.findAllWithCharacters();
    for (Episode episode : episodes) {
      for (Character character : characters) {
        character.getActors();
      }
    }
    return episodes;
  }

  @Override
  public List<Episode> findAllSortedAndPaged(Long seasonId, String title, Integer episodeNum) {
    List<Episode> episodes =
        repository.findBySeasonIdAndTitleAndEpisodeNum(seasonId, title, episodeNum);
    // Recorre las IDs de cada personaje que aparece en un episodio y lo convierte en una lista de
    // Long
    List<Long> characterIds =
        episodes.stream()
            .flatMap(e -> e.getCharacters().stream())
            .map(Character::getId)
            .collect(Collectors.toList());
    List<Character> charactersWithActors = repository.findByIdWithCharacters(characterIds);
    for (Episode episode : episodes)
      for (Character character : episode.getCharacters()) {
        for (Character characterWithActor : charactersWithActors) {
          // Revisa si el personaje sin datos de actor es el mismo que el personaje con datos de
          // actor, y si lo es, le inserta los datos de actor
          if (character.getId().equals(characterWithActor.getId())) {
            character.setActors(characterWithActor.getActors());
          }
        }
      }
    return episodes;
  }

  @Override
  public Episode findOne(Long id) {
    Episode episode = repository.findById(id).get();
    List<Long> characterIds =
        episode.getCharacters().stream().map(Character::getId).collect(Collectors.toList());
    List<Character> charactersWithActors = repository.findByIdWithCharacters(characterIds);
    for (Character character : episode.getCharacters()) {
      for (Character characterWithActor : charactersWithActors) {
        character.setActors(characterWithActor.getActors());
      }
    }
    return episode;
  }
}
