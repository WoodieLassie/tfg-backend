package es.alten.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SeasonNoEpisodesDTOTest {
  SeasonNoEpisodesDTO seasonNoEpisodesDTO = new SeasonNoEpisodesDTO();

  @BeforeEach
  void setUp() {
    seasonNoEpisodesDTO = new SeasonNoEpisodesDTO();
  }

  @Test
  void testEquals() {
    seasonNoEpisodesDTO.equals(null);
    Assertions.assertNotEquals(null, seasonNoEpisodesDTO);
  }

  @Test
  void testHashCode() {
    SeasonNoEpisodesDTO x = new SeasonNoEpisodesDTO();
    x.setDescription("desc");
    SeasonNoEpisodesDTO y = new SeasonNoEpisodesDTO();
    y.setDescription("desc");
    Assertions.assertTrue(x.equals(y) && y.equals(x));
    Assertions.assertEquals(x.hashCode(), y.hashCode());
  }
}
