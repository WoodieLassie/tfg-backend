package es.judith.controller;

import es.judith.domain.User;
import es.judith.dto.UserDTO;
import es.judith.dto.UserInputDTO;
import es.judith.dto.UserProfileDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
