package es.alten.controller.impl;

import es.alten.bo.ImageBO;
import es.alten.controller.ImageController;
import es.alten.domain.Image;
import es.alten.dto.ImageDTO;
import es.alten.exceptions.BadInputException;
import es.alten.utils.ImageUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@Tag(name = "images")
public class ImageControllerImpl implements ImageController {
  private static final Logger LOG = LoggerFactory.getLogger(ImageControllerImpl.class);
  private final ImageBO bo;

  public ImageControllerImpl(ImageBO bo) {
    this.bo = bo;
  }

  @GetMapping
  public ResponseEntity<List<ImageDTO>> findAll() {
    List<Image> images = bo.findAll();
    List<ImageDTO> convertedImages = new ArrayList<>();
    for (Image image : images) {
      ImageDTO imageDTO = new ImageDTO();
      imageDTO.loadFromDomain(image);
      convertedImages.add(imageDTO);
    }
    return ResponseEntity.ok(convertedImages);
  }

  @GetMapping(params = {"name"})
  public ResponseEntity<List<ImageDTO>> findByName(@RequestParam String name) {
    List<Image> images = bo.findByName(name);
    List<ImageDTO> convertedImages = new ArrayList<>();
    for (Image image : images) {
      ImageDTO imageDTO = new ImageDTO();
      imageDTO.loadFromDomain(image);
      convertedImages.add(imageDTO);
    }
    return ResponseEntity.ok(convertedImages);
  }

  @GetMapping(produces = MediaType.IMAGE_PNG_VALUE, value = "/{id}")
  public ResponseEntity<byte[]> findById(@PathVariable Long id) {
    return ResponseEntity.ok(bo.findById(id));
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
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }
}
