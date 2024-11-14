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
public class ImageControllerImpl extends RestControllerImpl<Image, ImageDTO, Long, ImageBO>
    implements ImageController {
  private static final Logger LOG = LoggerFactory.getLogger(ImageControllerImpl.class);

  @Override
  @GetMapping(produces = MediaType.IMAGE_PNG_VALUE, params = {"name"})
  public ResponseEntity<byte[]> findByName(@RequestParam String name) {
    return ResponseEntity.ok(bo.findByName(name));
  }

  @PostMapping
  public ResponseEntity<Image> add(@RequestParam("image") MultipartFile file) throws IOException {
      Image image = new Image();
      image.setName(file.getOriginalFilename());
      image.setType(file.getContentType());
      image.setImageData(ImageUtil.compressImage(file.getBytes()));
      bo.save(image);
    return ResponseEntity.ok(image);
  }
}
