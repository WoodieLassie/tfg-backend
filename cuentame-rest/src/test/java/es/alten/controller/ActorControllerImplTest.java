package es.alten.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.alten.bo.ActorBO;
import es.alten.bo.CharacterBO;
import es.alten.dto.ActorDTO;
import es.alten.dto.ActorInputDTO;
import es.alten.controller.impl.ActorControllerImpl;
import es.alten.domain.Actor;
import es.alten.dto.CharacterNoActorsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ComponentScan(basePackages = "es.alten.cuentame.*")
@Import(ActorControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = ActorControllerImpl.class)
@WebMvcTest(ActorControllerImpl.class)
class ActorControllerImplTest {
  @Autowired private MockMvc mockMvc;
  @MockBean private ActorBO actorBO;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private CharacterBO characterBO;

  private ActorInputDTO mockInputActor;
  private ActorDTO mockActor;
  private CharacterNoActorsDTO mockCharacter;

  @BeforeEach
  void setUp() {
    mockInputActor = new ActorInputDTO();
    mockCharacter = new CharacterNoActorsDTO();
    mockInputActor.setName("name");
    mockInputActor.setGender("gender");
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 1);
    calendar.set(Calendar.MONTH, Calendar.JANUARY);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    mockInputActor.setBirthDate(new Date(calendar.getTimeInMillis()));
    mockInputActor.setNationality("nationality");
    mockInputActor.setBirthLocation("location");
    mockInputActor.setCharacterId(1L);
    mockActor = new ActorDTO();
    mockActor.setId(1L);
    mockActor.setName("name");
    mockActor.setGender("gender");
    mockActor.setBirthDate(new Date(calendar.getTimeInMillis()));
    mockActor.setNationality("nationality");
    mockActor.setBirthLocation("location");
    mockCharacter.setId(1L);
    mockCharacter.setName("name");
    mockCharacter.setNationality("nationality");
    mockCharacter.setGender("gender");
    mockCharacter.setAge(23);
    mockCharacter.setDescription("desc");
    mockActor.setCharacter(mockCharacter);
    mockActor.setImageData("image".getBytes());
  }

  @Test
  void findAllTest() throws Exception {
    List<Actor> mockActorList = new ArrayList<>(List.of(mockActor.obtainDomainObject()));
    List<ActorDTO> mockActorDTOList = new ArrayList<>(List.of(mockActor));
    given(actorBO.findAll()).willReturn(mockActorList);
    when(characterBO.findOne(mockActor.getCharacter().getId()))
        .thenReturn(mockCharacter.obtainDomainObject());
    ResultActions response = mockMvc.perform(get("/api/actors"));
    response
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().json(objectMapper.writeValueAsString(mockActorDTOList)));
  }

  @Test
  void findByIdTest() throws Exception {
    Actor mockActorEntity = mockActor.obtainDomainObject();
    given(actorBO.findOne(mockActor.getId())).willReturn(mockActorEntity);
    when(characterBO.findOne(mockActor.getCharacter().getId()))
        .thenReturn(mockCharacter.obtainDomainObject());
    ResultActions response = mockMvc.perform(get("/api/actors/{id}", mockActor.getId()));
    response
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().json(objectMapper.writeValueAsString(mockActor)));
  }

  @Test
  void findImageById() throws Exception {
    Actor mockActorEntity = mockActor.obtainDomainObject();
    given(actorBO.findImageById(mockActor.getId())).willReturn(mockActorEntity.getImageData());
    ResultActions response = mockMvc.perform(get("/api/actors/image/{id}", mockActor.getId()));
    response
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().bytes(mockActor.getImageData()));
  }

  @Test
  void addTest() throws Exception {
    given(actorBO.save(any(Actor.class))).willAnswer(invocation -> invocation.getArgument(0));
    when(characterBO.findOne(mockActor.getCharacter().getId()))
        .thenReturn(mockCharacter.obtainDomainObject());
    ResultActions response =
        mockMvc.perform(
            post("/api/actors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockInputActor)));
    response.andDo(print()).andExpect(status().isCreated());
  }

  @Test
  void updateTest() throws Exception {
    Actor mockDbActor = mockInputActor.obtainDomainObject();
    mockDbActor.setId(1L);
    mockDbActor.setCharacter(mockCharacter.obtainDomainObject());
    ActorInputDTO updatedActor = new ActorInputDTO();
    updatedActor.setName("name2");
    updatedActor.setGender("gender2");
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 2);
    calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
    calendar.set(Calendar.DAY_OF_MONTH, 2);
    updatedActor.setBirthDate(new Date(calendar.getTimeInMillis()));
    updatedActor.setNationality("nationality2");
    updatedActor.setBirthLocation("location2");
    updatedActor.setCharacterId(1L);
    given(actorBO.findOne(mockDbActor.getId())).willReturn(mockDbActor);
    given(actorBO.save(any(Actor.class))).willAnswer(invocation -> invocation.getArgument(0));
    when(characterBO.findOne(updatedActor.getCharacterId()))
        .thenReturn(mockCharacter.obtainDomainObject());
    ResultActions response =
        mockMvc.perform(
            patch("/api/actors/{id}", mockDbActor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedActor)));
    response.andDo(print()).andExpect(status().isNoContent());
  }

  @Test
  void updateImageTest() throws Exception {
    Actor dbActor = new Actor();
    dbActor.setId(1L);
    MockMultipartFile newImage =
        new MockMultipartFile("image", "filename2.png", "image/png", "some png2".getBytes());
    MockMultipartHttpServletRequestBuilder builder =
        MockMvcRequestBuilders.multipart("/api/actors/image/{id}", dbActor.getId()).file(newImage);
    builder.with(
        request -> {
          request.setMethod("PATCH");
          return request;
        });
    given(actorBO.findOne(dbActor.getId())).willReturn(dbActor);
    given(actorBO.save(any(Actor.class))).willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response = mockMvc.perform(builder.file(newImage));
    response.andDo(print()).andExpect(status().isNoContent());
  }

  @Test
  void deleteTest() throws Exception {
    Actor mockDbActor = mockInputActor.obtainDomainObject();
    mockDbActor.setId(1L);
    given(actorBO.exists(mockActor.getId())).willReturn(true);
    willDoNothing().given(actorBO).delete(mockDbActor.getId());
    ResultActions response = mockMvc.perform(delete("/api/actors/{id}", mockDbActor.getId()));
    response.andExpect(status().isNoContent()).andDo(print());
  }

  @Test
  void findImageByIdNotFoundImageIsNullTest() throws Exception {
    mockActor.setImageData(null);
    Actor mockActorEntity = mockActor.obtainDomainObject();
    given(actorBO.findImageById(mockActor.getId())).willReturn(mockActorEntity.getImageData());
    ResultActions response = mockMvc.perform(get("/api/actors/image/{id}", mockActor.getId()));
    response.andExpect(status().isNotFound()).andDo(print());
  }

  @Test
  void findImageByIdNotFoundImageIsEmptyTest() throws Exception {
    mockActor.setImageData(new byte[0]);
    Actor mockActorEntity = mockActor.obtainDomainObject();
    given(actorBO.findImageById(mockActor.getId())).willReturn(mockActorEntity.getImageData());
    ResultActions response = mockMvc.perform(get("/api/actors/image/{id}", mockActor.getId()));
    response.andExpect(status().isNotFound()).andDo(print());
  }

  @Test
  void findByIdNotFoundTest() throws Exception {
    given(actorBO.findOne(mockActor.getId())).willReturn(null);
    ResultActions response = mockMvc.perform(get("/api/actors/{id}", mockActor.getId()));
    response.andExpect(status().isNotFound()).andDo(print());
  }

  @Test
  void addBadRequestTest() throws Exception {
    mockInputActor = new ActorInputDTO();
    ResultActions response =
        mockMvc.perform(
            post("/api/actors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockInputActor)));
    response.andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void addNotFoundTest() throws Exception {
    given(actorBO.save(any(Actor.class))).willAnswer(invocation -> invocation.getArgument(0));
    when(characterBO.findOne(mockActor.getCharacter().getId())).thenReturn(null);
    ResultActions response =
        mockMvc.perform(
            post("/api/actors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockInputActor)));
    response.andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void updateBadRequestTest() throws Exception {
    Actor mockDbActor = mockInputActor.obtainDomainObject();
    mockDbActor.setId(1L);
    mockDbActor.setCharacter(mockCharacter.obtainDomainObject());
    ActorInputDTO updatedActor = new ActorInputDTO();
    given(actorBO.findOne(mockDbActor.getId())).willReturn(mockDbActor);
    given(actorBO.save(any(Actor.class))).willAnswer(invocation -> invocation.getArgument(0));
    when(characterBO.findOne(updatedActor.getCharacterId()))
        .thenReturn(mockCharacter.obtainDomainObject());
    ResultActions response =
        mockMvc.perform(
            patch("/api/actors/{id}", mockDbActor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedActor)));
    response.andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void updateNewActorNotFoundTest() throws Exception {
    Actor mockDbActor = mockInputActor.obtainDomainObject();
    mockDbActor.setId(1L);
    mockDbActor.setCharacter(mockCharacter.obtainDomainObject());
    ActorInputDTO updatedActor = new ActorInputDTO();
    updatedActor.setName("name2");
    updatedActor.setGender("gender2");
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 2);
    calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
    calendar.set(Calendar.DAY_OF_MONTH, 2);
    updatedActor.setBirthDate(new Date(calendar.getTimeInMillis()));
    updatedActor.setNationality("nationality2");
    updatedActor.setBirthLocation("location2");
    updatedActor.setCharacterId(1L);
    given(actorBO.findOne(mockDbActor.getId())).willReturn(null);
    given(actorBO.save(any(Actor.class))).willAnswer(invocation -> invocation.getArgument(0));
    when(characterBO.findOne(updatedActor.getCharacterId()))
        .thenReturn(mockCharacter.obtainDomainObject());
    ResultActions response =
        mockMvc.perform(
            patch("/api/actors/{id}", mockDbActor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedActor)));
    response.andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void updateCharacterNotFoundTest() throws Exception {
    Actor mockDbActor = mockInputActor.obtainDomainObject();
    mockDbActor.setId(1L);
    mockDbActor.setCharacter(mockCharacter.obtainDomainObject());
    ActorInputDTO updatedActor = new ActorInputDTO();
    updatedActor.setName("name2");
    updatedActor.setGender("gender2");
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 2);
    calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
    calendar.set(Calendar.DAY_OF_MONTH, 2);
    updatedActor.setBirthDate(new Date(calendar.getTimeInMillis()));
    updatedActor.setNationality("nationality2");
    updatedActor.setBirthLocation("location2");
    updatedActor.setCharacterId(1L);
    given(actorBO.findOne(mockDbActor.getId())).willReturn(mockDbActor);
    given(actorBO.save(any(Actor.class))).willAnswer(invocation -> invocation.getArgument(0));
    when(characterBO.findOne(updatedActor.getCharacterId())).thenReturn(null);
    ResultActions response =
        mockMvc.perform(
            patch("/api/actors/{id}", mockDbActor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedActor)));
    response.andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void updateImageByIdNotAttachedBadRequestTest() throws Exception {
    Actor dbActor = new Actor();
    dbActor.setId(1L);
    MockMultipartFile newImage =
        new MockMultipartFile("image", "filename2.png", "image/png", new byte[0]);
    MockMultipartHttpServletRequestBuilder builder =
        MockMvcRequestBuilders.multipart("/api/actors/image/{id}", dbActor.getId()).file(newImage);
    builder.with(
        request -> {
          request.setMethod("PATCH");
          return request;
        });
    given(actorBO.findOne(dbActor.getId())).willReturn(dbActor);
    given(actorBO.save(any(Actor.class))).willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response = mockMvc.perform(builder.file(newImage));
    response.andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void updateImageByIdNotAnImageBadRequestTest() throws Exception {
    Actor dbActor = new Actor();
    dbActor.setId(1L);
    MockMultipartFile newImage =
        new MockMultipartFile("image", "filename2.png", "xDDDD", "some png2".getBytes());
    MockMultipartHttpServletRequestBuilder builder =
        MockMvcRequestBuilders.multipart("/api/actors/image/{id}", dbActor.getId()).file(newImage);
    builder.with(
        request -> {
          request.setMethod("PATCH");
          return request;
        });
    given(actorBO.findOne(dbActor.getId())).willReturn(dbActor);
    given(actorBO.save(any(Actor.class))).willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response = mockMvc.perform(builder.file(newImage));
    response.andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void updateImageByIdNotFoundTest() throws Exception {
    Actor dbActor = new Actor();
    dbActor.setId(1L);
    MockMultipartFile newImage =
        new MockMultipartFile("image", "filename2.png", "image/png", "some png2".getBytes());
    MockMultipartHttpServletRequestBuilder builder =
        MockMvcRequestBuilders.multipart("/api/actors/image/{id}", dbActor.getId()).file(newImage);
    builder.with(
        request -> {
          request.setMethod("PATCH");
          return request;
        });
    given(actorBO.findOne(dbActor.getId())).willReturn(null);
    given(actorBO.save(any(Actor.class))).willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response = mockMvc.perform(builder.file(newImage));
    response.andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void deleteNotFoundTest() throws Exception {
    Actor mockDbActor = mockInputActor.obtainDomainObject();
    mockDbActor.setId(1L);
    given(actorBO.exists(mockActor.getId())).willReturn(false);
    willDoNothing().given(actorBO).delete(mockDbActor.getId());
    ResultActions response = mockMvc.perform(delete("/api/actors/{id}", mockDbActor.getId()));
    response.andExpect(status().isNotFound()).andDo(print());
  }
}
