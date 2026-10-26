package es.judith.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.judith.bo.CharacterBO;
import es.judith.bo.EpisodeBO;
import es.judith.bo.JwtBO;
import es.judith.bo.SeasonBO;
import es.judith.config.JwtFilter;
import es.judith.config.SecurityConfig;
import es.judith.domain.Character;
import es.judith.domain.Episode;
import es.judith.domain.Season;
import es.judith.dto.episode.EpisodeInputDTO;
import es.judith.dto.episode.EpisodeNoSeasonDTO;
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
@ContextConfiguration(classes = EpisodeControllerImpl.class)
@WebMvcTest(EpisodeControllerImpl.class)
@Import({
        SecurityConfig.class,
        JwtFilter.class,
        EpisodeControllerImplTest.SecurityBeans.class
})
class EpisodeControllerImplTest {

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
    private EpisodeBO episodeBO;
    @MockitoBean
    private CharacterBO characterBO;
    @MockitoBean
    private SeasonBO seasonBO;

    @Test
    void findAllOk() throws Exception {
        mockMvc.perform(get("/api/episodes"))
                .andExpect(status().isOk());
    }
    @Test
    void findByIdOk() throws Exception {
        EpisodeNoSeasonDTO episodeDTO = new EpisodeNoSeasonDTO();
        episodeDTO.setId(1L);
        episodeDTO.setCharacters(List.of());
        Episode episode = episodeDTO.obtainDomainObject();
        episode.setSeason(new Season());
        given(episodeBO.findOneWithCharacters(1L)).willReturn(episode);
        given(episodeBO.exists(1L)).willReturn(true);
        given(episodeBO.convertToDTO(episode)).willReturn(episodeDTO);
        mockMvc.perform(get("/api/episodes/{id}", episodeDTO.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(episodeDTO)));
    }
    @Test
    void findByIdNotFound() throws Exception {
        EpisodeNoSeasonDTO episodeDTO = new EpisodeNoSeasonDTO();
        episodeDTO.setId(1L);
        episodeDTO.setCharacters(List.of());
        Episode episode = episodeDTO.obtainDomainObject();
        episode.setSeason(new Season());
        given(episodeBO.findOneWithCharacters(1L)).willReturn(episode);
        given(episodeBO.exists(1L)).willReturn(false);
        given(episodeBO.convertToDTO(episode)).willReturn(episodeDTO);
        mockMvc.perform(get("/api/episodes/{id}", episodeDTO.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addOk() throws Exception {
        EpisodeInputDTO episodeDTO = new EpisodeInputDTO();
        episodeDTO.setCharacterIds(List.of(1L));
        episodeDTO.setEpisodeNum(1);
        episodeDTO.setSeasonId(1L);
        episodeDTO.setSummary("AAAAAAAAAAAAAAAAAAAAAAA");
        episodeDTO.setTitle("judy haciendo el tfg (disfrutable, mi actividad favorita)");
        Season season = new Season();
        season.setId(1L);
        Character character = new Character();
        character.setId(1L);
        given(episodeBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(seasonBO.findOne(episodeDTO.getSeasonId())).willReturn(season);
        given(characterBO.findAllById(episodeDTO.getCharacterIds())).willReturn(List.of(character));
        mockMvc.perform(post("/api/episodes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(episodeDTO)))
                .andExpect(status().isCreated())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void addForbiddenNotAdmin() throws Exception {
        EpisodeInputDTO episodeDTO = new EpisodeInputDTO();
        episodeDTO.setCharacterIds(List.of(1L));
        episodeDTO.setEpisodeNum(1);
        episodeDTO.setSeasonId(1L);
        episodeDTO.setSummary("AAAAAAAAAAAAAAAAAAAAAAA");
        episodeDTO.setTitle("judy haciendo el tfg (disfrutable, mi actividad favorita)");
        Season season = new Season();
        season.setId(1L);
        Character character = new Character();
        character.setId(1L);
        given(episodeBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(seasonBO.findOne(episodeDTO.getSeasonId())).willReturn(season);
        given(characterBO.findAllById(episodeDTO.getCharacterIds())).willReturn(List.of(character));
        mockMvc.perform(post("/api/episodes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(episodeDTO)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void addForbiddenNotLoggedIn() throws Exception {
        EpisodeInputDTO episodeDTO = new EpisodeInputDTO();
        episodeDTO.setCharacterIds(List.of(1L));
        episodeDTO.setEpisodeNum(1);
        episodeDTO.setSeasonId(1L);
        episodeDTO.setSummary("AAAAAAAAAAAAAAAAAAAAAAAA");
        episodeDTO.setTitle("judy haciendo el tfg (disfrutable, mi actividad favorita)");
        Season season = new Season();
        season.setId(1L);
        Character character = new Character();
        character.setId(1L);
        given(episodeBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(seasonBO.findOne(episodeDTO.getSeasonId())).willReturn(season);
        given(characterBO.findAllById(episodeDTO.getCharacterIds())).willReturn(List.of(character));
        mockMvc.perform(post("/api/episodes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(episodeDTO)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addBadRequestFieldsNotPresent() throws Exception {
        EpisodeInputDTO episodeDTO = new EpisodeInputDTO();
        Season season = new Season();
        season.setId(1L);
        Character character = new Character();
        character.setId(1L);
        given(episodeBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(seasonBO.findOne(episodeDTO.getSeasonId())).willReturn(season);
        given(characterBO.findAllById(episodeDTO.getCharacterIds())).willReturn(List.of(character));
        mockMvc.perform(post("/api/episodes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(episodeDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addBadRequestIllegalString() throws Exception {
        EpisodeInputDTO episodeDTO = new EpisodeInputDTO();
        episodeDTO.setCharacterIds(List.of(1L));
        episodeDTO.setEpisodeNum(1);
        episodeDTO.setSeasonId(1L);
        episodeDTO.setSummary("AAAAAAAAAAAAAAAAAAAAAAA/////");
        episodeDTO.setTitle("judy haciendo el tfg (disfrutable, mi actividad favorita)");
        Season season = new Season();
        season.setId(1L);
        Character character = new Character();
        character.setId(1L);
        given(episodeBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(true);
        given(seasonBO.findOne(episodeDTO.getSeasonId())).willReturn(season);
        given(characterBO.findAllById(episodeDTO.getCharacterIds())).willReturn(List.of(character));
        mockMvc.perform(post("/api/episodes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(episodeDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addNotFoundSeasonDoesNotExist() throws Exception {
        EpisodeInputDTO episodeDTO = new EpisodeInputDTO();
        episodeDTO.setCharacterIds(List.of(1L));
        episodeDTO.setEpisodeNum(1);
        episodeDTO.setSeasonId(1L);
        episodeDTO.setSummary("AAAAAAAAAAAAAAAAAAAAAAA");
        episodeDTO.setTitle("judy haciendo el tfg (disfrutable, mi actividad favorita)");
        Season season = new Season();
        season.setId(1L);
        Character character = new Character();
        character.setId(1L);
        given(episodeBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(seasonBO.findOne(episodeDTO.getSeasonId())).willReturn(null);
        given(characterBO.findAllById(episodeDTO.getCharacterIds())).willReturn(List.of(character));
        mockMvc.perform(post("/api/episodes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(episodeDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addNotFoundCharactersDoNotExist() throws Exception {
        EpisodeInputDTO episodeDTO = new EpisodeInputDTO();
        episodeDTO.setCharacterIds(List.of(1L));
        episodeDTO.setEpisodeNum(1);
        episodeDTO.setSeasonId(1L);
        episodeDTO.setSummary("AAAAAAAAAAAAAAAAAAAAAAA");
        episodeDTO.setTitle("judy haciendo el tfg (disfrutable, mi actividad favorita)");
        Season season = new Season();
        season.setId(1L);
        Character character = new Character();
        character.setId(1L);
        given(episodeBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(seasonBO.findOne(episodeDTO.getSeasonId())).willReturn(season);
        given(characterBO.findAllById(episodeDTO.getCharacterIds())).willReturn(List.of());
        mockMvc.perform(post("/api/episodes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(episodeDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateOk() throws Exception {
        Season season = new Season();
        Character character = new Character();
        Episode episode = new Episode();
        episode.setSeason(season);
        episode.setCharacters(List.of(character));
        episode.setId(1L);
        episode.setTitle("title");
        episode.setSummary("summary");
        episode.setEpisodeNum(1);
        EpisodeInputDTO newEpisodeInfo = new EpisodeInputDTO();
        newEpisodeInfo.setSummary("SUMMMMMMMMMMMMMMMMMM");
        given(episodeBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(episodeBO.findOne(episode.getId())).willReturn(episode);
        mockMvc.perform(patch("/api/episodes/{id}", episode.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEpisodeInfo)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateForbiddenNotAdmin() throws Exception {
        Season season = new Season();
        Character character = new Character();
        Episode episode = new Episode();
        episode.setSeason(season);
        episode.setCharacters(List.of(character));
        episode.setId(1L);
        episode.setTitle("title");
        episode.setSummary("summary");
        episode.setEpisodeNum(1);
        EpisodeInputDTO newEpisodeInfo = new EpisodeInputDTO();
        newEpisodeInfo.setSummary("SUMMMMMMMMMMMMMMMMMM");
        given(episodeBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(episodeBO.findOne(episode.getId())).willReturn(episode);
        mockMvc.perform(patch("/api/episodes/{id}", episode.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEpisodeInfo)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void updateForbiddenNotLoggedIn() throws Exception {
        Season season = new Season();
        Character character = new Character();
        Episode episode = new Episode();
        episode.setSeason(season);
        episode.setCharacters(List.of(character));
        episode.setId(1L);
        episode.setTitle("title");
        episode.setSummary("summary");
        episode.setEpisodeNum(1);
        EpisodeInputDTO newEpisodeInfo = new EpisodeInputDTO();
        newEpisodeInfo.setSummary("SUMMMMMMMMMMMMMMMMMMM");
        given(episodeBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(episodeBO.findOne(episode.getId())).willReturn(episode);
        mockMvc.perform(patch("/api/episodes/{id}", episode.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEpisodeInfo)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateBadRequestIllegalString() throws Exception {
        Season season = new Season();
        Character character = new Character();
        Episode episode = new Episode();
        episode.setSeason(season);
        episode.setCharacters(List.of(character));
        episode.setId(1L);
        episode.setTitle("title");
        episode.setSummary("summary");
        episode.setEpisodeNum(1);
        EpisodeInputDTO newEpisodeInfo = new EpisodeInputDTO();
        newEpisodeInfo.setSummary("SUMMMMMMMMMMMMMMMMMM////");
        given(episodeBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(true);
        given(episodeBO.findOne(episode.getId())).willReturn(episode);
        mockMvc.perform(patch("/api/episodes/{id}", episode.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEpisodeInfo)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateNotFound() throws Exception {
        Season season = new Season();
        Character character = new Character();
        Episode episode = new Episode();
        episode.setSeason(season);
        episode.setCharacters(List.of(character));
        episode.setId(1L);
        episode.setTitle("title");
        episode.setSummary("summary");
        episode.setEpisodeNum(1);
        EpisodeInputDTO newEpisodeInfo = new EpisodeInputDTO();
        newEpisodeInfo.setSummary("SUMMMMMMMMMMMMMMMMMM");
        given(episodeBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(episodeBO.findOne(episode.getId())).willReturn(null);
        mockMvc.perform(patch("/api/episodes/{id}", episode.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEpisodeInfo)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteOk() throws Exception {
        given(episodeBO.exists(1L)).willReturn(true);
        mockMvc.perform(delete("/api/episodes/{id}", 1L))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteForbiddenNotAdmin() throws Exception {
        given(episodeBO.exists(1L)).willReturn(true);
        mockMvc.perform(delete("/api/episodes/{id}", 1L))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void deleteNotLoggedIn() throws Exception {
        given(episodeBO.exists(2L)).willReturn(true);
        mockMvc.perform(delete("/api/episodes/{id}", 2L))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteNotFound() throws Exception {
        given(episodeBO.exists(1L)).willReturn(false);
        mockMvc.perform(delete("/api/episodes/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
