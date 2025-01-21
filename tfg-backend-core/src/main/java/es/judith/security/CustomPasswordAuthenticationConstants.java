package es.judith.security;

import org.springframework.security.oauth2.core.AuthorizationGrantType;

public final class CustomPasswordAuthenticationConstants {

  public static final String AUTHORIZATION_GRANT_TYPE_PASSWORD_NAME = "custom_password";
  public static final AuthorizationGrantType AUTHORIZATION_GRANT_TYPE_PASSWORD =
      new AuthorizationGrantType(AUTHORIZATION_GRANT_TYPE_PASSWORD_NAME);
  public static final String PRINCIPAL = "java.security.Principal";
  public static final String DETAILS = "details";
  public static final String AUTHORITIES = "authorities";
  public static final String AUTHORITY = "authority";

  private CustomPasswordAuthenticationConstants() {}
}