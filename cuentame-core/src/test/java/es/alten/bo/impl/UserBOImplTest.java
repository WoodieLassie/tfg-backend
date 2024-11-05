package es.alten.bo.impl;

import static org.mockito.ArgumentMatchers.anyString;
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
import es.alten.dao.UserRepository;
import es.alten.domain.User;


@ExtendWith(MockitoExtension.class)
class UserBOImplTest {

  private static final String EMAIL_PRUEBA = "emailPrueba";

  @InjectMocks
  UserBOImpl userBO;

  @Mock
  UserRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    this.userBO = new UserBOImpl(repository);
  }

  @Test
  void testFindByEmail() {
    User mock_user = new User();
    mock_user.setId(Long.valueOf(1));
    mock_user.setEmail(EMAIL_PRUEBA);
    when(repository.findByEmail(anyString())).thenReturn(mock_user);
    User userBBDD = userBO.findByEmail(EMAIL_PRUEBA);

    verify(repository, times(1)).findByEmail(EMAIL_PRUEBA);

    Assertions.assertNotNull(userBBDD);
    Assertions.assertEquals(userBBDD.getId(), Long.valueOf(1));
    Assertions.assertEquals(EMAIL_PRUEBA, userBBDD.getEmail());
  }
}
