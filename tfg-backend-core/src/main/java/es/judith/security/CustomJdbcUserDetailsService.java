package es.judith.security;

import java.util.Arrays;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import es.judith.bo.UserBO;
import es.judith.domain.User;

@Service
public class CustomJdbcUserDetailsService implements UserDetailsService {

  private final UserBO userBO;

  public CustomJdbcUserDetailsService(UserBO userBO) {
    this.userBO = userBO;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    final User user = this.userBO.findByEmail(username);
    if (user == null) {
      throw new UsernameNotFoundException("Invalid username");
    }

    final List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ADMIN"));

    return new es.judith.security.domain.CustomUserDetails(user.getId(), username, user.getPassword(),
        true, true, true, true, authorities);
  }

}