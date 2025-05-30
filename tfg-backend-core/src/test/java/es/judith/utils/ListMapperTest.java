package es.judith.utils;

import es.judith.domain.ElvisEntity;
import es.judith.domain.User;
import es.judith.dto.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

class ListMapperTest {

  ListMapper listMapper = new ListMapper();

  @Test
  void test() {
    List<UserDTO> listUser = new ArrayList<>();
    UserDTO userDTO = new UserDTO();
    userDTO.setEmail("");
    userDTO.setId(Long.valueOf(10));
    listUser.add(userDTO);
    List<ElvisEntity> listElvisEntity = ListMapper.map(listUser);

    Assertions.assertNotNull(listElvisEntity);
  }

  @Test
  void test1()
      throws IllegalAccessException, InstantiationException, NoSuchMethodException,
          InvocationTargetException {
    List<User> listUser = new ArrayList<>();
    User user = new User();
    user.setCreatedBy(Constants.ANONYMOUS_USER);
    user.setId(Long.valueOf(10));
    listUser.add(user);
    List<UserDTO> listElvisEntity = ListMapper.map(listUser, UserDTO.class);

    Assertions.assertNotNull(listElvisEntity);
  }
}
