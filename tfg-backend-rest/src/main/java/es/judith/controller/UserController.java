package es.judith.controller;

import es.judith.domain.User;
import es.judith.dto.UserDTO;
import es.judith.dto.UserInputDTO;
import es.judith.web.BaseController;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("unused")
public interface UserController extends BaseController {
    ResponseEntity<UserDTO> getLoggedUser();
    ResponseEntity<User> register(UserInputDTO userDTO);
    ResponseEntity<UserDTO> getUser(Long userId);
}
