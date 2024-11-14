package es.alten.bo;

import es.alten.domain.Image;
import es.alten.dto.ImageDTO;

public interface ImageBO {
    byte[] findById(Long id);
    byte[] findByName(String name);
    Image save(ImageDTO imageDTO);
}
