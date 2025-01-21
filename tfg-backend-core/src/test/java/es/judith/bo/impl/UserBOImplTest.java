package es.judith.bo.impl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import es.judith.dao.UserRepository;
import es.judith.domain.User;


@ExtendWith(MockitoExtension.class)
class UserBOImplTest {

  private static final String EMAIL_PRUEBA = "emailPrueba";

  @InjectMocks
  UserBOImpl userBO;

  @Mock
  UserRepository repository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    this.userBO = new UserBOImpl(repository);
  }

  @Test
  void testFindByEmail() {
    User mockUser = new User();
    mockUser.setId(1L);
    mockUser.setEmail(EMAIL_PRUEBA);
    when(repository.findByEmail(anyString())).thenReturn(mockUser);
    User userBBDD = userBO.findByEmail(EMAIL_PRUEBA);

    verify(repository, times(1)).findByEmail(EMAIL_PRUEBA);

    Assertions.assertNotNull(userBBDD);
    Assertions.assertEquals(userBBDD.getId(), Long.valueOf(1));
    Assertions.assertEquals(EMAIL_PRUEBA, userBBDD.getEmail());
  }
  @Test
  void saveTest() {
    User mockUser = new User();
    mockUser.setId(1L);
    given(repository.save(mockUser)).willReturn(mockUser);
    User dbUser = userBO.save(mockUser);

    verify(repository, times(1)).save(mockUser);

    Assertions.assertNotNull(dbUser);
    Assertions.assertEquals(mockUser, dbUser);
  }
  @Test
  void deleteTest() {
    User mockUser = new User();
    mockUser.setId(1L);
    willDoNothing().given(repository).deleteById(mockUser.getId());
    userBO.delete(mockUser.getId());
    verify(repository, times(1)).deleteById(mockUser.getId());
  }
}
