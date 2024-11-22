package es.alten.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.alten.bo.CharacterBO;
import es.alten.controller.impl.CharacterControllerImpl;
import es.alten.domain.Actor;
import es.alten.domain.Character;
import es.alten.dto.CharacterInputDTO;
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
@Import(CharacterControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = CharacterControllerImpl.class)
@WebMvcTest(CharacterControllerImpl.class)
class CharacterControllerImplTest {
  @Autowired private MockMvc mockMvc;
  @MockBean private CharacterBO characterBO;
  @Autowired private ObjectMapper objectMapper;

  private CharacterInputDTO mockCharacter;

  @BeforeEach
  void setUp() {
    mockCharacter = new CharacterInputDTO();
    mockCharacter.setDescription("desc");
    mockCharacter.setNationality("nation");
    mockCharacter.setGender("gender");
    mockCharacter.setAge(23);
    mockCharacter.setName("name");
  }

  @Test
  void addTest() throws Exception {
    given(characterBO.save(any(Character.class)))
        .willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response =
        mockMvc.perform(
            post("/api/characters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockCharacter)));
    response.andDo(print()).andExpect(status().isCreated());
  }

  @Test
  void updateTest() throws Exception {
    Character mockDbCharacter = mockCharacter.obtainDomainObject();
    mockDbCharacter.setId(1L);
    CharacterInputDTO updatedCharacter = new CharacterInputDTO();
    updatedCharacter.setDescription("desc2");
    updatedCharacter.setNationality("nation2");
    updatedCharacter.setGender("gender2");
    updatedCharacter.setAge(25);
    updatedCharacter.setName("name2");
    given(characterBO.findOne(mockDbCharacter.getId())).willReturn(mockDbCharacter);
    given(characterBO.save(any(Character.class)))
        .willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response =
        mockMvc.perform(
            patch("/api/characters/{id}", mockDbCharacter.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCharacter)));
    response.andDo(print()).andExpect(status().isNoContent());
  }
  @Test
  void deleteTest() throws Exception {
    Character mockDbCharacter = mockCharacter.obtainDomainObject();
    mockDbCharacter.setId(1L);
    willDoNothing().given(characterBO).delete(mockDbCharacter.getId());
    ResultActions response = mockMvc.perform(delete("/api/characters/{id}", mockDbCharacter.getId()));
    response.andExpect(status().isNoContent()).andDo(print());
  }
}
