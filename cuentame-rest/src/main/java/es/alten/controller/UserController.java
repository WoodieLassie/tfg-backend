package es.alten.controller;

import es.alten.domain.User;
import es.alten.dto.UserDTO;
import es.alten.dto.UserInputDTO;
import es.alten.web.BaseController;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("unused")
public interface UserController extends BaseController {
    ResponseEntity<User> register(UserInputDTO userDTO);
}
