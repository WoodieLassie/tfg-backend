package es.judith.config;

import es.judith.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private BCryptPasswordEncoder passwordEncoder;
    {
        new BCryptPasswordEncoder(10);
    }
  private final CustomUserDetailsService userDetailsService;
  private final JwtFilter jwtFilter;

  public SecurityConfig(
          BCryptPasswordEncoder passwordEncoder,
          CustomUserDetailsService userDetailsService, JwtFilter jwtFilter) {
    super();
    this.passwordEncoder = passwordEncoder;
    this.userDetailsService = userDetailsService;
    this.jwtFilter = jwtFilter;
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authz ->
            authz
                .requestMatchers("error").permitAll()
                .requestMatchers("/api/users/login", "/api/users/register").permitAll()
                .requestMatchers(
                        "/swagger-ui/**", "/webjars/**", "/v3/api-docs/**, /api/users/login/**, /api/users/register/**")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/**")
                .permitAll()
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/actors/**, /api/characters/**, /api/episodes/**, /api/images/**, /api/seasons/**, /api/shows/**")
                .hasAuthority("ADMIN")
                .requestMatchers(
                        HttpMethod.POST, "/api/comments/**, /api/favourites/**, /api/reviews/**")
                .authenticated()
                .requestMatchers(HttpMethod.PATCH, "/**")
                .hasAuthority("ADMIN")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/actors/**, /api/characters/**, /api/episodes/**, /api/images/**, /api/seasons/**, /api/shows/**")
                .hasAuthority("ADMIN")
                .requestMatchers(
                        HttpMethod.DELETE, "/api/favourites/**, /api/comments/**, /api/reviews/**")
                .authenticated()
                .anyRequest().hasAuthority("ADMIN"))
                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(userDetailsService);
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}
