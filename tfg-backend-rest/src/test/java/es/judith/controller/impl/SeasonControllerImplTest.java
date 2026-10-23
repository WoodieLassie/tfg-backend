package es.judith.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.judith.bo.JwtBO;
import es.judith.bo.SeasonBO;
import es.judith.bo.ShowBO;
import es.judith.config.JwtFilter;
import es.judith.config.SecurityConfig;
import es.judith.domain.Season;
import es.judith.domain.Show;
import es.judith.dto.season.SeasonInputDTO;
import es.judith.security.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = SeasonControllerImpl.class)
@WebMvcTest(SeasonControllerImpl.class)
@Import({
        SecurityConfig.class,
        JwtFilter.class,
        SeasonControllerImplTest.SecurityBeans.class

})
class SeasonControllerImplTest {

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
    private SeasonBO seasonBO;
    @MockitoBean
    private ShowBO showBO;

    @Test
    void findByIdOk() throws Exception {
        Season season = new Season();
        season.setId(1L);
        given(seasonBO.findOne(season.getId())).willReturn(season);
        mockMvc.perform(get("/api/seasons/{id}", season.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(season)));
    }
    @Test
    void findByIdNotFound() throws Exception {
        Season season = new Season();
        season.setId(1L);
        given(seasonBO.findOne(season.getId())).willReturn(null);
        mockMvc.perform(get("/api/seasons/{id}", season.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addOk() throws Exception {
        SeasonInputDTO seasonDTO = new SeasonInputDTO();
        seasonDTO.setDescription("desc");
        seasonDTO.setSeasonNum(1);
        seasonDTO.setShowId(1L);
        Show show = new Show();
        show.setId(1L);
        Season season = seasonDTO.obtainDomainObject();
        given(showBO.findOne(show.getId())).willReturn(show);
        given(seasonBO.existsBySeasonNumAndShowId(season.getSeasonNum(), show.getId())).willReturn(null);
        given(seasonBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        mockMvc.perform(post("/api/seasons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seasonDTO)))
                .andExpect(status().isCreated())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void addForbiddenNotAdmin() throws Exception {
        SeasonInputDTO seasonDTO = new SeasonInputDTO();
        seasonDTO.setDescription("desc");
        seasonDTO.setSeasonNum(1);
        seasonDTO.setShowId(1L);
        Show show = new Show();
        show.setId(1L);
        Season season = seasonDTO.obtainDomainObject();
        given(showBO.findOne(show.getId())).willReturn(show);
        given(seasonBO.existsBySeasonNumAndShowId(season.getSeasonNum(), show.getId())).willReturn(null);
        given(seasonBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        mockMvc.perform(post("/api/seasons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seasonDTO)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void addForbiddenNotLoggedIn() throws Exception {
        SeasonInputDTO seasonDTO = new SeasonInputDTO();
        seasonDTO.setDescription("c");
        seasonDTO.setSeasonNum(1);
        seasonDTO.setShowId(1L);
        Show show = new Show();
        show.setId(1L);
        Season season = seasonDTO.obtainDomainObject();
        given(showBO.findOne(show.getId())).willReturn(show);
        given(seasonBO.existsBySeasonNumAndShowId(season.getSeasonNum(), show.getId())).willReturn(false);
        given(seasonBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        mockMvc.perform(post("/api/seasons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seasonDTO)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addConflict() throws Exception {
        SeasonInputDTO seasonDTO = new SeasonInputDTO();
        seasonDTO.setDescription("desc");
        seasonDTO.setSeasonNum(1);
        seasonDTO.setShowId(1L);
        Show show = new Show();
        show.setId(1L);
        Season season = seasonDTO.obtainDomainObject();
        given(showBO.findOne(show.getId())).willReturn(show);
        given(seasonBO.existsBySeasonNumAndShowId(season.getSeasonNum(), show.getId())).willReturn(true);
        given(seasonBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        mockMvc.perform(post("/api/seasons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seasonDTO)))
                .andExpect(status().isConflict())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addBadRequestFieldsNotPresent() throws Exception {
        SeasonInputDTO seasonDTO = new SeasonInputDTO();
        Show show = new Show();
        show.setId(1L);
        Season season = seasonDTO.obtainDomainObject();
        given(showBO.findOne(show.getId())).willReturn(show);
        given(seasonBO.existsBySeasonNumAndShowId(season.getSeasonNum(), show.getId())).willReturn(false);
        given(seasonBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        mockMvc.perform(post("/api/seasons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seasonDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addBadRequestIllegalString() throws Exception {
        SeasonInputDTO seasonDTO = new SeasonInputDTO();
        seasonDTO.setDescription("desc////");
        seasonDTO.setSeasonNum(1);
        seasonDTO.setShowId(1L);
        Show show = new Show();
        show.setId(1L);
        Season season = seasonDTO.obtainDomainObject();
        given(showBO.findOne(show.getId())).willReturn(show);
        given(seasonBO.existsBySeasonNumAndShowId(season.getSeasonNum(), show.getId())).willReturn(false);
        given(seasonBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(true);
        mockMvc.perform(post("/api/seasons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seasonDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addNotFound() throws Exception {
        SeasonInputDTO seasonDTO = new SeasonInputDTO();
        seasonDTO.setDescription("desc");
        seasonDTO.setSeasonNum(1);
        seasonDTO.setShowId(1L);
        Show show = new Show();
        show.setId(1L);
        Season season = seasonDTO.obtainDomainObject();
        given(showBO.findOne(show.getId())).willReturn(null);
        given(seasonBO.existsBySeasonNumAndShowId(season.getSeasonNum(), show.getId())).willReturn(false);
        given(seasonBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        mockMvc.perform(post("/api/seasons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seasonDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateOk() throws Exception {
        Season season = new Season();
        season.setId(1L);
        season.setDescription("desc");
        Show show = new Show();
        show.setId(1L);
        season.setShow(show);
        season.setSeasonNum(1);
        season.setEpisodes(List.of());
        SeasonInputDTO seasonInputDTO = new SeasonInputDTO();
        seasonInputDTO.setDescription("desc");
        given(seasonBO.findOne(season.getId())).willReturn(season);
        given(showBO.findOne(show.getId())).willReturn(show);
        given(seasonBO.existsBySeasonNumAndShowId(seasonInputDTO.getSeasonNum(), show.getId())).willReturn(false);
        given(seasonBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        mockMvc.perform(patch("/api/seasons/{id}", season.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seasonInputDTO)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateForbiddenNotAdmin() throws Exception {
        Season season = new Season();
        season.setId(1L);
        season.setDescription("desc");
        Show show = new Show();
        show.setId(1L);
        season.setShow(show);
        season.setSeasonNum(1);
        season.setEpisodes(List.of());
        SeasonInputDTO seasonInputDTO = new SeasonInputDTO();
        seasonInputDTO.setDescription("desc");
        given(seasonBO.findOne(season.getId())).willReturn(season);
        given(showBO.findOne(show.getId())).willReturn(show);
        given(seasonBO.existsBySeasonNumAndShowId(seasonInputDTO.getSeasonNum(), show.getId())).willReturn(false);
        given(seasonBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        mockMvc.perform(patch("/api/seasons/{id}", season.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seasonInputDTO)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void updateForbiddenNotLoggedIn() throws Exception {
        Season season = new Season();
        season.setId(1L);
        season.setDescription("desc");
        Show show = new Show();
        show.setId(1L);
        season.setShow(show);
        season.setSeasonNum(1);
        season.setEpisodes(List.of());
        SeasonInputDTO seasonInputDTO = new SeasonInputDTO();
        seasonInputDTO.setDescription("descc");
        given(seasonBO.findOne(season.getId())).willReturn(season);
        given(showBO.findOne(show.getId())).willReturn(show);
        given(seasonBO.existsBySeasonNumAndShowId(seasonInputDTO.getSeasonNum(), show.getId())).willReturn(false);
        given(seasonBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        mockMvc.perform(patch("/api/seasons/{id}", season.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seasonInputDTO)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateConflict() throws Exception {
        Season season = new Season();
        season.setId(1L);
        season.setDescription("desc");
        Show show = new Show();
        show.setId(1L);
        season.setShow(show);
        season.setSeasonNum(1);
        season.setEpisodes(List.of());
        SeasonInputDTO seasonInputDTO = new SeasonInputDTO();
        seasonInputDTO.setDescription("desc");
        seasonInputDTO.setSeasonNum(2);
        given(seasonBO.findOne(season.getId())).willReturn(season);
        given(showBO.findOne(show.getId())).willReturn(show);
        given(seasonBO.existsBySeasonNumAndShowId(seasonInputDTO.getSeasonNum(), show.getId())).willReturn(true);
        given(seasonBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        mockMvc.perform(patch("/api/seasons/{id}", season.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seasonInputDTO)))
                .andExpect(status().isConflict())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateBadRequestIllegalString() throws Exception {
        Season season = new Season();
        season.setId(1L);
        season.setDescription("desc");
        Show show = new Show();
        show.setId(1L);
        season.setShow(show);
        season.setSeasonNum(1);
        season.setEpisodes(List.of());
        SeasonInputDTO seasonInputDTO = new SeasonInputDTO();
        seasonInputDTO.setDescription("desc///");
        given(seasonBO.findOne(season.getId())).willReturn(season);
        given(showBO.findOne(show.getId())).willReturn(show);
        given(seasonBO.existsBySeasonNumAndShowId(seasonInputDTO.getSeasonNum(), show.getId())).willReturn(false);
        given(seasonBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(true);
        mockMvc.perform(patch("/api/seasons/{id}", season.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seasonInputDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateNotFoundShowDoesNotExist() throws Exception {
        Season season = new Season();
        season.setId(1L);
        season.setDescription("desc");
        Show show = new Show();
        show.setId(1L);
        season.setShow(show);
        season.setSeasonNum(1);
        season.setEpisodes(List.of());
        SeasonInputDTO seasonInputDTO = new SeasonInputDTO();
        seasonInputDTO.setDescription("desc");
        seasonInputDTO.setShowId(1L);
        given(seasonBO.findOne(season.getId())).willReturn(season);
        given(showBO.findOne(show.getId())).willReturn(null);
        given(seasonBO.existsBySeasonNumAndShowId(seasonInputDTO.getSeasonNum(), show.getId())).willReturn(false);
        given(seasonBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        mockMvc.perform(patch("/api/seasons/{id}", season.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seasonInputDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateNotFoundSeasonDoesNotExist() throws Exception {
        Season season = new Season();
        season.setId(1L);
        season.setDescription("desc");
        Show show = new Show();
        show.setId(1L);
        season.setShow(show);
        season.setSeasonNum(1);
        season.setEpisodes(List.of());
        SeasonInputDTO seasonInputDTO = new SeasonInputDTO();
        seasonInputDTO.setDescription("desc");
        given(seasonBO.findOne(season.getId())).willReturn(null);
        given(showBO.findOne(show.getId())).willReturn(show);
        given(seasonBO.existsBySeasonNumAndShowId(seasonInputDTO.getSeasonNum(), show.getId())).willReturn(false);
        given(seasonBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        mockMvc.perform(patch("/api/seasons/{id}", season.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seasonInputDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteOk() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteForbiddenNotAdmin() throws Exception {

    }
    @Test
    @WithAnonymousUser
    void deleteForbiddenNotLoggedIn() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteNotFound() throws Exception {

    }
}
