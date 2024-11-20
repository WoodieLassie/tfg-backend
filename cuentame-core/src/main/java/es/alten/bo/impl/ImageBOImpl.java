package es.alten.bo.impl;

import es.alten.bo.ImageBO;
import es.alten.dao.ImageRepository;
import es.alten.domain.Image;
import es.alten.domain.QImage;
import es.alten.dto.ImageFilterDTO;
import es.alten.utils.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ImageBOImpl
    extends ElvisGenericCRUDServiceImpl<Image, Long, QImage, ImageFilterDTO, ImageRepository>
    implements ImageBO {

    private static final long serialVersionUID = -5108297259280380172L;
    private static final Logger LOG = LoggerFactory.getLogger(ImageBOImpl.class);

    public ImageBOImpl(ImageRepository repository) {
        super(repository);
    }

    @Override
    public List<Image> findByName(String name) {
        LOG.debug("ImageBOImpl: findByName");
        return repository.findByName(name);
    }

    @Override
    public byte[] findById(Long id) {
        LOG.debug("ImageBOImpl: findById");
        Optional<Image> dbImage = repository.findById(id);
        return dbImage.map(image -> ImageUtil.decompressImage(image.getImageData())).orElse(null);
    }
}
