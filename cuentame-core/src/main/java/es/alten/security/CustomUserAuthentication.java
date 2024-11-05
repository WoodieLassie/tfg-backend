package es.alten.security;

import es.alten.bo.UserBO;
import es.alten.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@SuppressWarnings("unused")
public class CustomUserAuthentication implements AuthenticationProvider {

  private UserBO userBO;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public void setUserBO(UserBO userBO) {
    this.userBO = userBO;
  }

  @Autowired
  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.security.authentication.AuthenticationProvider#
   * authenticate(org.springframework.security.core.Authentication)
   */
  @Override
  public Authentication authenticate(Authentication authentication) {

    Authentication auth;

    // Get credentials
    String name = authentication.getName();
    String password = authentication.getCredentials().toString();
    // Search the user in database
    final User usuario = userBO.findByEmail(name);

    if (usuario != null) {
      // If password is OK
      if (passwordEncoder.matches(password, "PASS")) {

        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority("ADMIN"));

        org.springframework.security.core.userdetails.User userDetails =
            new org.springframework.security.core.userdetails.User(
                usuario.getEmail(), "PASS", true, true, true, true, grantedAuths);

        auth = new UsernamePasswordAuthenticationToken(userDetails, password, grantedAuths);
      } else {
        throw new BadCredentialsException("Usuario o contraseña incorrectos");
      }
    } else {
      throw new UsernameNotFoundException("Usuario o contraseña incorrectos");
    }
    return auth;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.security.authentication.AuthenticationProvider#
   * supports(java.lang.Class)
   */
  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

  @SuppressWarnings("unused")
  public static UserDetails getLoggedUser() {
    UserDetails result = null;
    final Object userDetails =
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (userDetails instanceof UserDetails) {
      result = (UserDetails) userDetails;
    }

    return result;
  }
}
