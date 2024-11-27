package es.alten.controller;

import es.alten.domain.Character;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.alten.bo.CharacterBO;
import es.alten.bo.EpisodeBO;
import es.alten.bo.SeasonBO;
import es.alten.controller.impl.EpisodeControllerImpl;
import es.alten.domain.Episode;
import es.alten.domain.Season;
import es.alten.dto.CharacterDTO;
import es.alten.dto.EpisodeDTO;
import es.alten.dto.EpisodeInputDTO;
import es.alten.dto.SeasonNoEpisodesDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

  private EpisodeInputDTO mockInputEpisode;
  private CharacterDTO mockCharacterDTO;
  private SeasonNoEpisodesDTO mockSeasonDTO;
  private EpisodeDTO mockEpisode;

  @BeforeEach
  void setUp() {
    mockInputEpisode = new EpisodeInputDTO();
    mockCharacterDTO = new CharacterDTO();
    mockSeasonDTO = new SeasonNoEpisodesDTO();
    mockEpisode = new EpisodeDTO();
    mockInputEpisode.setEpisodeNum(1);
    mockInputEpisode.setSeasonId(1L);
    mockInputEpisode.setSummary("summary");
    mockInputEpisode.setTitle("title");
    mockInputEpisode.setCharacterIds(new ArrayList<>(List.of(1L)));
    mockCharacterDTO.setName("name");
    mockCharacterDTO.setNationality("nationality");
    mockCharacterDTO.setGender("gender");
    mockCharacterDTO.setDescription("desc");
    mockCharacterDTO.setAge(23);
    mockCharacterDTO.setId(1L);
    mockSeasonDTO.setId(1L);
    mockSeasonDTO.setId(1L);
    mockSeasonDTO.setSeasonNum(1);
    mockSeasonDTO.setDescription("desc");
    mockEpisode.setId(1L);
    mockEpisode.setTitle("title");
    mockEpisode.setSummary("summary");
    mockEpisode.setCharacters(new ArrayList<>(List.of(mockCharacterDTO)));
    mockEpisode.setSeason(mockSeasonDTO);
    mockEpisode.setEpisodeNum(1);
  }

  @Test
  void findAllTest() throws Exception {
    List<Episode> mockEpisodeList = new ArrayList<>(List.of(mockEpisode.obtainDomainObject()));
    List<EpisodeDTO> mockEpisodeDTOList = new ArrayList<>(List.of(mockEpisode));
    given(episodeBO.findAll()).willReturn(mockEpisodeList);
    when(characterBO.findAllById(new ArrayList<>(List.of(mockCharacterDTO.getId()))))
        .thenReturn(new ArrayList<>(List.of(mockCharacterDTO.obtainDomainObject())));
    when(seasonBO.findOne(mockEpisode.getSeason().getId()))
        .thenReturn(mockSeasonDTO.obtainDomainObject());
    ResultActions response = mockMvc.perform(get("/api/episodes"));
    response
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().json(objectMapper.writeValueAsString(mockEpisodeDTOList)));
  }

  @Test
  void findByIdTest() throws Exception {
    Episode mockEpisodeEntity = mockEpisode.obtainDomainObject();
    given(episodeBO.findOne(mockEpisode.getId())).willReturn(mockEpisodeEntity);
    given(episodeBO.findOneWithCharacters(mockEpisode.getId())).willReturn(mockEpisodeEntity);
    when(characterBO.findAllById(new ArrayList<>(List.of(mockCharacterDTO.getId()))))
        .thenReturn(new ArrayList<>(List.of(mockCharacterDTO.obtainDomainObject())));
    when(seasonBO.findOne(mockEpisode.getSeason().getId()))
        .thenReturn(mockSeasonDTO.obtainDomainObject());
    ResultActions response = mockMvc.perform(get("/api/episodes/{id}", mockEpisode.getId()));
    response
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().json(objectMapper.writeValueAsString(mockEpisode)));
  }

  @Test
  void findAllSortedAndPagedTest() throws Exception {
    List<Episode> mockEpisodeList = new ArrayList<>(List.of(mockEpisode.obtainDomainObject()));
    PageRequest pagedRequest = PageRequest.of(0, 5);
    List<EpisodeDTO> mockEpisodeDTOList = new ArrayList<>(List.of(mockEpisode));
    Page<EpisodeDTO> mockEpisodeListPaged =
        new PageImpl<>(mockEpisodeDTOList, pagedRequest, mockEpisodeDTOList.size());
    given(
            episodeBO.findAllSortedAndPaged(
                mockEpisode.getSeason().getId(),
                mockEpisode.getTitle(),
                mockEpisode.getEpisodeNum()))
        .willReturn(mockEpisodeList);
    when(characterBO.findAllById(new ArrayList<>(List.of(mockCharacterDTO.getId()))))
        .thenReturn(new ArrayList<>(List.of(mockCharacterDTO.obtainDomainObject())));
    when(seasonBO.findOne(mockEpisode.getSeason().getId()))
        .thenReturn(mockSeasonDTO.obtainDomainObject());
    ResultActions response =
        mockMvc.perform(
            get("/api/episodes/sorted")
                .param("title", mockEpisode.getTitle())
                .param("episodeNum", String.valueOf(mockEpisode.getEpisodeNum()))
                .param("seasonId", String.valueOf(mockEpisode.getSeason().getId())));
    response
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().json(objectMapper.writeValueAsString(mockEpisodeListPaged)));
  }

  @Test
  void addTest() throws Exception {
    Season mockSeason = new Season();
    mockSeason.setId(1L);
    Character mockCharacter = new Character();
    mockCharacter.setId(1L);
    given(episodeBO.save(any(Episode.class))).willAnswer(invocation -> invocation.getArgument(0));
    when(seasonBO.findOne(mockInputEpisode.getSeasonId())).thenReturn(mockSeason);
    when(characterBO.findAllById(mockInputEpisode.getCharacterIds()))
        .thenReturn(new ArrayList<>(List.of(mockCharacter)));
    ResultActions response =
        mockMvc.perform(
            post("/api/episodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockInputEpisode)));
    response.andDo(print()).andExpect(status().isCreated());
  }

  @Test
  void updateTest() throws Exception {
    Season mockSeason = new Season();
    mockSeason.setId(1L);
    Character mockCharacter = new Character();
    mockCharacter.setId(1L);
    Episode mockDbEpisode = mockInputEpisode.obtainDomainObject();
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
                .content(objectMapper.writeValueAsString(updatedEpisode)));
    response.andDo(print()).andExpect(status().isNoContent());
  }

  @Test
  void deleteTest() throws Exception {
    Episode mockDbEpisode = mockInputEpisode.obtainDomainObject();
    mockDbEpisode.setId(1L);
    given(episodeBO.exists(mockEpisode.getId())).willReturn(true);
    willDoNothing().given(episodeBO).delete(mockDbEpisode.getId());
    ResultActions response = mockMvc.perform(delete("/api/episodes/{id}", mockDbEpisode.getId()));
    response.andExpect(status().isNoContent()).andDo(print());
  }

  @Test
  void findByIdNotFoundTest() throws Exception {
    given(episodeBO.findOne(mockEpisode.getId())).willReturn(null);
    given(episodeBO.findOneWithCharacters(mockEpisode.getId())).willReturn(null);
    when(characterBO.findAllById(new ArrayList<>(List.of(mockCharacterDTO.getId()))))
        .thenReturn(new ArrayList<>(List.of(mockCharacterDTO.obtainDomainObject())));
    when(seasonBO.findOne(mockEpisode.getSeason().getId()))
        .thenReturn(mockSeasonDTO.obtainDomainObject());
    ResultActions response = mockMvc.perform(get("/api/episodes/{id}", mockEpisode.getId()));
    response.andExpect(status().isNotFound()).andDo(print());
  }

  @Test
  void addBadRequestTest() throws Exception {
    Season mockSeason = new Season();
    mockSeason.setId(1L);
    Character mockCharacter = new Character();
    mockCharacter.setId(1L);
    mockInputEpisode = new EpisodeInputDTO();
    given(episodeBO.save(any(Episode.class))).willAnswer(invocation -> invocation.getArgument(0));
    when(seasonBO.findOne(mockInputEpisode.getSeasonId())).thenReturn(mockSeason);
    when(characterBO.findAllById(mockInputEpisode.getCharacterIds()))
        .thenReturn(new ArrayList<>(List.of(mockCharacter)));
    ResultActions response =
        mockMvc.perform(
            post("/api/episodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockInputEpisode)));
    response.andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void addCharactersNotFoundTest() throws Exception {
    Season mockSeason = new Season();
    mockSeason.setId(1L);
    Character mockCharacter = new Character();
    mockCharacter.setId(1L);
    given(episodeBO.save(any(Episode.class))).willAnswer(invocation -> invocation.getArgument(0));
    when(seasonBO.findOne(mockInputEpisode.getSeasonId())).thenReturn(mockSeason);
    when(characterBO.findAllById(mockInputEpisode.getCharacterIds())).thenReturn(new ArrayList<>());
    ResultActions response =
        mockMvc.perform(
            post("/api/episodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockInputEpisode)));
    response.andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void addSeasonNotFoundTest() throws Exception {
    Season mockSeason = new Season();
    mockSeason.setId(1L);
    Character mockCharacter = new Character();
    mockCharacter.setId(1L);
    given(episodeBO.save(any(Episode.class))).willAnswer(invocation -> invocation.getArgument(0));
    when(seasonBO.findOne(mockInputEpisode.getSeasonId())).thenReturn(null);
    when(characterBO.findAllById(mockInputEpisode.getCharacterIds()))
        .thenReturn(new ArrayList<>(List.of(mockCharacter)));
    ResultActions response =
        mockMvc.perform(
            post("/api/episodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockInputEpisode)));
    response.andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void updateBadRequestTest() throws Exception {
    Season mockSeason = new Season();
    mockSeason.setId(1L);
    Character mockCharacter = new Character();
    mockCharacter.setId(1L);
    Episode mockDbEpisode = mockInputEpisode.obtainDomainObject();
    mockDbEpisode.setId(1L);
    mockDbEpisode.setCharacters(new ArrayList<>(List.of(mockCharacter)));
    mockDbEpisode.setSeason(mockSeason);
    EpisodeInputDTO updatedEpisode = new EpisodeInputDTO();
    given(episodeBO.findOne(mockDbEpisode.getId())).willReturn(mockDbEpisode);
    given(episodeBO.save(any(Episode.class))).willAnswer(invocation -> invocation.getArgument(0));
    when(seasonBO.findOne(updatedEpisode.getSeasonId())).thenReturn(mockSeason);
    when(characterBO.findAllById(updatedEpisode.getCharacterIds()))
        .thenReturn(new ArrayList<>(List.of(mockCharacter)));
    ResultActions response =
        mockMvc.perform(
            patch("/api/episodes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEpisode)));
    response.andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void updateEpisodeNotFoundTest() throws Exception {
    Season mockSeason = new Season();
    mockSeason.setId(1L);
    Character mockCharacter = new Character();
    mockCharacter.setId(1L);
    Episode mockDbEpisode = mockInputEpisode.obtainDomainObject();
    mockDbEpisode.setId(1L);
    mockDbEpisode.setCharacters(new ArrayList<>(List.of(mockCharacter)));
    mockDbEpisode.setSeason(mockSeason);
    EpisodeInputDTO updatedEpisode = new EpisodeInputDTO();
    updatedEpisode.setEpisodeNum(1);
    updatedEpisode.setSeasonId(1L);
    updatedEpisode.setSummary("summary2");
    updatedEpisode.setTitle("title2");
    updatedEpisode.setCharacterIds(new ArrayList<>(List.of(1L)));
    given(episodeBO.findOne(mockDbEpisode.getId())).willReturn(null);
    given(episodeBO.save(any(Episode.class))).willAnswer(invocation -> invocation.getArgument(0));
    when(seasonBO.findOne(updatedEpisode.getSeasonId())).thenReturn(mockSeason);
    when(characterBO.findAllById(updatedEpisode.getCharacterIds()))
        .thenReturn(new ArrayList<>(List.of(mockCharacter)));
    ResultActions response =
        mockMvc.perform(
            patch("/api/episodes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEpisode)));
    response.andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void updateCharactersNotFoundTest() throws Exception {
    Season mockSeason = new Season();
    mockSeason.setId(1L);
    Character mockCharacter = new Character();
    mockCharacter.setId(1L);
    Episode mockDbEpisode = mockInputEpisode.obtainDomainObject();
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
    when(characterBO.findAllById(updatedEpisode.getCharacterIds())).thenReturn(new ArrayList<>());
    ResultActions response =
        mockMvc.perform(
            patch("/api/episodes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEpisode)));
    response.andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void updateSeasonNotFoundTest() throws Exception {
    Season mockSeason = new Season();
    mockSeason.setId(1L);
    Character mockCharacter = new Character();
    mockCharacter.setId(1L);
    Episode mockDbEpisode = mockInputEpisode.obtainDomainObject();
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
    when(seasonBO.findOne(updatedEpisode.getSeasonId())).thenReturn(null);
    when(characterBO.findAllById(updatedEpisode.getCharacterIds()))
        .thenReturn(new ArrayList<>(List.of(mockCharacter)));
    ResultActions response =
        mockMvc.perform(
            patch("/api/episodes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEpisode)));
    response.andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void deleteNotFoundTest() throws Exception {
    Episode mockDbEpisode = mockInputEpisode.obtainDomainObject();
    mockDbEpisode.setId(1L);
    given(episodeBO.exists(mockEpisode.getId())).willReturn(false);
    willDoNothing().given(episodeBO).delete(mockDbEpisode.getId());
    ResultActions response = mockMvc.perform(delete("/api/episodes/{id}", mockDbEpisode.getId()));
    response.andExpect(status().isNotFound()).andDo(print());
  }
}
