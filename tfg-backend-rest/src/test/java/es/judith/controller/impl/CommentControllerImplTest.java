package es.judith.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.judith.bo.AuthBO;
import es.judith.bo.CommentBO;
import es.judith.bo.JwtBO;
import es.judith.bo.ShowBO;
import es.judith.config.JwtFilter;
import es.judith.config.SecurityConfig;
import es.judith.domain.Comment;
import es.judith.domain.Role;
import es.judith.domain.Show;
import es.judith.domain.user.User;
import es.judith.dto.comment.CommentDTO;
import es.judith.dto.comment.CommentInputDTO;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = CommentControllerImpl.class)
@WebMvcTest(CommentControllerImpl.class)
@Import({
        SecurityConfig.class,
        JwtFilter.class,
        CommentControllerImplTest.SecurityBeans.class
})
public class CommentControllerImplTest {

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
    private CommentBO commentBO;
    @MockitoBean
    private AuthBO authBO;
    @MockitoBean
    private ShowBO showBO;

    @Test
    void findAllOk() throws Exception {
        Show show = new Show();
        show.setId(1L);
        User user = new User();
        user.setId(1L);
        Comment comment = new Comment();
        comment.setShow(show);
        comment.setUser(user);
        comment.setText("text");
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.loadFromDomain(comment);
        List<CommentDTO> commentList = List.of(commentDTO);
        given(commentBO.findAllByShowIdWithUser(show.getId())).willReturn(commentList);
        mockMvc.perform(get("/api/comments/{id}", comment.getShow().getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void addOk() throws Exception {
        Show show = new Show();
        show.setId(1L);
        User user = new User();
        user.setId(1L);
        CommentInputDTO commentInputDTO = new CommentInputDTO();
        commentInputDTO.setShowId(1L);
        commentInputDTO.setUserId(1L);
        commentInputDTO.setText("AAAAAAAAAAAAAAAAAAAAAAGFDJKNJKGFD");
        given(commentBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(showBO.findOne(show.getId())).willReturn(show);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentInputDTO)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void addForbiddenNotLoggedIn() throws Exception {
        Show show = new Show();
        show.setId(1L);
        User user = new User();
        user.setId(1L);
        CommentInputDTO commentInputDTO = new CommentInputDTO();
        commentInputDTO.setShowId(1L);
        commentInputDTO.setUserId(1L);
        commentInputDTO.setText("AAAAAAAAAAAAAAAAAAAAAAGFDJKNJKGFD");
        given(commentBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(showBO.findOne(show.getId())).willReturn(show);
        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentInputDTO)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void addBadRequestFieldsNotPresent() throws Exception {
        Show show = new Show();
        show.setId(1L);
        User user = new User();
        user.setId(1L);
        CommentInputDTO commentInputDTO = new CommentInputDTO();
        given(commentBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(showBO.findOne(show.getId())).willReturn(show);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentInputDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void addBadRequestIllegalString() throws Exception {
        Show show = new Show();
        show.setId(1L);
        User user = new User();
        user.setId(1L);
        CommentInputDTO commentInputDTO = new CommentInputDTO();
        commentInputDTO.setShowId(1L);
        commentInputDTO.setUserId(1L);
        commentInputDTO.setText("AAAAAAAAAAAAAAAAAAAAAAGFDJKNJKGFD////");
        given(commentBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(true);
        given(showBO.findOne(show.getId())).willReturn(show);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentInputDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void addNotFound() throws Exception {
        Show show = new Show();
        show.setId(1L);
        User user = new User();
        user.setId(1L);
        CommentInputDTO commentInputDTO = new CommentInputDTO();
        commentInputDTO.setShowId(1L);
        commentInputDTO.setUserId(1L);
        commentInputDTO.setText("AAAAAAAAAAAAAAAAAAAAAAGFDJKNJKGFD");
        given(commentBO.checkForIllegalStrings(anyString(), anyInt())).willReturn(false);
        given(showBO.findOne(show.getId())).willReturn(null);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentInputDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteOkIsCommentAuthor() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setId(1L);
        given(commentBO.exists(comment.getId())).willReturn(true);
        given(authBO.getCurrentUser()).willReturn(user);
        given(commentBO.findOne(comment.getId())).willReturn(comment);
        mockMvc.perform(delete("/api/comments/{id}", comment.getId()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @WithMockUser(authorities = "ADMIN")
    @Test
    void deleteOkIsAdmin() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);
        User user2 = new User();
        user2.setId(2L);
        user2.setRole(Role.ADMIN);
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setId(1L);
        given(commentBO.exists(comment.getId())).willReturn(true);
        given(authBO.getCurrentUser()).willReturn(user2);
        given(commentBO.findOne(comment.getId())).willReturn(comment);
        mockMvc.perform(delete("/api/comments/{id}", comment.getId()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteForbiddenNotAdminAndNotCommentAuthor() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);
        User user2 = new User();
        user2.setId(2L);
        user2.setRole(Role.USER);
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setId(1L);
        given(commentBO.exists(comment.getId())).willReturn(true);
        given(authBO.getCurrentUser()).willReturn(user2);
        given(commentBO.findOne(comment.getId())).willReturn(comment);
        mockMvc.perform(delete("/api/comments/{id}", comment.getId()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void deleteForbiddenNotLoggedIn() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);
        User user2 = new User();
        user2.setId(2L);
        user2.setRole(Role.USER);
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setId(1L);
        given(commentBO.exists(comment.getId())).willReturn(true);
        given(commentBO.findOne(comment.getId())).willReturn(comment);
        mockMvc.perform(delete("/api/comments/{id}", comment.getId()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteNotFound() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);
        User user2 = new User();
        user2.setId(2L);
        user2.setRole(Role.USER);
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setId(1L);
        given(commentBO.exists(comment.getId())).willReturn(false);
        given(authBO.getCurrentUser()).willReturn(user2);
        given(commentBO.findOne(comment.getId())).willReturn(null);
        mockMvc.perform(delete("/api/comments/{id}", comment.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
