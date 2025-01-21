package es.judith.bo.impl;

import es.judith.dao.ActorRepository;
import es.judith.domain.Actor;
import es.judith.utils.ImageUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActorBOImplTest {
  @InjectMocks ActorBOImpl actorBO;
  @Mock ActorRepository repository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.actorBO = new ActorBOImpl(repository);
  }

  @Test
  void testFindByImageId() {
    Actor mockActor = new Actor();
    String data = "byte array mock";
    byte[] mockImageData = ImageUtil.compressImage(data.getBytes());
    mockActor.setImageData(mockImageData);
    mockActor.setId(1L);

    when(repository.findById(mockActor.getId())).thenReturn(Optional.of(mockActor));
    byte[] dbActor = actorBO.findImageById(mockActor.getId());

    verify(repository, times(1)).findById(mockActor.getId());

    Assertions.assertNotNull(dbActor);
    Assertions.assertArrayEquals(dbActor, data.getBytes());
  }

  @Test
  void saveTest() {
    Actor mockActor = new Actor();
    mockActor.setId(1L);
    given(repository.save(mockActor)).willReturn(mockActor);
    Actor dbActor = actorBO.save(mockActor);

    verify(repository, times(1)).save(mockActor);

    Assertions.assertNotNull(dbActor);
    Assertions.assertEquals(mockActor, dbActor);
  }

  @Test
  void deleteTest() {
    Actor mockActor = new Actor();
    mockActor.setId(1L);
    willDoNothing().given(repository).deleteById(mockActor.getId());
    actorBO.delete(mockActor.getId());
    verify(repository, times(1)).deleteById(mockActor.getId());
  }
}
