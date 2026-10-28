package es.judith.controller.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import es.judith.bo.AuthBO;
import es.judith.bo.JwtBO;
import es.judith.bo.UserBO;
import es.judith.config.JwtFilter;
import es.judith.config.SecurityConfig;
import es.judith.domain.Role;
import es.judith.domain.user.User;
import es.judith.dto.user.UserDTO;
import es.judith.dto.user.UserInputDTO;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        UserInputDTO userInputDTO = new UserInputDTO();
        userInputDTO.setEmail("email");
        userInputDTO.setUsername("user");
        userInputDTO.setPassword("password");
        Map<String, String> mockToken = new HashMap<>();
        mockToken.put("token", "tokentest");
        given(authBO.verifyCredentials(userInputDTO.getEmail(), userInputDTO.getPassword())).willReturn(true);
        given(jwtBO.generateToken(userInputDTO.getEmail())).willReturn("tokentest");
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInputDTO)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(mockToken)));
    }
    @Test
    void loginForbidden() throws Exception {
        UserInputDTO userInputDTO = new UserInputDTO();
        userInputDTO.setEmail("email");
        userInputDTO.setUsername("user");
        userInputDTO.setPassword("password");
        Map<String, String> mockToken = new HashMap<>();
        mockToken.put("token", "tokentest");
        given(authBO.verifyCredentials(userInputDTO.getEmail(), userInputDTO.getPassword())).willReturn(false);
        given(jwtBO.generateToken(userInputDTO.getEmail())).willReturn("tokentest");
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInputDTO)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    void registerOk() throws Exception {
        UserInputDTO userDTO = new UserInputDTO();
        userDTO.setEmail("email@gmail.com");
        userDTO.setUsername("username");
        userDTO.setPassword("12345678");
        User user = userDTO.obtainDomainObject();
        user.setRole(Role.USER);
        given(userBO.checkForIllegalStrings(anyString(), anyString())).willReturn(false);
        given(userBO.findByEmail(anyString())).willReturn(null);
        given(userBO.findByUsername(anyString())).willReturn(null);
        given(authBO.encryptPassword(userDTO.getPassword())).willReturn("encrypted");
        user.setPassword("encrypted");
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }
    @Test
    void registerConflictEmail() throws Exception {
        UserInputDTO userDTO = new UserInputDTO();
        userDTO.setEmail("email@gmail.com");
        userDTO.setUsername("username");
        userDTO.setPassword("12345678");
        User user = userDTO.obtainDomainObject();
        user.setRole(Role.USER);
        given(userBO.checkForIllegalStrings(anyString(), anyString())).willReturn(false);
        given(userBO.findByEmail(anyString())).willReturn(user);
        given(userBO.findByUsername(anyString())).willReturn(null);
        given(authBO.encryptPassword(userDTO.getPassword())).willReturn("encrypted");
        user.setPassword("encrypted");
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isConflict())
                .andDo(print());
    }
    @Test
    void registerConflictUsername() throws Exception {
        UserInputDTO userDTO = new UserInputDTO();
        userDTO.setEmail("email@gmail.com");
        userDTO.setUsername("username");
        userDTO.setPassword("12345678");
        User user = userDTO.obtainDomainObject();
        user.setRole(Role.USER);
        given(userBO.checkForIllegalStrings(anyString(), anyString())).willReturn(false);
        given(userBO.findByEmail(anyString())).willReturn(null);
        given(userBO.findByUsername(anyString())).willReturn(user);
        given(authBO.encryptPassword(userDTO.getPassword())).willReturn("encrypted");
        user.setPassword("encrypted");
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isConflict())
                .andDo(print());
    }
    @Test
    void registerBadRequestFieldsNotPresent() throws Exception {
        UserInputDTO userDTO = new UserInputDTO();
        User user = new User();
        user.setRole(Role.USER);
        given(userBO.checkForIllegalStrings(anyString(), anyString())).willReturn(false);
        given(userBO.findByEmail(anyString())).willReturn(null);
        given(userBO.findByUsername(anyString())).willReturn(null);
        given(authBO.encryptPassword(userDTO.getPassword())).willReturn("encrypted");
        user.setPassword("encrypted");
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    void registerBadRequestIllegalStringEmail() throws Exception {
        UserInputDTO userDTO = new UserInputDTO();
        userDTO.setEmail("email@gmail.com");
        userDTO.setUsername("username");
        userDTO.setPassword("12345678");
        User user = userDTO.obtainDomainObject();
        user.setRole(Role.USER);
        given(userBO.checkForIllegalStrings(userDTO.getEmail(), "email")).willReturn(true);
        given(userBO.findByEmail(anyString())).willReturn(null);
        given(userBO.findByUsername(anyString())).willReturn(null);
        given(authBO.encryptPassword(userDTO.getPassword())).willReturn("encrypted");
        user.setPassword("encrypted");
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    void registerBadRequestIllegalStringUsername() throws Exception {
        UserInputDTO userDTO = new UserInputDTO();
        userDTO.setEmail("email@gmail.com");
        userDTO.setUsername("username");
        userDTO.setPassword("12345678");
        User user = userDTO.obtainDomainObject();
        user.setRole(Role.USER);
        given(userBO.checkForIllegalStrings(userDTO.getPassword(), "password")).willReturn(true);
        given(userBO.findByEmail(anyString())).willReturn(null);
        given(userBO.findByUsername(anyString())).willReturn(null);
        given(authBO.encryptPassword(userDTO.getPassword())).willReturn("encrypted");
        user.setPassword("encrypted");
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    void registerBadRequestIllegalStringPassword() throws Exception {
        UserInputDTO userDTO = new UserInputDTO();
        userDTO.setEmail("email@gmail.com");
        userDTO.setUsername("username");
        userDTO.setPassword("12345678");
        User user = userDTO.obtainDomainObject();
        user.setRole(Role.USER);
        given(userBO.checkForIllegalStrings(userDTO.getUsername(), "username")).willReturn(true);
        given(userBO.findByEmail(anyString())).willReturn(null);
        given(userBO.findByUsername(anyString())).willReturn(null);
        given(authBO.encryptPassword(userDTO.getPassword())).willReturn("encrypted");
        user.setPassword("encrypted");
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void findAllByUsernameOk() throws Exception {
        mockMvc.perform(get("/api/users/search"))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void findAllByUsernameForbiddenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/api/users/search"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void getLoggedUserOk() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("email@gmail.com");
        userDTO.setPassword("12345678");
        userDTO.setUsername("skfadf");
        userDTO.setRole(Role.USER);
        userDTO.setId(1L);
        User user = userDTO.obtainDomainObject();
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)));
    }
    @Test
    @WithAnonymousUser
    void getLoggedUserForbiddenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void getUserOk() throws Exception {
        UserProfileDTO userDTO = new UserProfileDTO();
        userDTO.setUsername("skfadf");
        userDTO.setId(1L);
        User user = userDTO.obtainDomainObject();
        given(userBO.findOne(userDTO.getId())).willReturn(user);
        mockMvc.perform(get("/api/users/profile/{id}", userDTO.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)));
    }
    @Test
    @WithAnonymousUser
    void getUserForbiddenNotLoggedIn() throws Exception {
        UserProfileDTO userDTO = new UserProfileDTO();
        userDTO.setUsername("skfadf");
        userDTO.setId(1L);
        User user = userDTO.obtainDomainObject();
        given(userBO.findOne(userDTO.getId())).willReturn(user);
        mockMvc.perform(get("/api/users/profile/{id}", userDTO.getId()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void getUserNotFound() throws Exception {
        UserProfileDTO userDTO = new UserProfileDTO();
        userDTO.setUsername("skfadf");
        userDTO.setId(1L);
        given(userBO.findOne(userDTO.getId())).willReturn(null);
        mockMvc.perform(get("/api/users/profile/{id}", userDTO.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void findImageByIdOk() throws Exception {
        String imageData = "askdjfkfdsjl";
        byte[] image = imageData.getBytes();
        User user = new User();
        user.setId(1L);
        user.setImageData(image);
        given(userBO.findImageById(1L)).willReturn(image);
        mockMvc.perform(get("/api/users/image/{id}", user.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().bytes(image));
    }
    @Test
    @WithAnonymousUser
    void findImageByIdForbiddenNotLoggedIn() throws Exception {
        String imageData = "askdjfkfdsjl";
        byte[] image = imageData.getBytes();
        User user = new User();
        user.setId(1L);
        user.setImageData(image);
        given(userBO.findImageById(1L)).willReturn(image);
        mockMvc.perform(get("/api/users/image/{id}", user.getId()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void findImageByIdNotFound() throws Exception {
        String imageData = "askdjfkfdsjl";
        byte[] image = imageData.getBytes();
        User user = new User();
        user.setId(1L);
        user.setImageData(image);
        given(userBO.findImageById(1L)).willReturn(null);
        mockMvc.perform(get("/api/users/image/{id}", user.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateOk() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setPassword("12345678");
        user.setEmail("email@gmail.com");
        user.setUsername("username");
        UserInputDTO newUserInfo = new UserInputDTO();
        newUserInfo.setEmail("aaaaa@gmail.com");
        given(userBO.checkForIllegalStrings(anyString(), anyString())).willReturn(false);
        given(userBO.findByEmail(newUserInfo.getEmail())).willReturn(null);
        given(userBO.findByUsername(newUserInfo.getUsername())).willReturn(null);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(patch("/api/users/details", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserInfo)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateConflictEmailAlreadyExists() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setPassword("12345678");
        user.setEmail("email@gmail.com");
        user.setUsername("username");
        UserInputDTO newUserInfo = new UserInputDTO();
        newUserInfo.setEmail("aaaaa@gmail.com");
        given(userBO.checkForIllegalStrings(anyString(), anyString())).willReturn(false);
        given(userBO.findByEmail(newUserInfo.getEmail())).willReturn(user);
        given(userBO.findByUsername(newUserInfo.getUsername())).willReturn(null);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(patch("/api/users/details", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserInfo)))
                .andExpect(status().isConflict())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateConflictUsernameAlreadyExists() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setPassword("12345678");
        user.setEmail("email@gmail.com");
        user.setUsername("username");
        UserInputDTO newUserInfo = new UserInputDTO();
        newUserInfo.setUsername("tfgjejejejetfgjejejeje");
        given(userBO.checkForIllegalStrings(anyString(), anyString())).willReturn(false);
        given(userBO.findByEmail(newUserInfo.getEmail())).willReturn(null);
        given(userBO.findByUsername(newUserInfo.getUsername())).willReturn(user);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(patch("/api/users/details", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserInfo)))
                .andExpect(status().isConflict())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateBadRequestIllegalStringEmail() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setPassword("12345678");
        user.setEmail("email@gmail.com");
        user.setUsername("username");
        UserInputDTO newUserInfo = new UserInputDTO();
        newUserInfo.setEmail("aaaaa///@gmail.com");
        given(userBO.checkForIllegalStrings(newUserInfo.getEmail(), "email")).willReturn(true);
        given(userBO.findByEmail(newUserInfo.getEmail())).willReturn(null);
        given(userBO.findByUsername(newUserInfo.getUsername())).willReturn(null);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(patch("/api/users/details", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserInfo)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateBadRequestIllegalStringUsername() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setPassword("12345678");
        user.setEmail("email@gmail.com");
        user.setUsername("username");
        UserInputDTO newUserInfo = new UserInputDTO();
        newUserInfo.setUsername("aa@@");
        given(userBO.checkForIllegalStrings(newUserInfo.getUsername(), "username")).willReturn(true);
        given(userBO.findByEmail(newUserInfo.getEmail())).willReturn(null);
        given(userBO.findByUsername(newUserInfo.getUsername())).willReturn(null);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(patch("/api/users/details", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserInfo)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateBadRequestIllegalStringPassword() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setPassword("12345678");
        user.setEmail("email@gmail.com");
        user.setUsername("username");
        UserInputDTO newUserInfo = new UserInputDTO();
        newUserInfo.setPassword("///////");
        given(userBO.checkForIllegalStrings(newUserInfo.getPassword(), "password")).willReturn(true);
        given(userBO.findByEmail(newUserInfo.getEmail())).willReturn(null);
        given(userBO.findByUsername(newUserInfo.getUsername())).willReturn(null);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(patch("/api/users/details", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserInfo)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void updateForbiddenNotLoggedIn() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setPassword("12345678");
        user.setEmail("email@gmail.com");
        user.setUsername("username");
        UserInputDTO newUserInfo = new UserInputDTO();
        newUserInfo.setEmail("aaaaa@gmail.com");
        given(userBO.checkForIllegalStrings(anyString(), anyString())).willReturn(false);
        given(userBO.findByEmail(newUserInfo.getEmail())).willReturn(null);
        given(userBO.findByUsername(newUserInfo.getUsername())).willReturn(null);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(patch("/api/users/details", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserInfo)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateImageByIdOk() throws Exception {
        User user = new User();
        user.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "image/png", "some png2".getBytes());
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/users/image");
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(authBO.getCurrentUser()).willReturn(user);
        given(userBO.findOne(1L)).willReturn(user);
        given(userBO.save(user)).willReturn(user);
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isNoContent());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateImageByIdBadRequestFileNotAnImage() throws Exception {
        User user = new User();
        user.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "desesperacion", "some png2".getBytes());
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/users/image");
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(authBO.getCurrentUser()).willReturn(user);
        given(userBO.findOne(1L)).willReturn(user);
        given(userBO.save(user)).willReturn(user);
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateImageByIdBadRequestFileNotAttached() throws Exception {
        User user = new User();
        user.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "image/png", new byte[0]);
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/users/image");
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(authBO.getCurrentUser()).willReturn(user);
        given(userBO.findOne(1L)).willReturn(user);
        given(userBO.save(user)).willReturn(user);
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void updateImageByIdBadRequestFileTooLarge() throws Exception {
        User user = new User();
        user.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "image/png", new byte[1000000000]);
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/users/image");
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(authBO.getCurrentUser()).willReturn(user);
        given(userBO.findOne(1L)).willReturn(user);
        given(userBO.save(user)).willReturn(user);
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isBadRequest());
    }
    @Test
    @WithAnonymousUser
    void updateImageByIdForbiddenNotLoggedIn() throws Exception {
        User user = new User();
        user.setId(1L);
        MockMultipartFile newImage =
                new MockMultipartFile("image", "filename2.png", "image/png", "png".getBytes());
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/users/image");
        builder.with(
                request -> {
                    request.setMethod("PATCH");
                    return request;
                });
        given(authBO.getCurrentUser()).willReturn(user);
        given(userBO.findOne(1L)).willReturn(user);
        given(userBO.save(user)).willReturn(user);
        mockMvc.perform(builder.file(newImage)).andDo(print()).andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void promoteUserOk() throws Exception {
        mockMvc.perform(patch("/api/users/promote/{id}", 1L))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void promoteUserForbiddenNotLoggedIn() throws Exception {
        mockMvc.perform(patch("/api/users/promote/{id}", 1L))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void promoteUserForbiddenNotAdmin() throws Exception {
        mockMvc.perform(patch("/api/users/promote/{id}", 1L))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteOkIsCurrentUser() throws Exception {
        User user = new User();
        user.setId(1L);
        given(userBO.findOne(user.getId())).willReturn(user);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(delete("/api/users/{id}", user.getId()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteOkIsAdmin() throws Exception {
        User user = new User();
        user.setId(2L);
        user.setRole(Role.ADMIN);
        User user2 = new User();
        user2.setId(1L);
        given(userBO.findOne(user2.getId())).willReturn(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteForbiddenNotCurrentUserAndNotAdmin() throws Exception {
        User user = new User();
        user.setId(2L);
        User user2 = new User();
        user2.setId(1L);
        given(userBO.findOne(user2.getId())).willReturn(user2);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithAnonymousUser
    void deleteForbiddenNotLoggedIn() throws Exception {
        User user = new User();
        user.setId(1L);
        given(userBO.findOne(user.getId())).willReturn(user);
        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
    @Test
    @WithMockUser(authorities = "USER")
    void deleteNotFound() throws Exception {
        User user = new User();
        user.setId(2L);
        user.setRole(Role.ADMIN);
        given(userBO.findOne(user.getId())).willReturn(null);
        given(authBO.getCurrentUser()).willReturn(user);
        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
