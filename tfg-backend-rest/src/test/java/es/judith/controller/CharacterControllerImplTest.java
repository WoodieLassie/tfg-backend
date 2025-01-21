package es.judith.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.judith.bo.CharacterBO;
import es.judith.controller.impl.CharacterControllerImpl;
import es.judith.domain.Character;
import es.judith.dto.ActorNoCharacterDTO;
import es.judith.dto.CharacterDTO;
import es.judith.dto.CharacterInputDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ComponentScan(basePackages = "es.judith.cuentame.*")
@Import(CharacterControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = CharacterControllerImpl.class)
@WebMvcTest(CharacterControllerImpl.class)
class CharacterControllerImplTest {
  @Autowired private MockMvc mockMvc;
  @MockBean private CharacterBO characterBO;
  @Autowired private ObjectMapper objectMapper;

  private CharacterInputDTO mockInputCharacter;
  private CharacterDTO mockCharacter;
  private ActorNoCharacterDTO mockActor;

  @BeforeEach
  void setUp() {
    mockInputCharacter = new CharacterInputDTO();
    mockCharacter = new CharacterDTO();
    mockActor = new ActorNoCharacterDTO();
    mockActor.setImageData("image".getBytes());
    mockInputCharacter.setDescription("desc");
    mockInputCharacter.setNationality("nation");
    mockInputCharacter.setGender("gender");
    mockInputCharacter.setAge(23);
    mockInputCharacter.setName("name");
    mockCharacter.setId(1L);
    mockCharacter.setNationality("nationality");
    mockCharacter.setGender("gender");
    mockCharacter.setDescription("desc");
    mockCharacter.setActors(new ArrayList<>(List.of(mockActor)));
    mockCharacter.setAge(23);
    mockCharacter.setName("name");
  }

  @Test
  void findAllTest() throws Exception {
    List<Character> mockCharacterList =
        new ArrayList<>(List.of(mockCharacter.obtainDomainObject()));
    List<CharacterDTO> mockCharacterDTOList = new ArrayList<>(List.of(mockCharacter));
    given(characterBO.findAll()).willReturn(mockCharacterList);
    ResultActions response = mockMvc.perform(get("/api/characters"));
    response
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().json(objectMapper.writeValueAsString(mockCharacterDTOList)));
  }

  @Test
  void findAllNoImageTest() throws Exception {
    mockActor.setImageData(null);
    List<Character> mockCharacterList =
        new ArrayList<>(List.of(mockCharacter.obtainDomainObject()));
    List<CharacterDTO> mockCharacterDTOList = new ArrayList<>(List.of(mockCharacter));
    given(characterBO.findAll()).willReturn(mockCharacterList);
    ResultActions response = mockMvc.perform(get("/api/characters"));
    response
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().json(objectMapper.writeValueAsString(mockCharacterDTOList)));
  }

  @Test
  void findByIdTest() throws Exception {
    Character mockCharacterEntity = mockCharacter.obtainDomainObject();
    given(characterBO.findOne(mockCharacter.getId())).willReturn(mockCharacterEntity);
    ResultActions response = mockMvc.perform(get("/api/characters/{id}", mockCharacter.getId()));
    response
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().json(objectMapper.writeValueAsString(mockCharacter)));
  }

  @Test
  void findByIdNoImageTest() throws Exception {
    mockActor.setImageData(null);
    Character mockCharacterEntity = mockCharacter.obtainDomainObject();
    given(characterBO.findOne(mockCharacter.getId())).willReturn(mockCharacterEntity);
    ResultActions response = mockMvc.perform(get("/api/characters/{id}", mockCharacter.getId()));
    response
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().json(objectMapper.writeValueAsString(mockCharacter)));
  }

  @Test
  void addTest() throws Exception {
    given(characterBO.save(any(Character.class)))
        .willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response =
        mockMvc.perform(
            post("/api/characters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockInputCharacter)));
    response.andDo(print()).andExpect(status().isCreated());
  }

  @Test
  void updateTest() throws Exception {
    Character mockDbCharacter = mockInputCharacter.obtainDomainObject();
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
    Character mockDbCharacter = mockInputCharacter.obtainDomainObject();
    mockDbCharacter.setId(1L);
    given(characterBO.exists(mockCharacter.getId())).willReturn(true);
    willDoNothing().given(characterBO).delete(mockDbCharacter.getId());
    ResultActions response =
        mockMvc.perform(delete("/api/characters/{id}", mockDbCharacter.getId()));
    response.andExpect(status().isNoContent()).andDo(print());
  }

  @Test
  void findByIdNotFoundTest() throws Exception {
    given(characterBO.findOne(mockCharacter.getId())).willReturn(null);
    ResultActions response = mockMvc.perform(get("/api/characters/{id}", mockCharacter.getId()));
    response.andExpect(status().isNotFound()).andDo(print());
  }

  @Test
  void addBadRequestTest() throws Exception {
    mockInputCharacter = new CharacterInputDTO();
    given(characterBO.save(any(Character.class)))
        .willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response =
        mockMvc.perform(
            post("/api/characters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockInputCharacter)));
    response.andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void updateBadRequestTest() throws Exception {
    Character mockDbCharacter = mockInputCharacter.obtainDomainObject();
    mockDbCharacter.setId(1L);
    CharacterInputDTO updatedCharacter = new CharacterInputDTO();
    given(characterBO.findOne(mockDbCharacter.getId())).willReturn(mockDbCharacter);
    given(characterBO.save(any(Character.class)))
        .willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response =
        mockMvc.perform(
            patch("/api/characters/{id}", mockDbCharacter.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCharacter)));
    response.andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void updateNotFoundTest() throws Exception {
    Character mockDbCharacter = mockInputCharacter.obtainDomainObject();
    mockDbCharacter.setId(1L);
    CharacterInputDTO updatedCharacter = new CharacterInputDTO();
    updatedCharacter.setDescription("desc2");
    updatedCharacter.setNationality("nation2");
    updatedCharacter.setGender("gender2");
    updatedCharacter.setAge(25);
    updatedCharacter.setName("name2");
    given(characterBO.findOne(mockDbCharacter.getId())).willReturn(null);
    given(characterBO.save(any(Character.class)))
        .willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response =
        mockMvc.perform(
            patch("/api/characters/{id}", mockDbCharacter.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCharacter)));
    response.andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void deleteNotFoundTest() throws Exception {
    Character mockDbCharacter = mockInputCharacter.obtainDomainObject();
    mockDbCharacter.setId(1L);
    given(characterBO.exists(mockCharacter.getId())).willReturn(false);
    willDoNothing().given(characterBO).delete(mockDbCharacter.getId());
    ResultActions response =
        mockMvc.perform(delete("/api/characters/{id}", mockDbCharacter.getId()));
    response.andExpect(status().isNotFound()).andDo(print());
  }
}
