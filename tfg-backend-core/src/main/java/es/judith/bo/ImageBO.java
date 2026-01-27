package es.judith.bo;

import es.judith.domain.Image;

import java.util.List;

public interface ImageBO extends GenericCRUDService<Image, Long> {
    List<Image> findByName(String name);
    byte[] findById(Long id);
}
