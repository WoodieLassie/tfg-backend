package es.alten.bo.impl;

import es.alten.dao.ImageRepository;
import es.alten.domain.Image;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageBOImplTest {

  private static final String IMAGE_TEST_NAME = "name";

  @InjectMocks ImageBOImpl imageBO;
  @Mock ImageRepository repository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.imageBO = new ImageBOImpl(repository);
  }

  @Test
  void testFindByName() {
    List<Image> mockImages = new ArrayList<>();
    Image mockImage = new Image();
    mockImage.setName(IMAGE_TEST_NAME);
    mockImage.setType("type");
    mockImage.setImageData("imageData".getBytes());

    when(repository.findByName(IMAGE_TEST_NAME)).thenReturn(mockImages);
    List<Image> dbImages = imageBO.findByName(IMAGE_TEST_NAME);

    verify(repository, times(1)).findByName(IMAGE_TEST_NAME);

    Assertions.assertNotNull(dbImages);
    for (Image dbImage : dbImages) {
      Assertions.assertEquals(IMAGE_TEST_NAME, dbImage.getName());
    }
    Assertions.assertEquals(dbImages.size(), mockImages.size());
  }

  @Test
  void testFindById() {
    Image mockImage = new Image();
    mockImage.setId(1L);
    mockImage.setName(IMAGE_TEST_NAME);
    mockImage.setType("type");
    mockImage.setImageData("imageData".getBytes());

    when(repository.findById(mockImage.getId())).thenReturn(Optional.of(mockImage));
    Optional<Image> dbImage = repository.findById(mockImage.getId());

    Assertions.assertNotNull(dbImage);
    Assertions.assertArrayEquals(mockImage.getImageData(), dbImage.get().getImageData());
  }

  @Test
  void saveTest() {
    Image mockImage = new Image();
    mockImage.setId(1L);
    given(repository.save(mockImage)).willReturn(mockImage);
    Image dbImage = imageBO.save(mockImage);

    verify(repository, times(1)).save(mockImage);

    Assertions.assertNotNull(dbImage);
    Assertions.assertEquals(mockImage, dbImage);
  }

  @Test
  void deleteTest() {
    Image mockImage = new Image();
    mockImage.setId(1L);
    willDoNothing().given(repository).deleteById(mockImage.getId());
    imageBO.delete(mockImage.getId());
    verify(repository, times(1)).deleteById(mockImage.getId());
  }
}
