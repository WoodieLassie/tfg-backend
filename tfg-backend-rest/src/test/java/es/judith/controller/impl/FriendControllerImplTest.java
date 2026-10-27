package es.judith.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.judith.bo.AuthBO;
import es.judith.bo.FriendBO;
import es.judith.bo.JwtBO;
import es.judith.bo.UserBO;
import es.judith.config.JwtFilter;
import es.judith.config.SecurityConfig;
import es.judith.domain.Friend;
import es.judith.domain.user.User;
import es.judith.dto.friend.FriendInputDTO;
import es.judith.dto.user.UserProfileDTO;
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

import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = FriendControllerImpl.class)
@WebMvcTest(FriendControllerImpl.class)
@Import({
        SecurityConfig.class,
        JwtFilter.class,
        FriendControllerImplTest.SecurityBeans.class
})
public class FriendControllerImplTest {

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
    private FriendBO friendBO;
    @MockitoBean
    private UserBO userBO;
    @MockitoBean
    private AuthBO authBO;

    @Test
    @WithMockUser(authorities = "USER")
    void findAllFriendsOk() throws Exception {
        User user = new User();
        user.setId(1L);
        Map<Long, UserProfileDTO> userFriendList = new HashMap<>();
        given(authBO.getCurrentUser()).willReturn(user);
        given(friendBO.getAllFriends(user.getId())).willReturn(userFriendList);
        mockMvc.perform(get("/api/friends"))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void findAllFriendsForbidden() throws Exception {
        User user = new User();
        user.setId(1L);
        Map<Long, UserProfileDTO> userFriendList = new HashMap<>();
        given(authBO.getCurrentUser()).willReturn(user);
        given(friendBO.getAllFriends(user.getId())).willReturn(userFriendList);
        mockMvc.perform(get("/api/friends"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void findAllSentRequestsOk() throws Exception {
        User user = new User();
        user.setId(1L);
        Map<Long, UserProfileDTO> userFriendList = new HashMap<>();
        given(authBO.getCurrentUser()).willReturn(user);
        given(friendBO.getAllSentRequests(user.getId())).willReturn(userFriendList);
        mockMvc.perform(get("/api/friends/requests"))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void findAllSentRequestsForbidden() throws Exception {
        User user = new User();
        user.setId(1L);
        Map<Long, UserProfileDTO> userFriendList = new HashMap<>();
        given(authBO.getCurrentUser()).willReturn(user);
        given(friendBO.getAllSentRequests(user.getId())).willReturn(userFriendList);
        mockMvc.perform(get("/api/friends/requests"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void findAllReceivedRequestsOk() throws Exception {
        User user = new User();
        user.setId(1L);
        Map<Long, UserProfileDTO> userFriendList = new HashMap<>();
        given(authBO.getCurrentUser()).willReturn(user);
        given(friendBO.getAllReceivedRequests(user.getId())).willReturn(userFriendList);
        mockMvc.perform(get("/api/friends/requested"))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void findAllReceivedRequestsForbidden() throws Exception {
        User user = new User();
        user.setId(1L);
        Map<Long, UserProfileDTO> userFriendList = new HashMap<>();
        given(authBO.getCurrentUser()).willReturn(user);
        given(friendBO.getAllReceivedRequests(user.getId())).willReturn(userFriendList);
        mockMvc.perform(get("/api/friends/requested"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void sendRequestOk() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        FriendInputDTO friendInputDTO = new FriendInputDTO();
        friendInputDTO.setRequestStatus(false);
        friendInputDTO.setUserSenderId(user.getId());
        friendInputDTO.setUserReceiverId(user2.getId());
        given(userBO.findOne(user2.getId())).willReturn(user2);
        given(friendBO.checkIfRelated(user.getId(), user2.getId())).willReturn(false);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(post("/api/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendInputDTO)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void sendRequestForbidden() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        FriendInputDTO friendInputDTO = new FriendInputDTO();
        friendInputDTO.setRequestStatus(false);
        friendInputDTO.setUserSenderId(user.getId());
        friendInputDTO.setUserReceiverId(user2.getId());
        given(userBO.findOne(user2.getId())).willReturn(user2);
        given(friendBO.checkIfRelated(user.getId(), user2.getId())).willReturn(false);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(post("/api/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendInputDTO)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void sendRequestConflict() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        FriendInputDTO friendInputDTO = new FriendInputDTO();
        friendInputDTO.setRequestStatus(false);
        friendInputDTO.setUserSenderId(user.getId());
        friendInputDTO.setUserReceiverId(user2.getId());
        given(userBO.findOne(user2.getId())).willReturn(user2);
        given(friendBO.checkIfRelated(user.getId(), user2.getId())).willReturn(true);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(post("/api/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendInputDTO)))
                .andExpect(status().isConflict())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void sendRequestNotFound() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        FriendInputDTO friendInputDTO = new FriendInputDTO();
        friendInputDTO.setRequestStatus(false);
        friendInputDTO.setUserSenderId(user.getId());
        friendInputDTO.setUserReceiverId(user2.getId());
        given(userBO.findOne(user2.getId())).willReturn(null);
        given(friendBO.checkIfRelated(user.getId(), user2.getId())).willReturn(false);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(post("/api/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendInputDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void sendRequestBadRequestFieldsNotPresent() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        FriendInputDTO friendInputDTO = new FriendInputDTO();
        friendInputDTO.setRequestStatus(false);
        given(userBO.findOne(user2.getId())).willReturn(user2);
        given(friendBO.checkIfRelated(user.getId(), user2.getId())).willReturn(false);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(post("/api/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendInputDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void sendRequestBadRequestReceiverIsCurrentUser() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        FriendInputDTO friendInputDTO = new FriendInputDTO();
        friendInputDTO.setRequestStatus(false);
        friendInputDTO.setUserSenderId(user.getId());
        friendInputDTO.setUserReceiverId(user.getId());
        given(userBO.findOne(user2.getId())).willReturn(user2);
        given(friendBO.checkIfRelated(user.getId(), user2.getId())).willReturn(false);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(post("/api/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendInputDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void acceptRequestOk() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Friend friendRequest = new Friend();
        friendRequest.setId(1L);
        friendRequest.setRequestStatus(false);
        friendRequest.setUserSender(user);
        friendRequest.setUserReceiver(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        given(friendBO.findOne(user.getId())).willReturn(friendRequest);
        given(friendBO.checkIfRequestReceiver(user.getId(), friendRequest.getUserSender().getId())).willReturn(true);
        mockMvc.perform(patch("/api/friends/requested/{id}", friendRequest.getId()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void acceptRequestForbiddenNotLoggedIn() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Friend friendRequest = new Friend();
        friendRequest.setId(1L);
        friendRequest.setRequestStatus(false);
        friendRequest.setUserSender(user);
        friendRequest.setUserReceiver(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        given(friendBO.findOne(user.getId())).willReturn(friendRequest);
        given(friendBO.checkIfRequestReceiver(user.getId(), friendRequest.getUserSender().getId())).willReturn(true);
        mockMvc.perform(patch("/api/friends/requested/{id}", friendRequest.getId()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void acceptRequestForbiddenUserIsNotReceiver() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Friend friendRequest = new Friend();
        friendRequest.setId(1L);
        friendRequest.setRequestStatus(false);
        friendRequest.setUserSender(user);
        friendRequest.setUserReceiver(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        given(friendBO.findOne(user.getId())).willReturn(friendRequest);
        given(friendBO.checkIfRequestReceiver(user.getId(), friendRequest.getUserSender().getId())).willReturn(false);
        mockMvc.perform(patch("/api/friends/requested/{id}", friendRequest.getId()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void acceptRequestConflict() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Friend friendRequest = new Friend();
        friendRequest.setId(1L);
        friendRequest.setRequestStatus(true);
        friendRequest.setUserSender(user);
        friendRequest.setUserReceiver(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        given(friendBO.findOne(user.getId())).willReturn(friendRequest);
        given(friendBO.checkIfRequestReceiver(user.getId(), friendRequest.getUserSender().getId())).willReturn(true);
        mockMvc.perform(patch("/api/friends/requested/{id}", friendRequest.getId()))
                .andExpect(status().isConflict())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void acceptRequestNotFound() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Friend friendRequest = new Friend();
        friendRequest.setId(1L);
        friendRequest.setRequestStatus(false);
        friendRequest.setUserSender(user);
        friendRequest.setUserReceiver(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        given(friendBO.findOne(user.getId())).willReturn(null);
        given(friendBO.checkIfRequestReceiver(user.getId(), friendRequest.getUserSender().getId())).willReturn(true);
        mockMvc.perform(patch("/api/friends/requested/{id}", friendRequest.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteOk() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Friend friendRequest = new Friend();
        friendRequest.setId(1L);
        friendRequest.setRequestStatus(false);
        friendRequest.setUserSender(user);
        friendRequest.setUserReceiver(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        given(friendBO.findOne(user.getId())).willReturn(friendRequest);
        given(friendBO.checkIfRelated(user.getId(), user2.getId())).willReturn(true);
        mockMvc.perform(delete("/api/friends/{id}", friendRequest.getId()))
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
        Friend friendRequest = new Friend();
        friendRequest.setId(1L);
        friendRequest.setRequestStatus(false);
        friendRequest.setUserSender(user);
        friendRequest.setUserReceiver(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        given(friendBO.findOne(user.getId())).willReturn(friendRequest);
        given(friendBO.checkIfRelated(user.getId(), user2.getId())).willReturn(true);
        mockMvc.perform(delete("/api/friends/{id}", friendRequest.getId()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteForbiddenUsersAreNotRelated() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Friend friendRequest = new Friend();
        friendRequest.setId(1L);
        friendRequest.setRequestStatus(false);
        friendRequest.setUserSender(user);
        friendRequest.setUserReceiver(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        given(friendBO.findOne(user.getId())).willReturn(friendRequest);
        given(friendBO.checkIfRelated(user.getId(), user2.getId())).willReturn(false);
        mockMvc.perform(delete("/api/friends/{id}", friendRequest.getId()))
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
        Friend friendRequest = new Friend();
        friendRequest.setId(1L);
        friendRequest.setRequestStatus(false);
        friendRequest.setUserSender(user);
        friendRequest.setUserReceiver(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        given(friendBO.findOne(user.getId())).willReturn(null);
        given(friendBO.checkIfRelated(user.getId(), user2.getId())).willReturn(true);
        mockMvc.perform(delete("/api/friends/{id}", friendRequest.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
