package es.judith.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import es.judith.utils.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public class Audit extends ElvisEntity {

  private static final Logger LOG = LoggerFactory.getLogger(Audit.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "deleted")
  private Integer deleted;

  /** When object was created. */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_time", nullable = false, length = 19, updatable = false)
  private Date createDate;

  /** When object was last updated. */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "update_time", length = 19)
  private Date updateDate;

  /** Creator of the object. */
  @Column(name = "create_user_id", updatable = false)
  private Long createdBy;

  /** Last modifier of the object. */
  @Column(name = "update_user_id")
  private Long updatedBy;

  /** Updates audit info. */
  @PreUpdate
  public void updateAuditInfo() throws ParseException {

    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    Date now = dateFormatter.parse(Instant.now().toString());
    this.updateDate = now;

    if (SecurityContextHolder.getContext().getAuthentication() != null
        && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
      Long userLoggedId = Constants.ANONYMOUS_USER;
      if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal()
          .equals("anonymousUser")) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2IntrospectionAuthenticatedPrincipal principal = (OAuth2IntrospectionAuthenticatedPrincipal) authentication.getPrincipal();
        LinkedHashMap<String, String> principalAttributes = (LinkedHashMap<String, String>) principal.getAttributes().get("java.security.Principal");
        Object userDetails = principalAttributes.get("details");
        LinkedHashMap<String, Object> userDetailsHashMap = (LinkedHashMap<String, Object>) userDetails;
          assert userDetailsHashMap != null;
          Integer userId = (Integer) userDetailsHashMap.get("id");
        userLoggedId = Long.valueOf(userId);
      }
      this.deleted = 0;
      this.updatedBy = userLoggedId;
    }
  }

  /** Creates the audit info. */
  @PrePersist
  public void createAuditInfo() throws ParseException {

    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    Date now = dateFormatter.parse(Instant.now().toString());
    this.createDate = now;
    this.updateDate = now;

    if (SecurityContextHolder.getContext().getAuthentication() != null
        && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
      Long userLoggedId = Constants.ANONYMOUS_USER;
      if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal()
          .equals("anonymousUser")) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2IntrospectionAuthenticatedPrincipal principal = (OAuth2IntrospectionAuthenticatedPrincipal) authentication.getPrincipal();
        LinkedHashMap<String, String> principalAttributes = (LinkedHashMap<String, String>) principal.getAttributes().get("java.security.Principal");
        Object userDetails = principalAttributes.get("details");
        LinkedHashMap<String, Object> userDetailsHashMap = (LinkedHashMap<String, Object>) userDetails;
          assert userDetailsHashMap != null;
          Integer userId = (Integer) userDetailsHashMap.get("id");
        userLoggedId = Long.valueOf(userId);
      }
      this.createdBy = userLoggedId;
      this.updatedBy = userLoggedId;
      this.deleted = 0;
    }
  }
}