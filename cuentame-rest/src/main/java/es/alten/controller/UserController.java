package es.alten.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.alten.domain.User;
import es.alten.dto.UserDTO;
import es.alten.web.BaseController;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("unused")
public interface UserController extends BaseController {
    ResponseEntity<String> login(String username, String password) throws JsonProcessingException;
    ResponseEntity<User> register(UserDTO userDTO);
}
