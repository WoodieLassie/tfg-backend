package es.alten.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class EpisodeFilDTOTest {
    EpisodeFilterDTO episodeFilterDTO;

    @BeforeEach
    void setUp() {
        episodeFilterDTO = new EpisodeFilterDTO();
    }
    @Test
    void testObtainFilterSpecification() {
        SeasonNoEpisodesDTO season = new SeasonNoEpisodesDTO();
        episodeFilterDTO.obtainFilterSpecification();
        episodeFilterDTO.setEpisodeNum(1);
        episodeFilterDTO.setSeason(season);
        episodeFilterDTO.setCharacters(new ArrayList<>());
        episodeFilterDTO.setSummary("sum");
        episodeFilterDTO.setTitle("title");
        Assertions.assertEquals("sum", episodeFilterDTO.getSummary());
        Assertions.assertEquals("title", episodeFilterDTO.getTitle());
        Assertions.assertEquals(List.of(), episodeFilterDTO.getCharacters());
        Assertions.assertEquals(season, episodeFilterDTO.getSeason());
    }
}
