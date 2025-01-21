package es.judith.bo.impl;

import es.judith.bo.ImageBO;
import es.judith.dao.ImageRepository;
import es.judith.domain.Image;
import es.judith.domain.QImage;
import es.judith.dto.ImageFilterDTO;
import es.judith.utils.ImageUtil;
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

    @Transactional(readOnly = true)
    @Override
    public List<Image> findByName(String name) {
        LOG.debug("ImageBOImpl: findByName");
        return repository.findByName(name);
    }

    @Transactional(readOnly = true)
    @Override
    public byte[] findById(Long id) {
        LOG.debug("ImageBOImpl: findById");
        Optional<Image> dbImage = repository.findById(id);
        return dbImage.map(image -> ImageUtil.decompressImage(image.getImageData())).orElse(null);
    }
}
