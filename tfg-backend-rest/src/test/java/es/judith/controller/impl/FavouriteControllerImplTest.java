package es.judith.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.judith.bo.*;
import es.judith.config.JwtFilter;
import es.judith.config.SecurityConfig;
import es.judith.domain.Favourite;
import es.judith.domain.Show;
import es.judith.domain.user.User;
import es.judith.dto.favourite.FavouriteDTO;
import es.judith.dto.favourite.FavouriteInputDTO;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = FavouriteControllerImpl.class)
@WebMvcTest(FavouriteControllerImpl.class)
@Import({
        SecurityConfig.class,
        JwtFilter.class,
        FavouriteControllerImplTest.SecurityBeans.class
})
public class FavouriteControllerImplTest {

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
    private FavouriteBO favouriteBO;
    @MockitoBean
    private ShowBO showBO;
    @MockitoBean
    private AuthBO authBO;
    @MockitoBean
    private FriendBO friendBO;
    @MockitoBean
    private UserBO userBO;

    @Test
    @WithMockUser(authorities = "USER")
    void findAllByUserOkIsCurrentUser() throws Exception {
        User user = new User();
        user.setId(1L);
        Show show = new Show();
        show.setId(1L);
        Favourite favourite = new Favourite();
        favourite.setShow(show);
        favourite.setUser(user);
        FavouriteDTO favouriteDTO = new FavouriteDTO();
        favouriteDTO.loadFromDomain(favourite);
        List<Favourite> favouriteList = List.of(favourite);
        given(userBO.findOne(user.getId())).willReturn(user);
        given(authBO.getCurrentUser()).willReturn(user);
        given(favouriteBO.findAllByUser(user.getId())).willReturn(favouriteList);
        mockMvc.perform(get("/api/favourites/{id}", user.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void findAllByUserOkIsFriend() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Show show = new Show();
        show.setId(1L);
        Favourite favourite = new Favourite();
        favourite.setShow(show);
        favourite.setUser(user2);
        FavouriteDTO favouriteDTO = new FavouriteDTO();
        favouriteDTO.loadFromDomain(favourite);
        List<Favourite> favouriteList = List.of(favourite);
        given(userBO.findOne(user2.getId())).willReturn(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        given(favouriteBO.findAllByUser(user.getId())).willReturn(favouriteList);
        given(friendBO.checkIfFriend(user.getId(), user2.getId())).willReturn(true);
        mockMvc.perform(get("/api/favourites/{id}", user2.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void findAllByUserForbiddenNotFriendAndNotCurrentUser() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Show show = new Show();
        show.setId(1L);
        Favourite favourite = new Favourite();
        favourite.setShow(show);
        favourite.setUser(user2);
        FavouriteDTO favouriteDTO = new FavouriteDTO();
        favouriteDTO.loadFromDomain(favourite);
        List<Favourite> favouriteList = List.of(favourite);
        given(userBO.findOne(user2.getId())).willReturn(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        given(favouriteBO.findAllByUser(user.getId())).willReturn(favouriteList);
        given(friendBO.checkIfFriend(user.getId(), user2.getId())).willReturn(false);
        mockMvc.perform(get("/api/favourites/{id}", user2.getId()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void findAllByUserForbiddenNotLoggedIn() throws Exception {
        User user = new User();
        user.setId(1L);
        Show show = new Show();
        show.setId(1L);
        Favourite favourite = new Favourite();
        favourite.setShow(show);
        favourite.setUser(user);
        FavouriteDTO favouriteDTO = new FavouriteDTO();
        favouriteDTO.loadFromDomain(favourite);
        List<Favourite> favouriteList = List.of(favourite);
        given(userBO.findOne(user.getId())).willReturn(user);
        given(favouriteBO.findAllByUser(user.getId())).willReturn(favouriteList);
        mockMvc.perform(get("/api/favourites/{id}", user.getId()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void findAllByUserNotFound() throws Exception {
        User user = new User();
        user.setId(1L);
        Show show = new Show();
        show.setId(1L);
        Favourite favourite = new Favourite();
        favourite.setShow(show);
        favourite.setUser(user);
        FavouriteDTO favouriteDTO = new FavouriteDTO();
        favouriteDTO.loadFromDomain(favourite);
        List<Favourite> favouriteList = List.of(favourite);
        given(userBO.findOne(user.getId())).willReturn(null);
        given(authBO.getCurrentUser()).willReturn(user);
        given(favouriteBO.findAllByUser(user.getId())).willReturn(favouriteList);
        mockMvc.perform(get("/api/favourites/{id}", user.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void addOk() throws Exception {
        Show show = new Show();
        show.setId(1L);
        User user = new User();
        user.setId(1L);
        FavouriteInputDTO favouriteInputDTO = new FavouriteInputDTO();
        favouriteInputDTO.setShowId(show.getId());
        favouriteInputDTO.setUserId(user.getId());
        given(showBO.findOne(favouriteInputDTO.getShowId())).willReturn(show);
        given(authBO.getCurrentUser()).willReturn(user);
        given(favouriteBO.findAllByUser(user.getId())).willReturn(List.of());
        mockMvc.perform(post("/api/favourites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favouriteInputDTO)))
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
        FavouriteInputDTO favouriteInputDTO = new FavouriteInputDTO();
        favouriteInputDTO.setShowId(show.getId());
        favouriteInputDTO.setUserId(user.getId());
        given(showBO.findOne(favouriteInputDTO.getShowId())).willReturn(show);
        given(authBO.getCurrentUser()).willReturn(user);
        given(favouriteBO.findAllByUser(user.getId())).willReturn(List.of());
        mockMvc.perform(post("/api/favourites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favouriteInputDTO)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void addConflict() throws Exception {
        Show show = new Show();
        show.setId(1L);
        User user = new User();
        user.setId(1L);
        FavouriteInputDTO favouriteInputDTO = new FavouriteInputDTO();
        favouriteInputDTO.setShowId(show.getId());
        favouriteInputDTO.setUserId(user.getId());
        Favourite favourite = favouriteInputDTO.obtainDomainObject();
        favourite.setUser(user);
        favourite.setShow(show);
        given(showBO.findOne(favouriteInputDTO.getShowId())).willReturn(show);
        given(authBO.getCurrentUser()).willReturn(user);
        given(favouriteBO.findAllByUser(user.getId())).willReturn(List.of(favourite));
        mockMvc.perform(post("/api/favourites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favouriteInputDTO)))
                .andExpect(status().isConflict())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void addNotFound() throws Exception {
        Show show = new Show();
        show.setId(1L);
        User user = new User();
        user.setId(1L);
        FavouriteInputDTO favouriteInputDTO = new FavouriteInputDTO();
        favouriteInputDTO.setShowId(show.getId());
        favouriteInputDTO.setUserId(user.getId());
        given(showBO.findOne(favouriteInputDTO.getShowId())).willReturn(null);
        given(authBO.getCurrentUser()).willReturn(user);
        given(favouriteBO.findAllByUser(user.getId())).willReturn(List.of());
        mockMvc.perform(post("/api/favourites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favouriteInputDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void addBadRequest() throws Exception {
        Show show = new Show();
        show.setId(1L);
        User user = new User();
        user.setId(1L);
        FavouriteInputDTO favouriteInputDTO = new FavouriteInputDTO();
        given(showBO.findOne(favouriteInputDTO.getShowId())).willReturn(show);
        given(authBO.getCurrentUser()).willReturn(user);
        given(favouriteBO.findAllByUser(user.getId())).willReturn(List.of());
        mockMvc.perform(post("/api/favourites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favouriteInputDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteOk() throws Exception {
        User user = new User();
        user.setId(1L);
        Favourite favourite = new Favourite();
        favourite.setUser(user);
        favourite.setId(1L);
        given(favouriteBO.exists(favourite.getId())).willReturn(true);
        given(favouriteBO.findOne(favourite.getId())).willReturn(favourite);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(delete("/api/favourites/{id}", favourite.getId()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteForbiddenNotCurrentUser() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Favourite favourite = new Favourite();
        favourite.setUser(user);
        favourite.setId(1L);
        given(favouriteBO.exists(favourite.getId())).willReturn(true);
        given(favouriteBO.findOne(favourite.getId())).willReturn(favourite);
        given(authBO.getCurrentUser()).willReturn(user2);
        mockMvc.perform(delete("/api/favourites/{id}", favourite.getId()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void deleteForbiddenNotLoggedIn() throws Exception {
        User user = new User();
        user.setId(1L);
        Favourite favourite = new Favourite();
        favourite.setUser(user);
        favourite.setId(1L);
        given(favouriteBO.exists(favourite.getId())).willReturn(true);
        given(favouriteBO.findOne(favourite.getId())).willReturn(favourite);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(delete("/api/favourites/{id}", favourite.getId()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteNotFound() throws Exception {
        User user = new User();
        user.setId(1L);
        Favourite favourite = new Favourite();
        favourite.setUser(user);
        favourite.setId(1L);
        given(favouriteBO.exists(favourite.getId())).willReturn(false);
        given(favouriteBO.findOne(favourite.getId())).willReturn(null);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(delete("/api/favourites/{id}", favourite.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
