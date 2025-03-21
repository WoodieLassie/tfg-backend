package es.judith.bo.impl;

import es.judith.dao.SeasonRepository;
import es.judith.domain.Character;
import es.judith.domain.Episode;
import es.judith.domain.Season;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeasonBOImplTest {

  private static final String CHARACTER_NAME_TEST = "Test";

  @InjectMocks SeasonBOImpl seasonBO;
  @Mock SeasonRepository repository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.seasonBO = new SeasonBOImpl(repository);
  }

  @Test
  void testFindAllByCharacters() {
    List<Season> mockSeasons = new ArrayList<>();
    Season mockSeason = new Season();
    List<Episode> mockEpisodes = new ArrayList<>();
    Episode mockEpisode = new Episode();
    List<Character> mockCharacters = new ArrayList<>();
    Character mockCharacter = new Character();
    mockCharacter.setName("Test");
    mockCharacter.setId(1L);
    mockCharacters.add(mockCharacter);
    mockEpisode.setCharacters(mockCharacters);
    mockEpisode.setId(1L);
    mockEpisode.setSeason(mockSeason);
    mockEpisodes.add(mockEpisode);
    mockSeason.setEpisodes(mockEpisodes);
    mockSeason.setId(1L);
    mockSeasons.add(mockSeason);
    when(repository.findAllByCharacter(anyString())).thenReturn(mockEpisodes);
    when(repository.findAll()).thenReturn(mockSeasons);
    List<Season> dbSeasons = seasonBO.findAllByCharacters(CHARACTER_NAME_TEST);

    verify(repository, times(1)).findAllByCharacter(CHARACTER_NAME_TEST);

    Assertions.assertNotNull(dbSeasons);
    Assertions.assertEquals(mockSeasons.size(), dbSeasons.size());
  }
//  @Test
//  void testExistsBySeasonNum() {
//    Season mockSeason = new Season();
//    mockSeason.setSeasonNum(1);
//    when(repository.existsBySeasonNum(mockSeason.getSeasonNum())).thenReturn(true);
//    Boolean dbSeasonExists = seasonBO.existsBySeasonNum(mockSeason.getSeasonNum());
//
//    verify(repository, times(1)).existsBySeasonNum(mockSeason.getSeasonNum());
//
//    Assertions.assertTrue(dbSeasonExists);
//  }
  @Test
  void saveTest() {
    Season mockSeason = new Season();
    mockSeason.setId(1L);
    given(repository.save(mockSeason)).willReturn(mockSeason);
    Season dbSeason = seasonBO.save(mockSeason);

    verify(repository, times(1)).save(mockSeason);

    Assertions.assertNotNull(dbSeason);
    Assertions.assertEquals(mockSeason, dbSeason);
  }
  @Test
  void deleteTest() {
    Season mockSeason = new Season();
    mockSeason.setId(1L);
    willDoNothing().given(repository).deleteById(mockSeason.getId());
    seasonBO.delete(mockSeason.getId());
    verify(repository, times(1)).deleteById(mockSeason.getId());
  }
}
