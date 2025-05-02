package es.judith.controller;

import es.judith.domain.Actor;
import es.judith.domain.User;
import es.judith.dto.UserDTO;
import es.judith.dto.UserInputDTO;
import es.judith.web.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("unused")
public interface UserController extends BaseController {
    ResponseEntity<UserDTO> getLoggedUser();
    ResponseEntity<User> register(UserInputDTO userDTO);
    ResponseEntity<byte[]> findImageById(Long id);
    ResponseEntity<User> updateImageById(Long id, MultipartFile file);
    ResponseEntity<UserDTO> getUser(Long userId);
}
