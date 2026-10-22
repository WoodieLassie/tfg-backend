package es.judith.controller.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import es.judith.bo.AuthBO;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@ContextConfiguration(classes = UserControllerImpl.class)
@WebMvcTest(UserControllerImpl.class)
@Import({
        SecurityConfig.class,
        JwtFilter.class,
        UserControllerImplTest.SecurityBeans.class
})
class UserControllerImplTest {

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
    private UserBO userBO;
    @MockitoBean
    private AuthBO authBO;

    @Test
    void loginOk() throws Exception {

    }
    @Test
    void loginForbidden() throws Exception {

    }
    @Test
    void registerOk() throws Exception {

    }
    @Test
    void registerConflictEmail() throws Exception {

    }
    @Test
    void registerConflictUsername() throws Exception {

    }
    @Test
    void registerBadRequestFieldsNotPresent() throws Exception {

    }
    @Test
    void registerBadRequestIllegalStringEmail() throws Exception {

    }
    @Test
    void registerBadRequestIllegalStringUsername() throws Exception {

    }
    @Test
    void registerBadRequestIllegalStringPassword() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void findAllByUsernameOk() throws Exception {

    }
    @Test
    void findAllByUsernameForbiddenNotLoggedIn() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void getLoggedUserOk() throws Exception {

    }
    @Test
    @WithAnonymousUser
    void getLoggedUserForbiddenNotLoggedIn() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void getUserOk() throws Exception {

    }
    @Test
    @WithAnonymousUser
    void getUserForbiddenNotLoggedIn() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void getUserNotFound() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void findImageByIdOk() throws Exception {

    }
    @Test
    @WithAnonymousUser
    void findImageByIdForbiddenNotLoggedIn() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void findImageByIdNotFound() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateOk() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateConflictEmailAlreadyExists() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateConflictUsernameAlreadyExists() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateBadRequestIllegalStringEmail() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateBadRequestIllegalStringUsername() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateBadRequestIllegalStringPassword() throws Exception {

    }
    @Test
    @WithAnonymousUser
    void updateForbiddenNotLoggedIn() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateImageByIdOk() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateImageByIdBadRequestFileNotAnImage() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateImageByIdBadRequestFileNotAttached() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateImageByIdBadRequestFileTooLarge() throws Exception {

    }
    @Test
    @WithAnonymousUser
    void updateImageByIdForbiddenNotLoggedIn() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteOkIsCurrentUser() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteOkIsAdmin() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteForbiddenNotCurrentUserAndNotAdmin() throws Exception {

    }
    @Test
    @WithAnonymousUser
    void deleteForbiddenNotLoggedIn() throws Exception {

    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteNotFound() throws Exception {

    }
}
