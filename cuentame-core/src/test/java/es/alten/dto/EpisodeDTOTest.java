package es.alten.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpisodeDTOTest {
  EpisodeDTO episodeDTO = new EpisodeDTO();

  @BeforeEach
  void setUp() {
    episodeDTO = new EpisodeDTO();
  }

  @Test
  void testEquals() {
    episodeDTO.equals(null);
    Assertions.assertNotEquals(null, episodeDTO);
  }

  @Test
  void testHashCode() {
    EpisodeDTO x = new EpisodeDTO();
    x.setTitle("title");
    EpisodeDTO y = new EpisodeDTO();
    y.setTitle("title");
    Assertions.assertTrue(x.equals(y) && y.equals(x));
    Assertions.assertEquals(x.hashCode(), y.hashCode());
  }
}
