package es.alten.controller;

import es.alten.dto.ActorDTO;
import es.alten.rest.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ActorController extends BaseController {
    ResponseEntity<byte[]> findImageById(Long id);
    ResponseEntity<ActorDTO> updateImageById(Long id, MultipartFile file);
}
