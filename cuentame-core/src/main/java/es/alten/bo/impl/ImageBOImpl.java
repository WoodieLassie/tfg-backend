package es.alten.bo.impl;

import es.alten.bo.ImageBO;
import es.alten.dao.ImageRepository;
import es.alten.domain.Image;
import es.alten.dto.ImageDTO;
import es.alten.utils.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ImageBOImpl
    implements ImageBO {

    private static final long serialVersionUID = -5108297259280380172L;
    private static final Logger LOG = LoggerFactory.getLogger(ImageBOImpl.class);
    private final ImageRepository repository;

    public ImageBOImpl(ImageRepository repository) {
        this.repository = repository;
    }

    public byte[] findById(Long id) {
        Optional<Image> dbImage = repository.findById(id);
        return dbImage.map(image -> ImageUtil.decompressImage(image.getImageData())).orElse(null);
    }

    public byte[] findByName(String name) {
        Optional<Image> dbImage = repository.findByName(name);
        return dbImage.map(image -> ImageUtil.decompressImage(image.getImageData())).orElse(null);
    }
    public Image save(ImageDTO imageDTO) {
        Image image = imageDTO.obtainDomainObject();
        repository.save(image);
        return image;
    }
}
