package es.alten.domain;

import es.alten.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.text.ParseException;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuditTest {

  Audit audit;

  Authentication auth;

  @BeforeEach
  void setUp() throws Exception {
    audit = new Audit();
  }

  @Test
  void testCreateAuditInfo() throws ParseException {
    mockAuthentication();
    when(auth.getPrincipal())
        .thenReturn(new User(Constants.ANONYMOUS_USER.toString(), "", Collections.emptyList()));
    audit.createAuditInfo();
    Assertions.assertEquals(Optional.empty(), Optional.empty());
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
    when(auth.getPrincipal())
        .thenReturn(new User(Constants.ANONYMOUS_USER.toString(), "", Collections.emptyList()));
    audit.updateAuditInfo();
    Assertions.assertEquals(Optional.empty(), Optional.empty());
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
