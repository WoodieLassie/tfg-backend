package es.alten.bo.impl;

import es.alten.domain.Character;
import es.alten.dao.EpisodeRepository;
import es.alten.domain.Episode;
import es.alten.domain.Season;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EpisodeBOImplTest {
  @InjectMocks EpisodeBOImpl episodeBO;
  @Mock EpisodeRepository repository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.episodeBO = new EpisodeBOImpl(repository);
  }

  @Test
  void testFindOneWithCharacters() {
    Episode mockEpisode = new Episode();
    mockEpisode.setId(1L);
    Character mockCharacter = new Character();
    mockCharacter.setId(1L);
    List<Character> mockCharacters = new ArrayList<>();
    mockCharacters.add(mockCharacter);
    mockEpisode.setCharacters(mockCharacters);
    when(repository.findById(mockEpisode.getId())).thenReturn(Optional.of(mockEpisode));
    Episode dbEpisode = episodeBO.findOneWithCharacters(mockEpisode.getId());

    verify(repository, times(1)).findById(mockEpisode.getId());
    verify(repository, times(1))
        .findByIdWithCharacters(
            mockEpisode.getCharacters().stream().map(Character::getId).toList());

    Assertions.assertNotNull(dbEpisode);
    Assertions.assertEquals(mockEpisode.getCharacters(), dbEpisode.getCharacters());
  }

  @Test
  void testFindAllSortedAndPaged() {
    List<Episode> mockEpisodes = new ArrayList<>();
    Season mockSeason = new Season();
    mockSeason.setId(1L);
    Episode mockEpisode = new Episode();
    mockEpisode.setEpisodeNum(1);
    mockEpisode.setTitle("title");
    mockEpisode.setSeason(mockSeason);
    mockEpisodes.add(mockEpisode);

    when(repository.findBySeasonIdAndTitleAndEpisodeNum(
            mockEpisode.getSeason().getId(), mockEpisode.getTitle(), mockEpisode.getEpisodeNum()))
        .thenReturn(mockEpisodes);
    List<Episode> dbEpisodes =
        episodeBO.findAllSortedAndPaged(
            mockEpisode.getSeason().getId(), mockEpisode.getTitle(), mockEpisode.getEpisodeNum());

    verify(repository, times(1))
        .findBySeasonIdAndTitleAndEpisodeNum(
            mockEpisode.getSeason().getId(), mockEpisode.getTitle(), mockEpisode.getEpisodeNum());

    Assertions.assertNotNull(dbEpisodes);
    Assertions.assertEquals(mockEpisodes, dbEpisodes);
  }
  @Test
  void saveTest() {
    Episode mockEpisode = new Episode();
    mockEpisode.setId(1L);
    given(repository.save(mockEpisode)).willReturn(mockEpisode);
    Episode dbEpisode = episodeBO.save(mockEpisode);

    verify(repository, times(1)).save(mockEpisode);

    Assertions.assertNotNull(dbEpisode);
    Assertions.assertEquals(mockEpisode, dbEpisode);
  }
  @Test
  void deleteTest() {
    Episode mockEpisode = new Episode();
    mockEpisode.setId(1L);
    willDoNothing().given(repository).deleteById(mockEpisode.getId());
    episodeBO.delete(mockEpisode.getId());
    verify(repository, times(1)).deleteById(mockEpisode.getId());
  }
}
