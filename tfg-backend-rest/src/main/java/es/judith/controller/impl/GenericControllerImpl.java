package es.judith.controller.impl;

import es.judith.bo.UserBO;
import es.judith.domain.User;
import es.judith.dto.UserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;

import java.util.LinkedHashMap;
import java.util.Objects;

public class GenericControllerImpl {
  private final UserBO userBO;

  public GenericControllerImpl(UserBO userBO) {
    this.userBO = userBO;
  }

  public UserDTO getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (Objects.equals(authentication.getPrincipal().toString(), "anonymousUser")) {
      return null;
    }
    OAuth2IntrospectionAuthenticatedPrincipal principal =
        (OAuth2IntrospectionAuthenticatedPrincipal) authentication.getPrincipal();
    LinkedHashMap<String, String> principalAttributes =
        (LinkedHashMap<String, String>) principal.getAttributes().get("java.security.Principal");
    Object userDetails = principalAttributes.get("details");
    LinkedHashMap<String, Object> userDetailsHashMap = (LinkedHashMap<String, Object>) userDetails;
    assert userDetailsHashMap != null;
    Integer userId = (Integer) userDetailsHashMap.get("id");
    Long userLoggedId = Long.valueOf(userId);
    User user = userBO.findOne(userLoggedId);
    UserDTO userDTO = new UserDTO();
    userDTO.loadFromDomain(user);
    return userDTO;
  }
}
