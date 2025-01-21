package es.judith.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.judith.bo.UserBO;
import es.judith.controller.impl.UserControllerImpl;
import es.judith.domain.User;
import es.judith.dto.UserInputDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ComponentScan(basePackages = "es.judith.cuentame.*")
@Import(UserControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = UserControllerImpl.class)
@WebMvcTest(UserControllerImpl.class)
class UserControllerImplTest {
  @Autowired private MockMvc mockMvc;
  @MockBean private UserBO userBO;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private PasswordEncoder passwordEncoder;

  private UserInputDTO mockUser;

  @BeforeEach
  void setUp() {
    mockUser = new UserInputDTO();
    mockUser.setEmail("j@gmail.com");
    mockUser.setPassword("admin");
  }

  @Test
  void addTest() throws Exception {
    given(userBO.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response =
        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockUser)));
    response.andDo(print()).andExpect(status().isCreated());
  }

  @Test
  void addConflicTest() throws Exception {
    given(userBO.findByEmail(mockUser.getEmail())).willReturn(mockUser.obtainDomainObject());
    given(userBO.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response =
        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockUser)));
    response.andDo(print()).andExpect(status().isConflict());
  }

  @Test
  void addBadRequestTest() throws Exception {
    mockUser = new UserInputDTO();
    given(userBO.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));
    ResultActions response =
        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockUser)));
    response.andDo(print()).andExpect(status().isBadRequest());
  }
}
