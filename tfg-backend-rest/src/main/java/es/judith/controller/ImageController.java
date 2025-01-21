package es.judith.controller;

import es.judith.domain.Image;
import es.judith.dto.ImageDTO;
import es.judith.web.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageController extends BaseController {
    ResponseEntity<List<ImageDTO>> findAll();
    ResponseEntity<List<ImageDTO>> findByName(String name);
    ResponseEntity<byte[]> findById(Long id);
    ResponseEntity<Image> add(MultipartFile file);
    ResponseEntity<Image> update(Long id, MultipartFile file);
    ResponseEntity<ImageDTO> delete(Long id);
}
