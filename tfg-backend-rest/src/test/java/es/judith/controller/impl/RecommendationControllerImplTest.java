package es.judith.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.judith.bo.*;
import es.judith.config.JwtFilter;
import es.judith.config.SecurityConfig;
import es.judith.domain.Recommendation;
import es.judith.domain.Show;
import es.judith.domain.user.User;
import es.judith.dto.recommendation.RecommendationInputDTO;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = RecommendationControllerImpl.class)
@WebMvcTest(RecommendationControllerImpl.class)
@Import({
        SecurityConfig.class,
        JwtFilter.class,
        RecommendationControllerImplTest.SecurityBeans.class
})
class RecommendationControllerImplTest {

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
    private RecommendationBO recommendationBO;
    @MockitoBean
    private AuthBO authBO;
    @MockitoBean
    private UserBO userBO;
    @MockitoBean
    private FriendBO friendBO;
    @MockitoBean
    private ShowBO showBO;

    @Test
    @WithMockUser(authorities = "USER")
    void findAllSentRecommendationsOk() throws Exception {
        User user = new User();
        user.setId(1L);
        Map<Long, ArrayList> recommendationList = new HashMap<>();
        given(authBO.getCurrentUser()).willReturn(user);
        given(recommendationBO.getAllSentRecommendations(user.getId())).willReturn(recommendationList);
        mockMvc.perform(get("/api/recommendations/sent"))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void findAllSentRecommendationsForbidden() throws Exception {
        User user = new User();
        user.setId(1L);
        Map<Long, ArrayList> recommendationList = new HashMap<>();
        given(authBO.getCurrentUser()).willReturn(user);
        given(recommendationBO.getAllSentRecommendations(user.getId())).willReturn(recommendationList);
        mockMvc.perform(get("/api/recommendations/sent"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void findAllReceivedRecommendationsOk() throws Exception {
        User user = new User();
        user.setId(1L);
        Map<Long, ArrayList> recommendationList = new HashMap<>();
        given(authBO.getCurrentUser()).willReturn(user);
        given(recommendationBO.getAllReceivedRecommendations(user.getId())).willReturn(recommendationList);
        mockMvc.perform(get("/api/recommendations/received"))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void findAllReceivedRecommendationsForbidden() throws Exception {
        User user = new User();
        user.setId(1L);
        Map<Long, ArrayList> recommendationList = new HashMap<>();
        given(authBO.getCurrentUser()).willReturn(user);
        given(recommendationBO.getAllReceivedRecommendations(user.getId())).willReturn(recommendationList);
        mockMvc.perform(get("/api/recommendations/received"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void sendRecommendationOk() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Show show = new Show();
        show.setId(1L);
        RecommendationInputDTO recommendationInputDTO = new RecommendationInputDTO();
        recommendationInputDTO.setShowId(show.getId());
        recommendationInputDTO.setUserSenderId(user.getId());
        recommendationInputDTO.setUserReceiverId(user2.getId());
        given(showBO.findOne(show.getId())).willReturn(show);
        given(userBO.findOne(user2.getId())).willReturn(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        given(recommendationBO.checkIfRecommended(user.getId(), user2.getId(), show.getId())).willReturn(false);
        given(friendBO.checkIfFriend(user.getId(), user2.getId())).willReturn(true);
        mockMvc.perform(post("/api/recommendations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recommendationInputDTO)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void sendRecommendationForbiddenNotLoggedIn() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Show show = new Show();
        show.setId(1L);
        RecommendationInputDTO recommendationInputDTO = new RecommendationInputDTO();
        recommendationInputDTO.setShowId(show.getId());
        recommendationInputDTO.setUserSenderId(user.getId());
        recommendationInputDTO.setUserReceiverId(user2.getId());
        given(showBO.findOne(show.getId())).willReturn(show);
        given(userBO.findOne(user2.getId())).willReturn(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        given(recommendationBO.checkIfRecommended(user.getId(), user2.getId(), show.getId())).willReturn(false);
        given(friendBO.checkIfFriend(user.getId(), user2.getId())).willReturn(true);
        mockMvc.perform(post("/api/recommendations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recommendationInputDTO)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void sendRecommendationForbiddenReceiverIsNotFriend() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Show show = new Show();
        show.setId(1L);
        RecommendationInputDTO recommendationInputDTO = new RecommendationInputDTO();
        recommendationInputDTO.setShowId(show.getId());
        recommendationInputDTO.setUserSenderId(user.getId());
        recommendationInputDTO.setUserReceiverId(user2.getId());
        given(showBO.findOne(show.getId())).willReturn(show);
        given(userBO.findOne(user2.getId())).willReturn(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        given(recommendationBO.checkIfRecommended(user.getId(), user2.getId(), show.getId())).willReturn(false);
        given(friendBO.checkIfFriend(user.getId(), user2.getId())).willReturn(false);
        mockMvc.perform(post("/api/recommendations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recommendationInputDTO)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void sendRecommendationNotFoundUserDoesNotExist() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Show show = new Show();
        show.setId(1L);
        RecommendationInputDTO recommendationInputDTO = new RecommendationInputDTO();
        recommendationInputDTO.setShowId(show.getId());
        recommendationInputDTO.setUserSenderId(user.getId());
        recommendationInputDTO.setUserReceiverId(user2.getId());
        given(showBO.findOne(show.getId())).willReturn(show);
        given(userBO.findOne(user2.getId())).willReturn(null);
        given(authBO.getCurrentUser()).willReturn(user);
        given(recommendationBO.checkIfRecommended(user.getId(), user2.getId(), show.getId())).willReturn(false);
        given(friendBO.checkIfFriend(user.getId(), user2.getId())).willReturn(true);
        mockMvc.perform(post("/api/recommendations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recommendationInputDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void sendRecommendationNotFoundShowDoesNotExist() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Show show = new Show();
        show.setId(1L);
        RecommendationInputDTO recommendationInputDTO = new RecommendationInputDTO();
        recommendationInputDTO.setShowId(show.getId());
        recommendationInputDTO.setUserSenderId(user.getId());
        recommendationInputDTO.setUserReceiverId(user2.getId());
        given(showBO.findOne(show.getId())).willReturn(null);
        given(userBO.findOne(user2.getId())).willReturn(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        given(recommendationBO.checkIfRecommended(user.getId(), user2.getId(), show.getId())).willReturn(false);
        given(friendBO.checkIfFriend(user.getId(), user2.getId())).willReturn(true);
        mockMvc.perform(post("/api/recommendations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recommendationInputDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void sendRecommendationConflict() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Show show = new Show();
        show.setId(1L);
        RecommendationInputDTO recommendationInputDTO = new RecommendationInputDTO();
        recommendationInputDTO.setShowId(show.getId());
        recommendationInputDTO.setUserSenderId(user.getId());
        recommendationInputDTO.setUserReceiverId(user2.getId());
        given(showBO.findOne(show.getId())).willReturn(show);
        given(userBO.findOne(user2.getId())).willReturn(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        given(recommendationBO.checkIfRecommended(user.getId(), user2.getId(), show.getId())).willReturn(true);
        given(friendBO.checkIfFriend(user.getId(), user2.getId())).willReturn(true);
        mockMvc.perform(post("/api/recommendations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recommendationInputDTO)))
                .andExpect(status().isConflict())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void sendRecommendationBadRequest() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Show show = new Show();
        show.setId(1L);
        RecommendationInputDTO recommendationInputDTO = new RecommendationInputDTO();
        given(showBO.findOne(show.getId())).willReturn(show);
        given(userBO.findOne(user2.getId())).willReturn(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        given(recommendationBO.checkIfRecommended(user.getId(), user2.getId(), show.getId())).willReturn(false);
        given(friendBO.checkIfFriend(user.getId(), user2.getId())).willReturn(true);
        mockMvc.perform(post("/api/recommendations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recommendationInputDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteOk() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Show show = new Show();
        show.setId(1L);
        Recommendation recommendation = new Recommendation();
        recommendation.setShow(show);
        recommendation.setUserSender(user);
        recommendation.setUserReceiver(user2);
        recommendation.setId(1L);
        given(authBO.getCurrentUser()).willReturn(user);
        given(recommendationBO.findOne(recommendation.getId())).willReturn(recommendation);
        mockMvc.perform(delete("/api/recommendations/{id}", recommendation.getId()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void deleteForbiddenNotLoggedIn() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Show show = new Show();
        show.setId(1L);
        Recommendation recommendation = new Recommendation();
        recommendation.setShow(show);
        recommendation.setUserSender(user);
        recommendation.setUserReceiver(user2);
        recommendation.setId(1L);
        given(authBO.getCurrentUser()).willReturn(user);
        given(recommendationBO.findOne(recommendation.getId())).willReturn(recommendation);
        mockMvc.perform(delete("/api/recommendations/{id}", recommendation.getId()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteForbiddenUserIsNeitherSenderOrReceiver() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Show show = new Show();
        show.setId(1L);
        User user3 = new User();
        user3.setId(3L);
        Recommendation recommendation = new Recommendation();
        recommendation.setShow(show);
        recommendation.setUserSender(user3);
        recommendation.setUserReceiver(user2);
        recommendation.setId(1L);
        given(authBO.getCurrentUser()).willReturn(user);
        given(recommendationBO.findOne(recommendation.getId())).willReturn(recommendation);
        mockMvc.perform(delete("/api/recommendations/{id}", recommendation.getId()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteNotFound() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Show show = new Show();
        show.setId(1L);
        Recommendation recommendation = new Recommendation();
        recommendation.setShow(show);
        recommendation.setUserSender(user);
        recommendation.setUserReceiver(user2);
        recommendation.setId(1L);
        given(authBO.getCurrentUser()).willReturn(user);
        given(recommendationBO.findOne(recommendation.getId())).willReturn(null);
        mockMvc.perform(delete("/api/recommendations/{id}", recommendation.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
