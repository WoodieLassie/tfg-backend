package es.alten.bo;

import es.alten.domain.Image;
import es.alten.domain.QImage;
import es.alten.dto.ImageDTO;
import es.alten.dto.ImageFilterDTO;

import java.util.List;

public interface ImageBO extends GenericCRUDService<Image, Long, QImage, ImageFilterDTO> {
    List<Image> findByName(String name);
    byte[] findById(Long id);
}
