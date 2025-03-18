package es.judith.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import es.judith.security.CustomOpaqueTokenIntrospector;
import es.judith.security.CustomPasswordAuthenticationConverter;
import es.judith.security.CustomPasswordAuthenticationProvider;
import es.judith.security.CustomRefreshTokenAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final PasswordEncoder passwordEncoder;
  private final UserDetailsService userDetailsService;
  private final OAuth2AuthorizationService oauth2AuthorizationService;

  public SecurityConfig(
      PasswordEncoder passwordEncoder,
      UserDetailsService userDetailsService,
      OAuth2AuthorizationService oauth2AuthorizationService) {
    super();
    this.passwordEncoder = passwordEncoder;
    this.userDetailsService = userDetailsService;
    this.oauth2AuthorizationService = oauth2AuthorizationService;
  }

  @Bean
  @Order(1)
  SecurityFilterChain asSecurityFilterChain(HttpSecurity http) throws Exception {
    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

    http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
        .tokenEndpoint(
            tokenEndpoint ->
                tokenEndpoint
                    .accessTokenRequestConverter(new CustomPasswordAuthenticationConverter())
                    .authenticationProvider(
                        new CustomPasswordAuthenticationProvider(
                            this.oauth2AuthorizationService,
                            userDetailsService,
                            tokenGenerator(),
                            passwordEncoder))
                    .authenticationProvider(
                        new CustomRefreshTokenAuthenticationProvider(
                            oauth2AuthorizationService,
                            tokenGenerator(),
                            this.userDetailsService)));

    return http.build();
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            authz ->
                authz
                    .requestMatchers(
                        "/swagger-ui/**", "/webjars/**", "/v3/api-docs/**, /api/users/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/**")
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.POST,
                        "/api/actors/**, /api/characters/**, /api/episodes/**, /api/images/**, /api/seasons/**, /api/shows/**")
                    .hasAuthority("ADMIN")
                    .requestMatchers(
                        HttpMethod.POST, "/api/comments/**, /api/favourites/**, /api/reviews/**")
                    .hasAuthority("USER")
                    .requestMatchers(HttpMethod.PATCH, "/**")
                    .hasAuthority("ADMIN")
                    .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/actors/**, /api/characters/**, /api/episodes/**, /api/images/**, /api/seasons/**, /api/shows/**")
                    .hasAuthority("ADMIN")
                    .requestMatchers(
                        HttpMethod.DELETE, "/api/favourites/**, /api/comments/**, /api/reviews/**")
                    .hasAuthority("USER")
                    .anyRequest()
                    .permitAll())
        .oauth2ResourceServer(
            configurer ->
                configurer.opaqueToken(
                    opaqueTokenConfigurer ->
                        opaqueTokenConfigurer.introspector(opaqueTokenIntrospector())));

    return http.build();
  }

  @Bean
  OAuth2TokenGenerator<OAuth2Token> tokenGenerator() {
    final OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
    final OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();

    return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, refreshTokenGenerator);
  }

  @Bean
  OpaqueTokenIntrospector opaqueTokenIntrospector() {
    return new CustomOpaqueTokenIntrospector(this.oauth2AuthorizationService);
  }
}
