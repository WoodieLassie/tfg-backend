package es.alten.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class EpisodeInputDTOTest {
  EpisodeInputDTO episodeInputDTO = new EpisodeInputDTO();

  @BeforeEach
  void setUp() {
    episodeInputDTO = new EpisodeInputDTO();
  }

  @Test
  void testEquals() {
    episodeInputDTO.equals(null);
    Assertions.assertNotEquals(null, episodeInputDTO);
  }

  @Test
  void testHashCode() {
    EpisodeInputDTO x = new EpisodeInputDTO();
    x.setTitle("title");
    EpisodeInputDTO y = new EpisodeInputDTO();
    y.setTitle("title");
    Assertions.assertTrue(x.equals(y) && y.equals(x));
    Assertions.assertEquals(x.hashCode(), y.hashCode());
  }

  @Test
  void testAllFieldsArePresent() {
    EpisodeInputDTO x = new EpisodeInputDTO();
    EpisodeInputDTO y = new EpisodeInputDTO();
    y.setTitle("title");
    y.setId(1L);
    y.setEpisodeNum(1);
    y.setSummary("summary");
    y.setSeasonId(1L);
    y.setCharacterIds(new ArrayList<>());
    y.getCharacterIds().add(1L);
    Assertions.assertFalse(x.allFieldsArePresent());
    Assertions.assertTrue(y.allFieldsArePresent());
  }
}
