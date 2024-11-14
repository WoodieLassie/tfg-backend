package es.alten.bo;

import es.alten.domain.Image;
import es.alten.dto.ImageDTO;

import java.util.List;

public interface ImageBO {
    List<Image> findAll();
    List<Image> findByName(String name);
    byte[] findById(Long id);
    Image save(ImageDTO imageDTO);
}
