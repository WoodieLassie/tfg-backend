package es.alten.bo.impl;

import es.alten.dao.ActorRepository;
import es.alten.domain.Actor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActorBOImplTest {
    @InjectMocks
    ActorBOImpl actorBO;
    @Mock
    ActorRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.actorBO = new ActorBOImpl(repository);
    }

    @Test
    void testFindByImageId() {
        Actor mockActor = new Actor();
        byte[] mockImageData = "byte array mock".getBytes();
        mockActor.setImageData(mockImageData);
        mockActor.setId(1L);

        when(repository.findById(mockActor.getId())).thenReturn(Optional.of(mockActor));
        Optional<Actor> dbActor = repository.findById(mockActor.getId());

        verify(repository, times(1)).findById(mockActor.getId());

        Assertions.assertNotNull(dbActor);
        Assertions.assertArrayEquals(dbActor.get().getImageData(), mockImageData);
    }
}
