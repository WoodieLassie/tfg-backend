package es.alten.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpisodeNoSeasonDTOTest {
  EpisodeNoSeasonDTO episodeNoSeasonDTO = new EpisodeNoSeasonDTO();

  @BeforeEach
  void setUp() {
    episodeNoSeasonDTO = new EpisodeNoSeasonDTO();
  }

  @Test
  void testEquals() {
    episodeNoSeasonDTO.equals(null);
    Assertions.assertNotEquals(null, episodeNoSeasonDTO);
  }

  @Test
  void testHashCode() {
    EpisodeNoSeasonDTO x = new EpisodeNoSeasonDTO();
    x.setTitle("title");
    EpisodeNoSeasonDTO y = new EpisodeNoSeasonDTO();
    y.setTitle("title");
    Assertions.assertTrue(x.equals(y) && y.equals(x));
    Assertions.assertEquals(x.hashCode(), y.hashCode());
  }
}
