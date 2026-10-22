package es.judith.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.judith.bo.JwtBO;
import es.judith.bo.SeasonBO;
import es.judith.bo.ShowBO;
import es.judith.config.JwtFilter;
import es.judith.config.SecurityConfig;
import es.judith.domain.Show;
import es.judith.dto.show.ShowDTO;
import es.judith.dto.show.ShowInputDTO;
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
@ContextConfiguration(classes = ShowControllerImpl.class)
@WebMvcTest(ShowControllerImpl.class)
@Import({
        SecurityConfig.class,
        JwtFilter.class,
        ShowControllerImplTest.SecurityBeans.class
})
class ShowControllerImplTest {

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
    private ShowBO showBO;
    @MockitoBean
    private SeasonBO seasonBO;

    @Test
    void findAllOk() throws Exception {
        mockMvc.perform(get("/api/shows"))
                .andExpect(status().isOk());
    }
    @Test
    void findByIdOk() throws Exception {
        ShowDTO showDTO = new ShowDTO();
        showDTO.setName("test");
        Show show = showDTO.obtainDomainObject();
        show.setSeasons(List.of());
        show.setId(1L);
        given(showBO.findOne(show.getId())).willReturn(show);
        given(showBO.convertToShowDTO(show)).willReturn(showDTO);
        mockMvc.perform(get("/api/shows/{id}", show.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(showDTO)));
    }
    @Test
    void findByIdNotFound() throws Exception {
        ShowDTO showDTO = new ShowDTO();
        showDTO.setName("test");
        Show show = showDTO.obtainDomainObject();
        show.setSeasons(List.of());
        show.setId(1L);
        given(showBO.findOne(show.getId())).willReturn(null);
        given(showBO.convertToShowDTO(show)).willReturn(showDTO);
        mockMvc.perform(get("/api/shows/{id}", show.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    void findByImageIdOk() throws Exception {
        String imageData = "askdjfkfdsjl";
        byte[] image = imageData.getBytes();
        Show show = new Show();
        show.setId(1L);
        show.setImageData(image);
        given(showBO.findImageById(show.getId())).willReturn(image);
        mockMvc.perform(get("/api/shows/image/{id}", show.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().bytes(image));
    }
    @Test
    void findByImageIdNotFound() throws Exception {
        String imageData = "askdjfkfdsjl";
        byte[] image = imageData.getBytes();
        Show show = new Show();
        show.setId(1L);
        show.setImageData(image);
        given(showBO.findImageById(show.getId())).willReturn(null);
        mockMvc.perform(get("/api/shows/image/{id}", show.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addOk() throws Exception {
        ShowInputDTO showDTO = new ShowInputDTO();
        showDTO.setDescription("desc");
        showDTO.setName("name");
        Show show = showDTO.obtainDomainObject();
        given(showBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(showBO.save(show)).willReturn(show);
        mockMvc.perform(post("/api/shows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(show)))
                .andExpect(status().isCreated())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void addForbiddenNotAdmin() throws Exception {
        ShowInputDTO showDTO = new ShowInputDTO();
        showDTO.setDescription("desc");
        showDTO.setName("name");
        Show show = showDTO.obtainDomainObject();
        given(showBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(showBO.save(show)).willReturn(show);
        mockMvc.perform(post("/api/shows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(show)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void addForbiddenNotLoggedIn() throws Exception {
        ShowInputDTO showDTO = new ShowInputDTO();
        showDTO.setDescription("desc");
        showDTO.setName("namee");
        Show show = showDTO.obtainDomainObject();
        given(showBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(showBO.save(show)).willReturn(show);
        mockMvc.perform(post("/api/shows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(show)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addBadRequestFieldsNotPresent() throws Exception {
        ShowInputDTO showDTO = new ShowInputDTO();
        Show show = showDTO.obtainDomainObject();
        given(showBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(showBO.save(show)).willReturn(show);
        mockMvc.perform(post("/api/shows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(show)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addBadRequestIllegalString() throws Exception {
        ShowInputDTO showDTO = new ShowInputDTO();
        showDTO.setDescription("desc");
        showDTO.setName("name/////");
        Show show = showDTO.obtainDomainObject();
        given(showBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(true);
        given(showBO.save(show)).willReturn(show);
        mockMvc.perform(post("/api/shows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(show)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateOk() throws Exception {
        Show show = new Show();
        show.setId(1L);
        show.setDescription("desc");
        show.setName("name");
        ShowInputDTO newShowInfo = new ShowInputDTO();
        newShowInfo.setName("nameee");
        given(showBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(showBO.findOne(show.getId())).willReturn(show);
        given(showBO.save(show)).willReturn(show);
        mockMvc.perform(patch("/api/shows/{id}", show.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newShowInfo)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateForbiddenNotAdmin() throws Exception {
        Show show = new Show();
        show.setId(1L);
        show.setDescription("desc");
        show.setName("name");
        ShowInputDTO newShowInfo = new ShowInputDTO();
        newShowInfo.setName("nameee");
        given(showBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(showBO.findOne(show.getId())).willReturn(show);
        given(showBO.save(show)).willReturn(show);
        mockMvc.perform(patch("/api/shows/{id}", show.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newShowInfo)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void updateForbiddenNotLoggedIn() throws Exception {
        Show show = new Show();
        show.setId(1L);
        show.setDescription("desc");
        show.setName("namee");
        ShowInputDTO newShowInfo = new ShowInputDTO();
        newShowInfo.setName("nameee");
        given(showBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(showBO.findOne(show.getId())).willReturn(show);
        given(showBO.save(show)).willReturn(show);
        mockMvc.perform(patch("/api/shows/{id}", show.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newShowInfo)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateNotFound() throws Exception {
        Show show = new Show();
        show.setId(1L);
        show.setDescription("desc");
        show.setName("name");
        ShowInputDTO newShowInfo = new ShowInputDTO();
        newShowInfo.setName("nameee");
        given(showBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(showBO.findOne(show.getId())).willReturn(null);
        given(showBO.save(show)).willReturn(show);
        mockMvc.perform(patch("/api/shows/{id}", show.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newShowInfo)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateBadRequestIllegalString() throws Exception {
        Show show = new Show();
        show.setId(1L);
        show.setDescription("desc");
        show.setName("name////");
        ShowInputDTO newShowInfo = new ShowInputDTO();
        newShowInfo.setName("nameee");
        given(showBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(true);
        given(showBO.findOne(show.getId())).willReturn(show);
        given(showBO.save(show)).willReturn(show);
        mockMvc.perform(patch("/api/shows/{id}", show.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newShowInfo)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateImageByIdOk() throws Exception {
        Show show = new Show();
        show.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "image/png", "some png2".getBytes());
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/shows/image/{id}", show.getId()).file(newImage);
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(showBO.findOne(show.getId())).willReturn(show);
        given(showBO.save(any(Show.class))).willAnswer(invocation -> invocation.getArgument(0));
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isNoContent());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateImageByIdForbiddenNotAdmin() throws Exception {
        Show show = new Show();
        show.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "image/png", "some png2".getBytes());
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/shows/image/{id}", show.getId()).file(newImage);
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(showBO.findOne(show.getId())).willReturn(show);
        given(showBO.save(any(Show.class))).willAnswer(invocation -> invocation.getArgument(0));
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isForbidden());
    }
    @Test
    @WithAnonymousUser
    void updateImageByIdForbiddenNotLoggedIn() throws Exception {
        Show show = new Show();
        show.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "image/png", "some png22".getBytes());
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/shows/image/{id}", show.getId()).file(newImage);
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(showBO.findOne(show.getId())).willReturn(show);
        given(showBO.save(any(Show.class))).willAnswer(invocation -> invocation.getArgument(0));
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateImageByIdBadRequestFileNotAnImage() throws Exception {
        Show show = new Show();
        show.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "hacer tests = purgatorio", "some png2".getBytes());
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/shows/image/{id}", show.getId()).file(newImage);
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(showBO.findOne(show.getId())).willReturn(show);
        given(showBO.save(any(Show.class))).willAnswer(invocation -> invocation.getArgument(0));
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateImageByIdBadRequestFileNotAttached() throws Exception {
        Show show = new Show();
        show.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "image/png", new byte[0]);
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/shows/image/{id}", show.getId()).file(newImage);
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(showBO.findOne(show.getId())).willReturn(show);
        given(showBO.save(any(Show.class))).willAnswer(invocation -> invocation.getArgument(0));
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateImageByIdBadRequestFileTooLarge() throws Exception {
        Show show = new Show();
        show.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "image/png", new byte[100000000]);
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/shows/image/{id}", show.getId()).file(newImage);
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(showBO.findOne(show.getId())).willReturn(show);
        given(showBO.save(any(Show.class))).willAnswer(invocation -> invocation.getArgument(0));
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateImageByIdNotFound() throws Exception {
        Show show = new Show();
        show.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "image/png", "HAJAJASJJASJDF".getBytes());
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/shows/image/{id}", show.getId()).file(newImage);
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(showBO.findOne(show.getId())).willReturn(null);
        given(showBO.save(any(Show.class))).willAnswer(invocation -> invocation.getArgument(0));
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteOk() throws Exception {
        given(showBO.exists(1L)).willReturn(true);
        mockMvc.perform(delete("/api/shows/{id}", 1L))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteNotFound() throws Exception {
        given(showBO.exists(1L)).willReturn(false);
        mockMvc.perform(delete("/api/shows/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteForbiddenNotAdmin() throws Exception {
        given(showBO.exists(1L)).willReturn(true);
        mockMvc.perform(delete("/api/shows/{id}", 1L))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void deleteForbiddenNotLoggedIn() throws Exception {
        given(showBO.exists(2L)).willReturn(true);
        mockMvc.perform(delete("/api/shows/{id}", 2L))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
}
