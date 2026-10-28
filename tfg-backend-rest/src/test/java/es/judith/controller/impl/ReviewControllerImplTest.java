package es.judith.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.judith.bo.*;
import es.judith.config.JwtFilter;
import es.judith.config.SecurityConfig;
import es.judith.domain.Review;
import es.judith.domain.Show;
import es.judith.domain.user.User;
import es.judith.dto.review.ReviewInputDTO;
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
@ContextConfiguration(classes = ReviewControllerImpl.class)
@WebMvcTest(ReviewControllerImpl.class)
@Import({
        SecurityConfig.class,
        JwtFilter.class,
        RecommendationControllerImplTest.SecurityBeans.class
})
class ReviewControllerImplTest {

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
    private ReviewBO reviewBO;
    @MockitoBean
    private ShowBO showBO;
    @MockitoBean
    private AuthBO authBO;
    @MockitoBean
    private FriendBO friendBO;
    @MockitoBean
    private UserBO userBO;

    @Test
    void findAverageRatingByShowOk() throws Exception {
        Show show = new Show();
        show.setId(1L);
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Review review1 = new Review();
        review1.setUser(user1);
        review1.setRating(4);
        review1.setShow(show);
        Review review2 = new Review();
        review2.setUser(user2);
        review2.setRating(3);
        review2.setShow(show);
        given(reviewBO.calculateAverageRating(show.getId())).willReturn(3.5);
        given(showBO.findOne(show.getId())).willReturn(show);
        mockMvc.perform(get("/api/reviews/show/{id}", show.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    void findAverageRatingByShowNotFound() throws Exception {
        Show show = new Show();
        show.setId(1L);
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Review review1 = new Review();
        review1.setUser(user1);
        review1.setRating(4);
        review1.setShow(show);
        Review review2 = new Review();
        review2.setUser(user2);
        review2.setRating(3);
        review2.setShow(show);
        given(reviewBO.calculateAverageRating(show.getId())).willReturn(3.5);
        given(showBO.findOne(show.getId())).willReturn(null);
        mockMvc.perform(get("/api/reviews/show/{id}", show.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void findAllByUserOk() throws Exception {
        User user1 = new User();
        User user2 = new User();
        user1.setId(1L);
        user2.setId(2L);
        given(authBO.getCurrentUser()).willReturn(user1);
        given(friendBO.checkIfFriend(user1.getId(), user2.getId())).willReturn(true);
        given(reviewBO.findAllByUserId(user2.getId())).willReturn(List.of());
        given(userBO.findOne(user2.getId())).willReturn(user2);
        mockMvc.perform(get("/api/reviews/user/{id}", user2.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void findAllByUserForbiddenNotLoggedIn() throws Exception {
        User user1 = new User();
        User user2 = new User();
        user1.setId(1L);
        user2.setId(2L);
        given(authBO.getCurrentUser()).willReturn(user1);
        given(friendBO.checkIfFriend(user1.getId(), user2.getId())).willReturn(true);
        given(reviewBO.findAllByUserId(user2.getId())).willReturn(List.of());
        given(userBO.findOne(user2.getId())).willReturn(user2);
        mockMvc.perform(get("/api/reviews/user/{id}", user2.getId()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void findAllByUserForbiddenUserNotFriend() throws Exception {
        User user1 = new User();
        User user2 = new User();
        user1.setId(1L);
        user2.setId(2L);
        given(authBO.getCurrentUser()).willReturn(user1);
        given(friendBO.checkIfFriend(user1.getId(), user2.getId())).willReturn(false);
        given(reviewBO.findAllByUserId(user2.getId())).willReturn(List.of());
        given(userBO.findOne(user2.getId())).willReturn(user2);
        mockMvc.perform(get("/api/reviews/user/{id}", user2.getId()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void findAllByUserNotFound() throws Exception {
        User user1 = new User();
        User user2 = new User();
        user1.setId(1L);
        user2.setId(2L);
        given(authBO.getCurrentUser()).willReturn(user1);
        given(friendBO.checkIfFriend(user1.getId(), user2.getId())).willReturn(true);
        given(reviewBO.findAllByUserId(user2.getId())).willReturn(List.of());
        given(userBO.findOne(user2.getId())).willReturn(null);
        mockMvc.perform(get("/api/reviews/user/{id}", user2.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void addOkNewReview() throws Exception {
        Show show = new Show();
        show.setId(1L);
        User user = new User();
        user.setId(1L);
        ReviewInputDTO reviewInputDTO = new ReviewInputDTO();
        reviewInputDTO.setRating(1);
        reviewInputDTO.setShowId(1L);
        given(authBO.getCurrentUser()).willReturn(user);
        given(reviewBO.checkIfUserReviewInShow(show.getId(), user.getId())).willReturn(null);
        given(showBO.findOne(show.getId())).willReturn(show);
        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewInputDTO)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void addOkModifyExistingReview() throws Exception {
        Show show = new Show();
        show.setId(1L);
        User user = new User();
        user.setId(1L);
        Review existingReview = new Review();
        existingReview.setShow(show);
        existingReview.setUser(user);
        existingReview.setRating(3);
        existingReview.setId(1L);
        ReviewInputDTO reviewInputDTO = new ReviewInputDTO();
        reviewInputDTO.setRating(1);
        reviewInputDTO.setShowId(1L);
        given(authBO.getCurrentUser()).willReturn(user);
        given(reviewBO.checkIfUserReviewInShow(show.getId(), user.getId())).willReturn(existingReview);
        given(showBO.findOne(show.getId())).willReturn(show);
        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewInputDTO)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void addOkForbiddenNotLoggedIn() throws Exception {
        Show show = new Show();
        show.setId(1L);
        User user = new User();
        user.setId(1L);
        ReviewInputDTO reviewInputDTO = new ReviewInputDTO();
        reviewInputDTO.setRating(1);
        reviewInputDTO.setShowId(1L);
        given(authBO.getCurrentUser()).willReturn(user);
        given(reviewBO.checkIfUserReviewInShow(show.getId(), user.getId())).willReturn(null);
        given(showBO.findOne(show.getId())).willReturn(show);
        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewInputDTO)))
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
        ReviewInputDTO reviewInputDTO = new ReviewInputDTO();
        given(authBO.getCurrentUser()).willReturn(user);
        given(reviewBO.checkIfUserReviewInShow(show.getId(), user.getId())).willReturn(null);
        given(showBO.findOne(show.getId())).willReturn(show);
        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewInputDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void addBadRequestInvalidRating() throws Exception {
        Show show = new Show();
        show.setId(1L);
        User user = new User();
        user.setId(1L);
        ReviewInputDTO reviewInputDTO = new ReviewInputDTO();
        reviewInputDTO.setRating(70);
        reviewInputDTO.setShowId(1L);
        given(authBO.getCurrentUser()).willReturn(user);
        given(reviewBO.checkIfUserReviewInShow(show.getId(), user.getId())).willReturn(null);
        given(showBO.findOne(show.getId())).willReturn(show);
        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewInputDTO)))
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
        ReviewInputDTO reviewInputDTO = new ReviewInputDTO();
        reviewInputDTO.setRating(1);
        reviewInputDTO.setShowId(1L);
        given(authBO.getCurrentUser()).willReturn(user);
        given(reviewBO.checkIfUserReviewInShow(show.getId(), user.getId())).willReturn(null);
        given(showBO.findOne(show.getId())).willReturn(null);
        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewInputDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteOk() throws Exception {
        User user = new User();
        user.setId(1L);
        Review review = new Review();
        review.setId(1L);
        review.setUser(user);
        given(reviewBO.exists(review.getId())).willReturn(true);
        given(reviewBO.findOne(review.getId())).willReturn(review);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(delete("/api/reviews/{id}", review.getId()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void deleteForbiddenNotLoggedIn() throws Exception {
        User user = new User();
        user.setId(1L);
        Review review = new Review();
        review.setId(1L);
        review.setUser(user);
        given(reviewBO.exists(review.getId())).willReturn(true);
        given(reviewBO.findOne(review.getId())).willReturn(review);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(delete("/api/reviews/{id}", review.getId()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteForbiddenNotReviewAuthor() throws Exception {
        User user = new User();
        user.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Review review = new Review();
        review.setId(1L);
        review.setUser(user2);
        given(reviewBO.exists(review.getId())).willReturn(true);
        given(reviewBO.findOne(review.getId())).willReturn(review);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(delete("/api/reviews/{id}", review.getId()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteNotFound() throws Exception {
        User user = new User();
        user.setId(1L);
        Review review = new Review();
        review.setId(1L);
        review.setUser(user);
        given(reviewBO.exists(review.getId())).willReturn(false);
        given(reviewBO.findOne(review.getId())).willReturn(null);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(delete("/api/reviews/{id}", review.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
