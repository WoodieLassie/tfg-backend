package es.judith.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;

import java.text.ParseException;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuditTest {

  Audit audit;

  Authentication auth;

  @BeforeEach
  void setUp() {
    audit = new Audit();
  }

  @Test
  void testCreateAuditInfo() throws ParseException {
    mockAuthentication();
    Map<String, Object> mockAuthenticatedPrincipal = new LinkedHashMap<>();
    Map<String, Object> mockDetails = new LinkedHashMap<>();
    Map<String, Object> mockUserDetails = new LinkedHashMap<>();
    mockUserDetails.put("id", 1);
    mockDetails.put("details", mockUserDetails);
    mockAuthenticatedPrincipal.put("java.security.Principal", mockDetails);
    when(auth.getPrincipal())
        .thenReturn(new OAuth2IntrospectionAuthenticatedPrincipal(mockAuthenticatedPrincipal, null));
    audit.createAuditInfo();
    Integer userId = (Integer) mockUserDetails.get("id");
    Long userLoggedId = Long.valueOf(userId);
    Assertions.assertEquals(userLoggedId, audit.getCreatedBy());
    Assertions.assertEquals(userLoggedId, audit.getUpdatedBy());
  }

  @Test
  void testCreateAuditInfo1() throws ParseException {
    mockAuthentication();
    when(auth.getPrincipal()).thenReturn("anonymousUser");
    audit.createAuditInfo();
    Assertions.assertEquals(Optional.empty(), Optional.empty());
  }

  @Test
  void testCreateAuditInfo2() throws ParseException {

    SecurityContext securityContext = mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(null);
    SecurityContextHolder.setContext(securityContext);
    audit.createAuditInfo();
    Assertions.assertEquals(Optional.empty(), Optional.empty());
  }

  @Test
  void testCreateAuditInfo3() throws ParseException {
    mockAuthentication();
    when(auth.getPrincipal()).thenReturn(null);
    audit.createAuditInfo();
    Assertions.assertEquals(Optional.empty(), Optional.empty());
  }

  @Test
  void testUpdateAuditInfo() throws ParseException {
    mockAuthentication();
    Map<String, Object> mockAuthenticatedPrincipal = new LinkedHashMap<>();
    Map<String, Object> mockDetails = new LinkedHashMap<>();
    Map<String, Object> mockUserDetails = new LinkedHashMap<>();
    mockUserDetails.put("id", 1);
    mockDetails.put("details", mockUserDetails);
    mockAuthenticatedPrincipal.put("java.security.Principal", mockDetails);
    when(auth.getPrincipal())
            .thenReturn(new OAuth2IntrospectionAuthenticatedPrincipal(mockAuthenticatedPrincipal, null));
    audit.updateAuditInfo();
    Integer userId = (Integer) mockUserDetails.get("id");
    Long userLoggedId = Long.valueOf(userId);
    Assertions.assertEquals(userLoggedId, audit.getUpdatedBy());
  }

  @Test
  void testUpdateAuditInfo1() throws ParseException {
    mockAuthentication();
    when(auth.getPrincipal()).thenReturn("anonymousUser");
    audit.updateAuditInfo();
    Assertions.assertEquals(Optional.empty(), Optional.empty());
  }

  @Test
  void testUpdateAuditInfo2() throws ParseException {
    SecurityContext securityContext = mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(null);
    SecurityContextHolder.setContext(securityContext);
    audit.updateAuditInfo();
    Assertions.assertEquals(Optional.empty(), Optional.empty());
  }

  @Test
  void testUpdateAuditInfo3() throws ParseException {
    mockAuthentication();
    when(auth.getPrincipal()).thenReturn(null);
    audit.updateAuditInfo();
    Assertions.assertEquals(Optional.empty(), Optional.empty());
  }

  private void mockAuthentication() {
    auth = mock(Authentication.class);
    SecurityContext securityContext = mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(auth);
    SecurityContextHolder.setContext(securityContext);
  }
}
