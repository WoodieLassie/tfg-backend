package es.alten.bo.impl;

import es.alten.dao.SeasonRepository;
import es.alten.domain.Character;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SeasonBOImplTest {

    private static final String CHARACTER_NAME_TEST = "Test";

    @InjectMocks
    SeasonBOImpl seasonBO;
    @Mock
    SeasonRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        this.seasonBO = new SeasonBOImpl(repository);
    }

    @Test
    void testFindAllByCharacters() {
        List<Season> mock_seasons = new ArrayList<>();
        Season mock_season = new Season();
        List<Episode> mock_episodes = new ArrayList<>();
        Episode mock_episode = new Episode();
        List<Character> mock_characters = new ArrayList<>();
        Character mock_character = new Character();
        mock_character.setName("Test");
        mock_character.setId(1L);
        mock_characters.add(mock_character);
        mock_episode.setCharacters(mock_characters);
        mock_episode.setId(1L);
        mock_episode.setSeason(mock_season);
        mock_episodes.add(mock_episode);
        mock_season.setEpisodes(mock_episodes);
        mock_season.setId(1L);
        mock_seasons.add(mock_season);
        when(repository.findAllByCharacter(anyString())).thenReturn(mock_episodes);
        when(repository.findAll()).thenReturn(mock_seasons);
        List<Season> dbSeasons = seasonBO.findAllByCharacters(CHARACTER_NAME_TEST);

        verify(repository, times(1)).findAllByCharacter(CHARACTER_NAME_TEST);

        Assertions.assertNotNull(dbSeasons);
        Assertions.assertEquals(dbSeasons.size(), 1);
    }
}
