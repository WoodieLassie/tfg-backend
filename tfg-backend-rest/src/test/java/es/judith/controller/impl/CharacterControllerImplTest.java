package es.judith.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.judith.bo.ActorBO;
import es.judith.bo.CharacterBO;
import es.judith.bo.JwtBO;
import es.judith.config.JwtFilter;
import es.judith.config.SecurityConfig;
import es.judith.domain.Actor;
import es.judith.domain.Character;
import es.judith.dto.character.CharacterInputDTO;
import es.judith.dto.character.CharacterNoActorsDTO;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = CharacterControllerImpl.class)
@WebMvcTest(CharacterControllerImpl.class)
@Import({
        SecurityConfig.class,
        JwtFilter.class,
        CharacterControllerImplTest.SecurityBeans.class
})
class CharacterControllerImplTest {

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
    private CharacterBO characterBO;
    @MockitoBean
    private ActorBO actorBO;

    @Test
    void findAllOk() throws Exception {
        mockMvc.perform(get("/api/characters"))
                .andExpect(status().isOk());
    }
    @Test
    void findByIdOk() throws Exception {
        CharacterNoActorsDTO characterDTO = new CharacterNoActorsDTO();
        characterDTO.setId(1L);
        Character character = characterDTO.obtainDomainObject();
        character.setActors(List.of());
        given(characterBO.findOne(characterDTO.getId())).willReturn(character);
        mockMvc.perform(get("/api/characters/{id}", characterDTO.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(characterDTO)));
    }
    @Test
    void findByIdNotFound() throws Exception {
        CharacterNoActorsDTO characterDTO = new CharacterNoActorsDTO();
        characterDTO.setId(1L);
        Character character = characterDTO.obtainDomainObject();
        character.setActors(List.of());
        given(characterBO.findOne(characterDTO.getId())).willReturn(null);
        mockMvc.perform(get("/api/characters/{id}", characterDTO.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    void findImageByIdOk() throws Exception {
        String imageData = "askdjfkfdsjl";
        byte[] image = imageData.getBytes();
        Character character = new Character();
        character.setId(1L);
        character.setImageData(image);
        given(characterBO.findImageById(character.getId())).willReturn(image);
        mockMvc.perform(get("/api/characters/image/{id}", character.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().bytes(image));
    }
    @Test
    void findImageByIdNotFound() throws Exception {
        String imageData = "askdjfkfdsjl";
        byte[] image = imageData.getBytes();
        Character character = new Character();
        character.setId(1L);
        character.setImageData(image);
        given(characterBO.findImageById(character.getId())).willReturn(null);
        mockMvc.perform(get("/api/characters/image/{id}", character.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addOk() throws Exception {
        CharacterInputDTO characterDTO = new CharacterInputDTO();
        characterDTO.setAge(20);
        characterDTO.setDescription("desc");
        characterDTO.setGender("gender");
        characterDTO.setName("name");
        characterDTO.setNationality("nationality");
        Character character = characterDTO.obtainDomainObject();
        given(characterBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(characterBO.save(character)).willReturn(character);
        mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(character)))
                .andExpect(status().isCreated())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void addForbiddenNotAdmin() throws Exception {
        CharacterInputDTO characterDTO = new CharacterInputDTO();
        characterDTO.setAge(20);
        characterDTO.setDescription("desc");
        characterDTO.setGender("gender");
        characterDTO.setName("name");
        characterDTO.setNationality("nationality");
        Character character = characterDTO.obtainDomainObject();
        given(characterBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(characterBO.save(character)).willReturn(character);
        mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(character)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void addForbiddenNotLoggedIn() throws Exception {
        CharacterInputDTO characterDTO = new CharacterInputDTO();
        characterDTO.setAge(20);
        characterDTO.setDescription("desc");
        characterDTO.setGender("gender");
        characterDTO.setName("namee");
        characterDTO.setNationality("nationality");
        Character character = characterDTO.obtainDomainObject();
        given(characterBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(characterBO.save(character)).willReturn(character);
        mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(character)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addBadRequestFieldsNotPresent() throws Exception {
        CharacterInputDTO characterDTO = new CharacterInputDTO();
        Character character = characterDTO.obtainDomainObject();
        given(characterBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(characterBO.save(character)).willReturn(character);
        mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(character)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addBadRequestIllegalString() throws Exception {
        CharacterInputDTO characterDTO = new CharacterInputDTO();
        characterDTO.setAge(20);
        characterDTO.setDescription("desc");
        characterDTO.setGender("gender");
        characterDTO.setName("namee///////");
        characterDTO.setNationality("nationality");
        Character character = characterDTO.obtainDomainObject();
        given(characterBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(true);
        given(characterBO.save(character)).willReturn(character);
        mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(character)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateOk() throws Exception {
        Character character = new Character();
        character.setAge(20);
        character.setDescription("desc");
        character.setGender("gender");
        character.setName("namee");
        character.setNationality("nationality");
        character.setId(1L);
        CharacterInputDTO characterDTO = new CharacterInputDTO();
        characterDTO.setNationality("AAAAAAAAAAAAAAA");
        given(characterBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(characterBO.findOne(character.getId())).willReturn(character);
        given(characterBO.save(character)).willReturn(character);
        mockMvc.perform(patch("/api/characters/{id}", character.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(characterDTO)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateNotFound() throws Exception {
        Character character = new Character();
        character.setAge(20);
        character.setDescription("desc");
        character.setGender("gender");
        character.setName("namee");
        character.setNationality("nationality");
        character.setId(1L);
        CharacterInputDTO characterDTO = new CharacterInputDTO();
        characterDTO.setNationality("AAAAAAAAAAAAAAA");
        given(characterBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(characterBO.findOne(character.getId())).willReturn(null);
        given(characterBO.save(character)).willReturn(character);
        mockMvc.perform(patch("/api/characters/{id}", character.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(characterDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateForbiddenNotAdmin() throws Exception {
        Character character = new Character();
        character.setAge(20);
        character.setDescription("desc");
        character.setGender("gender");
        character.setName("namee");
        character.setNationality("nationality");
        character.setId(1L);
        CharacterInputDTO characterDTO = new CharacterInputDTO();
        characterDTO.setNationality("AAAAAAAAAAAAAAA");
        given(characterBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(characterBO.findOne(character.getId())).willReturn(character);
        given(characterBO.save(character)).willReturn(character);
        mockMvc.perform(patch("/api/characters/{id}", character.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(characterDTO)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void updateForbiddenNotLoggedIn() throws Exception {
        Character character = new Character();
        character.setAge(20);
        character.setDescription("desc");
        character.setGender("gender");
        character.setName("namee");
        character.setNationality("nationalityyyyy");
        character.setId(1L);
        CharacterInputDTO characterDTO = new CharacterInputDTO();
        characterDTO.setNationality("AAAAAAAAAAAAAAA");
        given(characterBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(characterBO.findOne(character.getId())).willReturn(character);
        given(characterBO.save(character)).willReturn(character);
        mockMvc.perform(patch("/api/characters/{id}", character.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(characterDTO)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateBadRequestIllegalString() throws Exception {
        Character character = new Character();
        character.setAge(20);
        character.setDescription("desc");
        character.setGender("gender");
        character.setName("namee");
        character.setNationality("nationality/////");
        character.setId(1L);
        CharacterInputDTO characterDTO = new CharacterInputDTO();
        characterDTO.setNationality("AAAAAAAAAAAAAAA");
        given(characterBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(true);
        given(characterBO.findOne(character.getId())).willReturn(character);
        given(characterBO.save(character)).willReturn(character);
        mockMvc.perform(patch("/api/characters/{id}", character.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(characterDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateImageByIdOk() throws Exception {
        Character character = new Character();
        character.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "image/png", "some png2".getBytes());
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/characters/image/{id}", character.getId()).file(newImage);
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(characterBO.findOne(character.getId())).willReturn(character);
        given(characterBO.save(any(Character.class))).willAnswer(invocation -> invocation.getArgument(0));
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isNoContent());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateImageByIdNotFound() throws Exception {
        Character character = new Character();
        character.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "image/png", "some png2".getBytes());
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/characters/image/{id}", character.getId()).file(newImage);
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(characterBO.findOne(character.getId())).willReturn(null);
        given(characterBO.save(any(Character.class))).willAnswer(invocation -> invocation.getArgument(0));
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateImageByIdBadRequestFileNotAnImage() throws Exception {
        Character character = new Character();
        character.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "SOY UN PUTO VIRUS", "some png2".getBytes());
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/characters/image/{id}", character.getId()).file(newImage);
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(characterBO.findOne(character.getId())).willReturn(character);
        given(characterBO.save(any(Character.class))).willAnswer(invocation -> invocation.getArgument(0));
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateImageByIdBadRequestFileNotAttached() throws Exception {
        Character character = new Character();
        character.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "image/png", new byte[0]);
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/characters/image/{id}", character.getId()).file(newImage);
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(characterBO.findOne(character.getId())).willReturn(character);
        given(characterBO.save(any(Character.class))).willAnswer(invocation -> invocation.getArgument(0));
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateImageByIdBadRequestFileTooLarge() throws Exception {
        Character character = new Character();
        character.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "image/png", new byte[10000000]);
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/characters/image/{id}", character.getId()).file(newImage);
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(characterBO.findOne(character.getId())).willReturn(character);
        given(characterBO.save(any(Character.class))).willAnswer(invocation -> invocation.getArgument(0));
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateImageByIdForbiddenNotAdmin() throws Exception {
        Character character = new Character();
        character.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "image/png", "some png2".getBytes());
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/characters/image/{id}", character.getId()).file(newImage);
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(characterBO.findOne(character.getId())).willReturn(character);
        given(characterBO.save(any(Character.class))).willAnswer(invocation -> invocation.getArgument(0));
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isForbidden());
    }
    @Test
    @WithAnonymousUser
    void updateImageByIdForbiddenNotLoggedIn() throws Exception {
        Character character = new Character();
        character.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename22.png", "image/png", "some png2".getBytes());
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/characters/image/{id}", character.getId()).file(newImage);
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(characterBO.findOne(character.getId())).willReturn(character);
        given(characterBO.save(any(Character.class))).willAnswer(invocation -> invocation.getArgument(0));
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteOk() throws Exception {
        given(characterBO.exists(1L)).willReturn(true);
        mockMvc.perform(delete("/api/characters/{id}", 1L))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteNotFound() throws Exception {
        given(characterBO.exists(1L)).willReturn(false);
        mockMvc.perform(delete("/api/characters/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteForbiddenNotAdmin() throws Exception {
        given(characterBO.exists(1L)).willReturn(true);
        mockMvc.perform(delete("/api/characters/{id}", 1L))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void deleteForbiddenNotLoggedIn() throws Exception {
        given(characterBO.exists(2L)).willReturn(true);
        mockMvc.perform(delete("/api/characters/{id}", 2L))
                .andExpect(status().isForbidden())
                .andDo(print());

    }
}
