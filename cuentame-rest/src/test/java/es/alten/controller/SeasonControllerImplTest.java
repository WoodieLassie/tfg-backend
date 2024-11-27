package es.alten.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.alten.bo.SeasonBO;
import es.alten.controller.impl.SeasonControllerImpl;
import es.alten.domain.Season;
import es.alten.dto.CharacterDTO;
import es.alten.dto.EpisodeNoSeasonDTO;
import es.alten.dto.SeasonDTO;
import es.alten.dto.SeasonInputDTO;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ComponentScan(basePackages = "es.alten.cuentame.*")
@Import(SeasonControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = SeasonControllerImpl.class)
@WebMvcTest(SeasonControllerImpl.class)
class SeasonControllerImplTest {
  @Autowired private MockMvc mockMvc;
  @MockBean private SeasonBO seasonBO;
  @Autowired private ObjectMapper objectMapper;

  private SeasonInputDTO mockInputSeason;
  private SeasonDTO mockSeason;
  private EpisodeNoSeasonDTO mockEpisode;
  private CharacterDTO mockCharacter;

  @BeforeEach
  void setUp() {
    mockInputSeason = new SeasonInputDTO();
    mockCharacter = new CharacterDTO();
    mockEpisode = new EpisodeNoSeasonDTO();
    mockSeason = new SeasonDTO();
    mockInputSeason.setSeasonNum(1);
    mockInputSeason.setDescription("desc");
    mockCharacter.setId(1L);
    mockCharacter.setAge(23);
    mockCharacter.setNationality("nationality");
    mockCharacter.setGender("gender");
    mockCharacter.setActors(new ArrayList<>());
    mockCharacter.setDescription("desc");
    mockCharacter.setName("name");
    mockEpisode.setEpisodeNum(1);
    mockEpisode.setTitle("title");
    mockEpisode.setId(1L);
    mockEpisode.setSummary("summary");
    mockEpisode.setCharacters(List.of(mockCharacter));
    mockSeason.setId(1L);
    mockSeason.setDescription("desc");
    mockSeason.setSeasonNum(1);
    mockSeason.setEpisodes(List.of(mockEpisode));
  }

  @Test
  void findAllTest() throws Exception {
    List<Season> mockSeasonList = new ArrayList<>(List.of(mockSeason.obtainDomainObject()));
    List<SeasonDTO> mockSeasonDTOList = new ArrayList<>(List.of(mockSeason));
    given(seasonBO.findAll()).willReturn(mockSeasonList);
    ResultActions response = mockMvc.perform(get("/api/seasons"));
    response
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().json(objectMapper.writeValueAsString(mockSeasonDTOList)));
  }

  @Test
  void findAllByCharacterTest() throws Exception {
    List<Season> mockSeasonList = new ArrayList<>(List.of(mockSeason.obtainDomainObject()));
    List<SeasonDTO> mockSeasonDTOList = new ArrayList<>(List.of(mockSeason));
    given(seasonBO.findAllByCharacters(mockCharacter.getName())).willReturn(mockSeasonList);
    ResultActions response =
        mockMvc.perform(get("/api/seasons/sorted").param("characterName", mockCharacter.getName()));
    response
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().json(objectMapper.writeValueAsString(mockSeasonDTOList)));
  }

  @Test
  void findByIdTest() throws Exception {
    Season mockSeasonEntity = mockSeason.obtainDomainObject();
    given(seasonBO.findOne(mockSeason.getId())).willReturn(mockSeasonEntity);
    ResultActions response = mockMvc.perform(get("/api/seasons/{id}", mockSeason.getId()));
    response
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().json(objectMapper.writeValueAsString(mockSeason)));
  }

  @Test
  void addTest() throws Exception {
    given(seasonBO.save(any(Season.class))).willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response =
        mockMvc.perform(
            post("/api/seasons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockInputSeason)));
    response.andDo(print()).andExpect(status().isCreated());
  }

  @Test
  void updateTest() throws Exception {
    Season mockDbSeason = mockInputSeason.obtainDomainObject();
    mockDbSeason.setId(1L);
    Season updatedSeason = new Season();
    updatedSeason.setDescription("desc2");
    updatedSeason.setSeasonNum(2);
    given(seasonBO.findOne(mockDbSeason.getId())).willReturn(mockDbSeason);
    given(seasonBO.save(any(Season.class))).willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response =
        mockMvc.perform(
            patch("/api/seasons/{id}", mockDbSeason.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedSeason)));
    response.andDo(print()).andExpect(status().isNoContent());
  }

  @Test
  void deleteTest() throws Exception {
    Season mockDbSeason = mockInputSeason.obtainDomainObject();
    mockDbSeason.setId(1L);
    given(seasonBO.exists(mockSeason.getId())).willReturn(true);
    willDoNothing().given(seasonBO).delete(mockDbSeason.getId());
    ResultActions response = mockMvc.perform(delete("/api/seasons/{id}", mockDbSeason.getId()));
    response.andExpect(status().isNoContent()).andDo(print());
  }

  @Test
  void findByIdNotFoundTest() throws Exception {
    given(seasonBO.findOne(mockSeason.getId())).willReturn(null);
    ResultActions response = mockMvc.perform(get("/api/seasons/{id}", mockSeason.getId()));
    response.andExpect(status().isNotFound()).andDo(print());
  }

  @Test
  void addBadRequestTest() throws Exception {
    mockInputSeason = new SeasonInputDTO();
    given(seasonBO.save(any(Season.class))).willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response =
        mockMvc.perform(
            post("/api/seasons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockInputSeason)));
    response.andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void addConflictTest() throws Exception {
    given(seasonBO.save(any(Season.class))).willAnswer(invocation -> invocation.getArgument(0));
    when(seasonBO.existsBySeasonNum(mockInputSeason.getSeasonNum())).thenReturn(true);
    ResultActions response =
        mockMvc.perform(
            post("/api/seasons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockInputSeason)));
    response.andDo(print()).andExpect(status().isConflict());
  }

  @Test
  void updateBadRequestTest() throws Exception {
    Season mockDbSeason = mockInputSeason.obtainDomainObject();
    mockDbSeason.setId(1L);
    Season updatedSeason = new Season();
    given(seasonBO.findOne(mockDbSeason.getId())).willReturn(mockDbSeason);
    given(seasonBO.save(any(Season.class))).willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response =
        mockMvc.perform(
            patch("/api/seasons/{id}", mockDbSeason.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedSeason)));
    response.andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void updateConflictTest() throws Exception {
    Season mockDbSeason = mockInputSeason.obtainDomainObject();
    mockDbSeason.setId(1L);
    Season updatedSeason = new Season();
    updatedSeason.setDescription("desc2");
    updatedSeason.setSeasonNum(2);
    given(seasonBO.existsBySeasonNum(updatedSeason.getSeasonNum())).willReturn(true);
    given(seasonBO.findOne(mockDbSeason.getId())).willReturn(mockDbSeason);
    given(seasonBO.save(any(Season.class))).willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response =
        mockMvc.perform(
            patch("/api/seasons/{id}", mockDbSeason.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedSeason)));
    response.andDo(print()).andExpect(status().isConflict());
  }

  @Test
  void updateNotFoundTest() throws Exception {
    Season mockDbSeason = mockInputSeason.obtainDomainObject();
    mockDbSeason.setId(1L);
    Season updatedSeason = new Season();
    updatedSeason.setDescription("desc2");
    updatedSeason.setSeasonNum(2);
    given(seasonBO.findOne(mockDbSeason.getId())).willReturn(null);
    given(seasonBO.save(any(Season.class))).willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response =
        mockMvc.perform(
            patch("/api/seasons/{id}", mockDbSeason.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedSeason)));
    response.andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void deleteNotFoundTest() throws Exception {
    Season mockDbSeason = mockInputSeason.obtainDomainObject();
    mockDbSeason.setId(1L);
    given(seasonBO.exists(mockSeason.getId())).willReturn(false);
    willDoNothing().given(seasonBO).delete(mockDbSeason.getId());
    ResultActions response = mockMvc.perform(delete("/api/seasons/{id}", mockDbSeason.getId()));
    response.andExpect(status().isNotFound()).andDo(print());
  }
}
