package es.judith.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.judith.bo.ActorBO;
import es.judith.bo.CharacterBO;
import es.judith.bo.JwtBO;
import es.judith.config.JwtFilter;
import es.judith.config.SecurityConfig;
import es.judith.domain.Actor;
import es.judith.domain.Character;
import es.judith.dto.actor.ActorInputDTO;
import es.judith.dto.actor.ActorNoCharacterDTO;
import es.judith.exceptions.BadInputException;
import es.judith.security.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = ActorControllerImpl.class)
@WebMvcTest(ActorControllerImpl.class)
@Import({
        SecurityConfig.class,
        JwtFilter.class,
        ActorControllerImplTest.SecurityBeans.class
})
class ActorControllerImplTest {

    @TestConfiguration
    static class SecurityBeans {
        @Bean
        BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(10);
        }
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtBO jwtBO;
    @MockitoBean
    private CustomUserDetailsService userDetailsService;
    @MockitoBean
    private ActorBO actorBO;
    @MockitoBean
    private CharacterBO characterBO;

    @Test
    void findAllTestOk() throws Exception {
      mockMvc.perform(get("/api/actors"))
              .andExpect(status().isOk());
    }
    @Test
    void findByIdOk() throws Exception {
        ActorNoCharacterDTO actorDTO = new ActorNoCharacterDTO();
        actorDTO.setId(1L);
        Actor actor = actorDTO.obtainDomainObject();
        given(actorBO.findOne(actor.getId())).willReturn(actor);
        mockMvc.perform(get("/api/actors/{id}", actorDTO.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(actorDTO)));
    }
    @Test
    void findByIdNotFound() throws Exception {
        ActorNoCharacterDTO actorDTO = new ActorNoCharacterDTO();
        actorDTO.setId(2L);
        given(actorBO.findOne(1L)).willReturn(null);
        mockMvc.perform(get("/api/actors/{id}", actorDTO.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void findImageByIdOk() throws Exception {
        String imageData = "askdjfkfdsjl";
        byte[] image = imageData.getBytes();
        Actor actor = new Actor();
        actor.setId(1L);
        actor.setImageData(image);
        given(actorBO.findImageById(actor.getId())).willReturn(image);
        mockMvc.perform(get("/api/actors/image/{id}", actor.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().bytes(image));
    }

    @Test
    void findImageByIdNotFound() throws Exception {
        Actor actor = new Actor();
        actor.setId(1L);
        given(actorBO.findImageById(actor.getId())).willReturn(null);
        mockMvc.perform(get("/api/actors/image/{id}", actor.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void addOk() throws Exception {
        Character character = new Character();
        character.setId(1L);
        List<Long> list = List.of(character.getId());
        List<Character> listCharacters = List.of(character);
        ActorInputDTO actorInputDTO = new ActorInputDTO();
        actorInputDTO.setName("name");
        actorInputDTO.setBirthDate(new Date(1, 1, 1970));
        actorInputDTO.setGender("gender");
        actorInputDTO.setBirthLocation("test");
        actorInputDTO.setNationality("test");
        actorInputDTO.setCharacterIds(list);
        Actor actor = actorInputDTO.obtainDomainObject();
        actor.setCharacters(listCharacters);
        given(actorBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(characterBO.findAllById(list)).willReturn(listCharacters);
        given(actorBO.save(actor)).willReturn(actor);
        mockMvc.perform(post("/api/actors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actorInputDTO)))
                .andExpect(status().isCreated())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void addForbiddenNotAdmin() throws Exception {
        Character character = new Character();
        character.setId(1L);
        List<Long> list = List.of(character.getId());
        List<Character> listCharacters = List.of(character);
        ActorInputDTO actorInputDTO = new ActorInputDTO();
        actorInputDTO.setName("name");
        actorInputDTO.setBirthDate(new Date(1, 1, 1970));
        actorInputDTO.setGender("gender");
        actorInputDTO.setBirthLocation("test");
        actorInputDTO.setNationality("test");
        actorInputDTO.setCharacterIds(list);
        Actor actor = actorInputDTO.obtainDomainObject();
        actor.setCharacters(listCharacters);
        given(actorBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(characterBO.findAllById(list)).willReturn(listCharacters);
        given(actorBO.save(actor)).willReturn(actor);
        mockMvc.perform(post("/api/actors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actorInputDTO)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void addForbiddenNotLoggedIn() throws Exception {
        Character character = new Character();
        character.setId(1L);
        List<Long> list = List.of(character.getId());
        List<Character> listCharacters = List.of(character);
        ActorInputDTO actorInputDTO = new ActorInputDTO();
        actorInputDTO.setName("name");
        actorInputDTO.setBirthDate(new Date(1, 1, 1970));
        actorInputDTO.setGender("gender");
        actorInputDTO.setBirthLocation("test");
        actorInputDTO.setNationality("testest");
        actorInputDTO.setCharacterIds(list);
        Actor actor = actorInputDTO.obtainDomainObject();
        actor.setCharacters(listCharacters);
        given(actorBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(characterBO.findAllById(list)).willReturn(listCharacters);
        given(actorBO.save(actor)).willReturn(actor);
        mockMvc.perform(post("/api/actors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actorInputDTO)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addBadRequestFieldsNotPresent() throws Exception {
        ActorInputDTO actorInputDTO = new ActorInputDTO();
        Actor actor = actorInputDTO.obtainDomainObject();
        given(actorBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(actorBO.save(actor)).willReturn(actor);
        mockMvc.perform(post("/api/actors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actorInputDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addBadRequestIllegalString() throws Exception {
        Character character = new Character();
        character.setId(1L);
        List<Long> list = List.of(character.getId());
        List<Character> listCharacters = List.of(character);
        ActorInputDTO actorInputDTO = new ActorInputDTO();
        actorInputDTO.setName("name");
        actorInputDTO.setBirthDate(new Date(1, 1, 1970));
        actorInputDTO.setGender("gender");
        actorInputDTO.setBirthLocation("test");
        actorInputDTO.setNationality("test//////");
        actorInputDTO.setCharacterIds(list);
        Actor actor = actorInputDTO.obtainDomainObject();
        actor.setCharacters(listCharacters);
        doThrow(new BadInputException()).when(actorBO).throwExceptionIfIllegalStringsAreFound(actorInputDTO.getName(), actorInputDTO.getNationality(), actorInputDTO.getGender(), actorInputDTO.getBirthLocation(), actorInputDTO.getBirthDate());
        given(actorBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(true);
        given(characterBO.findAllById(list)).willReturn(listCharacters);
        given(actorBO.save(actor)).willReturn(actor);
        mockMvc.perform(post("/api/actors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actorInputDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateOk() throws Exception {
        Character character = new Character();
        character.setId(1L);
        List<Long> list = List.of(character.getId());
        List<Character> listCharacters = List.of(character);
        Actor actor = new Actor();
        actor.setId(1L);
        actor.setName("name");
        actor.setBirthDate(new Date(1, 1, 1970));
        actor.setGender("gender");
        actor.setBirthLocation("test");
        actor.setNationality("test");
        actor.setCharacters(listCharacters);
        ActorInputDTO newActorInfo = new ActorInputDTO();
        newActorInfo.setName("que gracioso eres intellij me encantas como IDE, no voy a desinstalarte cuando termine el tfg nononono no voy a hacer eso");
        given(actorBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(actorBO.findOne(actor.getId())).willReturn(actor);
        given(characterBO.findAllById(list)).willReturn(listCharacters);
        given(actorBO.save(actor)).willReturn(actor);
        mockMvc.perform(patch("/api/actors/{id}", actor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newActorInfo)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateNotFound() throws Exception {
        Character character = new Character();
        character.setId(1L);
        List<Long> list = List.of(character.getId());
        List<Character> listCharacters = List.of(character);
        Actor actor = new Actor();
        actor.setId(1L);
        actor.setName("name");
        actor.setBirthDate(new Date(1, 1, 1970));
        actor.setGender("gender");
        actor.setBirthLocation("test");
        actor.setNationality("test");
        actor.setCharacters(listCharacters);
        ActorInputDTO newActorInfo = new ActorInputDTO();
        newActorInfo.setName("name");
        given(actorBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(actorBO.findOne(actor.getId())).willReturn(null);
        given(characterBO.findAllById(list)).willReturn(listCharacters);
        given(actorBO.save(actor)).willReturn(actor);
        mockMvc.perform(patch("/api/actors/{id}", actor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newActorInfo)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateForbiddenNotAdmin() throws Exception {
        Character character = new Character();
        character.setId(1L);
        List<Long> list = List.of(character.getId());
        List<Character> listCharacters = List.of(character);
        Actor actor = new Actor();
        actor.setId(1L);
        actor.setName("name");
        actor.setBirthDate(new Date(1, 1, 1970));
        actor.setGender("gender");
        actor.setBirthLocation("test");
        actor.setNationality("test");
        actor.setCharacters(listCharacters);
        ActorInputDTO newActorInfo = new ActorInputDTO();
        newActorInfo.setName("name");
        given(actorBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(actorBO.findOne(actor.getId())).willReturn(actor);
        given(characterBO.findAllById(list)).willReturn(listCharacters);
        given(actorBO.save(actor)).willReturn(actor);
        mockMvc.perform(patch("/api/actors/{id}", actor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newActorInfo)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    void updateForbiddenNotLoggedIn() throws Exception {
        Character character = new Character();
        character.setId(1L);
        List<Long> list = List.of(character.getId());
        List<Character> listCharacters = List.of(character);
        Actor actor = new Actor();
        actor.setId(1L);
        actor.setName("name");
        actor.setBirthDate(new Date(1, 1, 1970));
        actor.setGender("gender");
        actor.setBirthLocation("test");
        actor.setNationality("test");
        actor.setCharacters(listCharacters);
        ActorInputDTO newActorInfo = new ActorInputDTO();
        newActorInfo.setName("AAAAAAAAAAAAAAAAAAAAAA");
        given(actorBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(actorBO.findOne(actor.getId())).willReturn(actor);
        given(characterBO.findAllById(list)).willReturn(listCharacters);
        given(actorBO.save(actor)).willReturn(actor);
        mockMvc.perform(patch("/api/actors/{id}", actor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newActorInfo)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateBadRequestIllegalString() throws Exception {
        Character character = new Character();
        character.setId(1L);
        List<Long> list = List.of(character.getId());
        List<Character> listCharacters = List.of(character);
        Actor actor = new Actor();
        actor.setId(1L);
        actor.setName("name");
        actor.setBirthDate(new Date(1, 1, 1970));
        actor.setGender("gender");
        actor.setBirthLocation("test");
        actor.setNationality("test");
        actor.setCharacters(listCharacters);
        ActorInputDTO newActorInfo = new ActorInputDTO();
        newActorInfo.setName("////////&·$(%(/$&$(//%DELETE FROM ACTOR * /)(·%($&/(&%");
        given(actorBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(true);
        doThrow(new BadInputException()).when(actorBO).throwExceptionIfIllegalStringsAreFound(newActorInfo.getName(), newActorInfo.getNationality(), newActorInfo.getGender(), newActorInfo.getBirthLocation(), newActorInfo.getBirthDate());
        given(actorBO.findOne(actor.getId())).willReturn(actor);
        given(characterBO.findAllById(list)).willReturn(listCharacters);
        given(actorBO.save(actor)).willReturn(actor);
        mockMvc.perform(patch("/api/actors/{id}", actor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newActorInfo)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateImageByIdOk() throws Exception {
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
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isNoContent());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateImageByIdNotFound() throws Exception {
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
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateImageByIdBadRequestFileNotAnImage() throws Exception {
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
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateImageByIdBadRequestFileNotAttached() throws Exception {
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
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateImageByIdBadRequestFileTooLarge() throws Exception {
        Actor dbActor = new Actor();
        dbActor.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "image/png", new byte[100000000]);
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/actors/image/{id}", dbActor.getId()).file(newImage);
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(actorBO.findOne(dbActor.getId())).willReturn(dbActor);
        given(actorBO.save(any(Actor.class))).willAnswer(invocation -> invocation.getArgument(0));
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteOk() throws Exception {
        given(actorBO.exists(1L)).willReturn(true);
        mockMvc.perform(delete("/api/actors/{id}", 1L))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteNotFound() throws Exception {
        given(actorBO.exists(1L)).willReturn(false);
        mockMvc.perform(delete("/api/actors/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteForbiddenNotAdmin() throws Exception {
        given(actorBO.exists(1L)).willReturn(true);
        mockMvc.perform(delete("/api/actors/{id}", 1L))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    void deleteForbiddenNotLoggedIn() throws Exception {
        given(actorBO.exists(2L)).willReturn(true);
        mockMvc.perform(delete("/api/actors/{id}", 2L))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
}