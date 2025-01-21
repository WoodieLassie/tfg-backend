package es.judith.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpisodeSwaggerDTOTest {
  EpisodeSwaggerDTO episodeSwaggerDTO = new EpisodeSwaggerDTO();

  @BeforeEach
  void setUp() {
    episodeSwaggerDTO = new EpisodeSwaggerDTO();
  }

  @Test
  void testEquals() {
    episodeSwaggerDTO.equals(null);
    Assertions.assertNotEquals(null, episodeSwaggerDTO);
  }

  @Test
  void testHashCode() {
    EpisodeSwaggerDTO x = new EpisodeSwaggerDTO();
    x.setTitle("title");
    EpisodeSwaggerDTO y = new EpisodeSwaggerDTO();
    y.setTitle("title");
    Assertions.assertTrue(x.equals(y) && y.equals(x));
    Assertions.assertEquals(x.hashCode(), y.hashCode());
  }
}
