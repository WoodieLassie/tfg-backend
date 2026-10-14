package es.judith.bo;

import es.judith.domain.user.User;

import java.util.List;

/** Define services to work with Users. */
public interface UserBO extends GenericBO<User, Long> {
  List<User> findAllByUsername(String username);
  User findByEmail(String email);
  User findByUsername(String username);
  byte[] findImageById(Long id);
  void promoteUser(Long id);
  boolean checkForIllegalStrings(String string, String fieldType);
  String generateImageUrl(Long userId);
}
