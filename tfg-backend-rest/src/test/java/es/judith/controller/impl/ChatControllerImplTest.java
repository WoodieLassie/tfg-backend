package es.judith.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.judith.bo.ChatMessageBO;
import es.judith.bo.JwtBO;
import es.judith.bo.UserBO;
import es.judith.config.JwtFilter;
import es.judith.config.SecurityConfig;
import es.judith.security.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = ChatControllerImpl.class)
@WebMvcTest(ChatControllerImpl.class)
@Import({
        SecurityConfig.class,
        JwtFilter.class,
        ChatControllerImplTest.SecurityBeans.class
})
public class ChatControllerImplTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtBO jwtBO;
    @MockitoBean
    private CustomUserDetailsService userDetailsService;
    @MockitoBean
    private ChatMessageBO chatMessageBO;
    @MockitoBean
    private UserBO userBO;
    @MockitoBean
    private SimpMessagingTemplate messagingTemplate;

    @TestConfiguration
    static class SecurityBeans {
        @Bean
        BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(10);
        }
    }

    @Test
    @WithMockUser(authorities = "USER")
    void getMessagesOk() throws Exception {
        given(chatMessageBO.findChatMessages(1L, 2L)).willReturn(List.of());
        mockMvc.perform(get("/api/messages/{senderId}/{receiverId}", 1L, 2L))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void getMessagesForbiddenNotLoggedIn() throws Exception {
        given(chatMessageBO.findChatMessages(1L, 2L)).willReturn(List.of());
        mockMvc.perform(get("/api/messages/{senderId}/{receiverId}", 1L, 2L))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void getMessagesForbiddenUserIsNotFriend() throws Exception {
        //TODO: Todavia no está implementada la logica de amistades en Chat. Pendiente de testear mensajeria en frontend
    }
}
