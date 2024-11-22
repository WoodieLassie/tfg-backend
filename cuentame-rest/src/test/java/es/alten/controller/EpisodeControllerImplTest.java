package es.alten.controller;

import es.alten.domain.Actor;
import es.alten.domain.Character;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.alten.bo.CharacterBO;
import es.alten.bo.EpisodeBO;
import es.alten.bo.SeasonBO;
import es.alten.controller.impl.EpisodeControllerImpl;
import es.alten.domain.Episode;
import es.alten.domain.Season;
import es.alten.dto.EpisodeInputDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ComponentScan(basePackages = "es.alten.cuentame.*")
@Import(EpisodeControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = EpisodeControllerImpl.class)
@WebMvcTest(EpisodeControllerImpl.class)
class EpisodeControllerImplTest {
  @Autowired private MockMvc mockMvc;
  @MockBean private EpisodeBO episodeBO;
  @MockBean private SeasonBO seasonBO;
  @MockBean private CharacterBO characterBO;
  @Autowired private ObjectMapper objectMapper;

  private EpisodeInputDTO mockEpisode;

  @BeforeEach
  void setUp() {
    mockEpisode = new EpisodeInputDTO();
    mockEpisode.setEpisodeNum(1);
    mockEpisode.setSeasonId(1L);
    mockEpisode.setSummary("summary");
    mockEpisode.setTitle("title");
    mockEpisode.setCharacterIds(new ArrayList<>(List.of(1L)));
  }

  @Test
  void addTest() throws Exception {
    Season mockSeason = new Season();
    mockSeason.setId(1L);
    Character mockCharacter = new Character();
    mockCharacter.setId(1L);
    given(episodeBO.save(any(Episode.class))).willAnswer(invocation -> invocation.getArgument(0));
    when(seasonBO.findOne(mockEpisode.getSeasonId())).thenReturn(mockSeason);
    when(characterBO.findAllById(mockEpisode.getCharacterIds()))
        .thenReturn(new ArrayList<>(List.of(mockCharacter)));
    ResultActions response =
        mockMvc.perform(
            post("/api/episodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockEpisode)));
    response.andDo(print()).andExpect(status().isCreated());
  }

  @Test
  void updateTest() throws Exception {
    Season mockSeason = new Season();
    mockSeason.setId(1L);
    Character mockCharacter = new Character();
    mockCharacter.setId(1L);
    Episode mockDbEpisode = mockEpisode.obtainDomainObject();
    mockDbEpisode.setId(1L);
    mockDbEpisode.setCharacters(new ArrayList<>(List.of(mockCharacter)));
    mockDbEpisode.setSeason(mockSeason);
    EpisodeInputDTO updatedEpisode = new EpisodeInputDTO();
    updatedEpisode.setEpisodeNum(1);
    updatedEpisode.setSeasonId(1L);
    updatedEpisode.setSummary("summary2");
    updatedEpisode.setTitle("title2");
    updatedEpisode.setCharacterIds(new ArrayList<>(List.of(1L)));
    given(episodeBO.findOne(mockDbEpisode.getId())).willReturn(mockDbEpisode);
    given(episodeBO.save(any(Episode.class))).willAnswer(invocation -> invocation.getArgument(0));
    when(seasonBO.findOne(updatedEpisode.getSeasonId())).thenReturn(mockSeason);
    when(characterBO.findAllById(updatedEpisode.getCharacterIds()))
        .thenReturn(new ArrayList<>(List.of(mockCharacter)));
    ResultActions response =
        mockMvc.perform(
            patch("/api/episodes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockEpisode)));
    response.andDo(print()).andExpect(status().isNoContent());
  }
  @Test
  void deleteTest() throws Exception {
    Episode mockDbEpisode = mockEpisode.obtainDomainObject();
    mockDbEpisode.setId(1L);
    willDoNothing().given(episodeBO).delete(mockDbEpisode.getId());
    ResultActions response = mockMvc.perform(delete("/api/episodes/{id}", mockDbEpisode.getId()));
    response.andExpect(status().isNoContent()).andDo(print());
  }
}
