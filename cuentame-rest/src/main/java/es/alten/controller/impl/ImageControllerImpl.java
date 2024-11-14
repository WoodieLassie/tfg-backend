package es.alten.controller.impl;

import es.alten.bo.ImageBO;
import es.alten.controller.ImageController;
import es.alten.domain.ElvisEntity;
import es.alten.domain.Image;
import es.alten.dto.ImageDTO;
import es.alten.exceptions.BadInputException;
import es.alten.rest.impl.RestControllerImpl;
import es.alten.utils.ImageUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
@Tag(name = "images")
public class ImageControllerImpl implements ImageController {
  private static final Logger LOG = LoggerFactory.getLogger(ImageControllerImpl.class);
  private final ImageBO bo;

  public ImageControllerImpl(ImageBO bo) {
    this.bo = bo;
  }

  @GetMapping(produces = MediaType.IMAGE_PNG_VALUE, value = "/{id}")
  public ResponseEntity<byte[]> findById(@PathVariable Long id) {
      return ResponseEntity.ok(bo.findById(id));
  }

  @GetMapping(produces = MediaType.IMAGE_PNG_VALUE, params = {"name"})
  public ResponseEntity<byte[]> findByName(@RequestParam String name) {
    return ResponseEntity.ok(bo.findByName(name));
  }

  @PostMapping
  public ResponseEntity<ImageDTO> add(@RequestParam("image") MultipartFile file) {
    ImageDTO imageDTO = new ImageDTO();
    imageDTO.setName(file.getOriginalFilename());
    imageDTO.setType(file.getContentType());
    try {
      imageDTO.setImageData(ImageUtil.compressImage(file.getBytes()));
    } catch (IOException e) {
      throw new BadInputException(e);
    }
    bo.save(imageDTO);
    return ResponseEntity.ok(imageDTO);
  }
}
