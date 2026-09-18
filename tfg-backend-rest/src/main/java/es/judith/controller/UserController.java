package es.judith.controller;

import es.judith.domain.User;
import es.judith.dto.UserDTO;
import es.judith.dto.UserInputDTO;
import es.judith.dto.UserProfileDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@SuppressWarnings("unused")
public interface UserController extends Serializable {
    ResponseEntity<UserDTO> getLoggedUser();
    ResponseEntity<User> register(UserInputDTO userDTO);
    ResponseEntity<byte[]> findImageById(Long id);
    ResponseEntity<User> updateImageById(MultipartFile file);
    ResponseEntity<UserProfileDTO> getUser(Long userId);
}
