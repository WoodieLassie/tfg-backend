package es.judith.controller;

import es.judith.domain.user.User;
import es.judith.dto.user.UserDTO;
import es.judith.dto.user.UserInputDTO;
import es.judith.dto.user.UserProfileDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("unused")
public interface UserController extends Serializable {
    ResponseEntity<UserDTO> getLoggedUser();
    ResponseEntity<Map<String, String>> login(UserInputDTO userInputDTO);
    ResponseEntity<User> register(UserInputDTO userDTO);
    ResponseEntity<byte[]> findImageById(Long id);
    ResponseEntity<User> update(UserInputDTO userDTO);
    ResponseEntity<User> updateImageById(MultipartFile file);
    ResponseEntity<UserProfileDTO> getUser(Long userId);
    ResponseEntity<User> delete(Long userId);
}
