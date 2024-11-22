package es.alten.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.alten.bo.SeasonBO;
import es.alten.controller.impl.SeasonControllerImpl;
import es.alten.domain.Season;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

  private SeasonInputDTO mockSeason;

  @BeforeEach
  void setUp() {
    mockSeason = new SeasonInputDTO();
    mockSeason.setSeasonNum(1);
    mockSeason.setDescription("desc");
  }
  @Test
  void addTest() throws Exception {
    given(seasonBO.save(any(Season.class))).willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response =
        mockMvc.perform(
            post("/api/seasons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockSeason)));
    response.andDo(print()).andExpect(status().isCreated());
  }
  @Test
  void updateTest() throws Exception {
    Season mockDbSeason = mockSeason.obtainDomainObject();
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
    Season mockDbSeason = mockSeason.obtainDomainObject();
    mockDbSeason.setId(1L);
    willDoNothing().given(seasonBO).delete(mockDbSeason.getId());
    ResultActions response = mockMvc.perform(delete("/api/seasons/{id}", mockDbSeason.getId()));
    response.andExpect(status().isNoContent()).andDo(print());
  }
}
