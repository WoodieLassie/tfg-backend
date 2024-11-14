package es.alten.bo;

import es.alten.domain.Image;
import es.alten.domain.QImage;
import es.alten.dto.ImageFilterDTO;

public interface ImageBO extends GenericCRUDService<Image, Long, QImage, ImageFilterDTO> {
    byte[] findByName(String name);
    Image save(Image image);
}
