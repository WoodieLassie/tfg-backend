package es.alten.controller;

import es.alten.domain.Character;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.alten.bo.ActorBO;
import es.alten.bo.CharacterBO;
import es.alten.dto.ActorInputDTO;
import es.alten.controller.impl.ActorControllerImpl;
import es.alten.domain.Actor;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

  private ActorInputDTO mockActor;
  private MockMultipartFile mockImageData;

  @BeforeEach
  void setUp() {
    mockImageData =
        new MockMultipartFile("image", "filename.png", "image/png", "some png".getBytes());
    mockActor = new ActorInputDTO();
    mockActor.setName("name");
    mockActor.setGender("gender");
    mockActor.setBirthDate(new Date(1, 1, 1));
    mockActor.setNationality("nationality");
    mockActor.setBirthLocation("location");
    mockActor.setCharacterId(1L);
  }

  @Test
  void addTest() throws Exception {
    given(actorBO.save(any(Actor.class))).willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response =
        mockMvc.perform(
            post("/api/actors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockActor)));
    response.andDo(print()).andExpect(status().isCreated());
  }

  @Test
  void updateTest() throws Exception {
    Actor mockDbActor = mockActor.obtainDomainObject();
    mockDbActor.setId(1L);
    Character mockCharacter = new Character();
    mockCharacter.setId(1L);
    mockDbActor.setCharacter(mockCharacter);
    ActorInputDTO updatedActor = new ActorInputDTO();
    updatedActor.setName("name2");
    updatedActor.setGender("gender2");
    updatedActor.setBirthDate(new Date(2, 2, 2));
    updatedActor.setNationality("nationality2");
    updatedActor.setBirthLocation("location2");
    updatedActor.setCharacterId(1L);
    given(actorBO.findOne(mockDbActor.getId())).willReturn(mockDbActor);
    given(actorBO.save(any(Actor.class))).willAnswer(invocation -> invocation.getArgument(0));
    when(characterBO.findOne(updatedActor.getCharacterId())).thenReturn(mockCharacter);
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
    Actor mockDbActor = mockActor.obtainDomainObject();
    mockDbActor.setId(1L);
    willDoNothing().given(actorBO).delete(mockDbActor.getId());
    ResultActions response = mockMvc.perform(delete("/api/actors/{id}", mockDbActor.getId()));
    response.andExpect(status().isNoContent()).andDo(print());
  }
}
