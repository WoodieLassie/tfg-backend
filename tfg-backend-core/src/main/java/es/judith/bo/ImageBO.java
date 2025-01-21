package es.judith.bo;

import es.judith.domain.Image;
import es.judith.domain.QImage;
import es.judith.dto.ImageFilterDTO;

import java.util.List;

public interface ImageBO extends GenericCRUDService<Image, Long, QImage, ImageFilterDTO> {
    List<Image> findByName(String name);
    byte[] findById(Long id);
}
